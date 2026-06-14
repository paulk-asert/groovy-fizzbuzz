/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.codehaus.groovy.control.CompilationFailedException
import org.codehaus.groovy.control.MultipleCompilationErrorsException
import org.codehaus.groovy.control.messages.SyntaxErrorMessage

/**
 * The off-by-one, refuted at compile time. The broken build fills
 * `r[i] = spec(i)` instead of `spec(i + 1)`, so slot 0 holds spec(0) == "🥤🐝"
 * (0 divides everything) where the spec for FizzBuzz number 1 requires "1".
 * The loop invariant is no longer preserved and the verifier names the
 * offending slot with a concrete counterexample.
 *
 * Compiled on the fly via GroovyClassLoader so this module itself stays green —
 * the checker turns the bad fill into a compile error, which we catch and print.
 *
 * Run with:
 *   ../gradlew :verified:run.BadFizzBuzz
 */
class BadFizzBuzz {

    static final String SOURCE = '''
        import groovy.contracts.Decreases
        import groovy.contracts.Ensures
        import groovy.contracts.Invariant
        import groovy.contracts.Requires
        import groovy.transform.TypeChecked

        @TypeChecked(extensions = 'verification.VerifyChecker')
        class BrokenFizzBuzz {
            @Ensures({ result == (n % 15 == 0 ? '🥤🐝' : (n % 3 == 0 ? '🥤' : (n % 5 == 0 ? '🐝' : n.toString()))) })
            static String spec(int n) {
                n % 15 == 0 ? '🥤🐝' : (n % 3 == 0 ? '🥤' : (n % 5 == 0 ? '🐝' : n.toString()))
            }

            @Requires({ upTo >= 1 })
            @Ensures({ result.length == upTo })
            @Ensures({ (0..<upTo).every { int k -> result[k] == BrokenFizzBuzz.spec(k + 1) } })
            static String[] build(int upTo) {
                String[] r = new String[upTo]
                int i = 0
                @Invariant({ 0 <= i && i <= upTo && r.length == upTo && (0..<i).every { int k -> r[k] == BrokenFizzBuzz.spec(k + 1) } })
                @Decreases({ upTo - i })
                while (i < upTo) {
                    r[i] = BrokenFizzBuzz.spec(i)   // BUG: should be spec(i + 1)
                    i = i + 1
                }
                return r
            }
        }
    '''.stripIndent()

    static void main(String[] args) {
        println '=' * 70
        println 'Compiling the off-by-one FizzBuzz fill (r[i] = spec(i))...'
        println '-' * 70

        def gcl = new GroovyClassLoader(Thread.currentThread().contextClassLoader)
        try {
            gcl.parseClass(SOURCE, 'BrokenFizzBuzz.groovy')
            println '  [!] UNEXPECTED: compilation succeeded — the checker missed the off-by-one.'
            System.exit(1)
        } catch (MultipleCompilationErrorsException e) {
            def messages = e.errorCollector.errors.collect { err ->
                err instanceof SyntaxErrorMessage ? err.cause.message : err.toString()
            }
            messages.each { m -> println "  ${m.replaceAll(/\n/, '\n  ')}" }
            String all = messages.join('\n')
            assert all.contains('invariant') || all.contains('Cannot prove')
            println '-' * 70
            println '  [ok] The off-by-one was refuted at compile time, with a counterexample.'
        } catch (CompilationFailedException e) {
            println "  Compilation failed: ${e.message}"
        } finally {
            try { gcl.close() } catch (Throwable ignored) {}
        }
    }
}
