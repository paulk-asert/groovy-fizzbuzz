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
import org.codehaus.groovy.control.MultipleCompilationErrorsException
import org.codehaus.groovy.control.messages.SyntaxErrorMessage

/**
 * The other half of "shape beside semantics": the verifier refuting a string
 * law that does NOT hold. Concatenation is associative but NOT commutative, so
 * a combiner claiming commutativity is rejected — with a minimal emoji witness.
 *
 * Compiled on the fly via GroovyClassLoader so this module stays green.
 *
 * Run with:
 *   ../gradlew :verified:run.BadJoin
 */
class BadJoin {

    static final String SOURCE = '''
        import groovy.contracts.Ensures
        import groovy.transform.TypeChecked

        @TypeChecked(extensions = 'verification.VerifyChecker')
        class CommutativeGlue {
            @Ensures({ '🥤' + a == a + '🥤' })   // FALSE: concat is not commutative
            static void claim(String a) { }
        }
    '''.stripIndent()

    static void main(String[] args) {
        println '=' * 70
        println 'Claiming string concat is commutative (🥤 + a == a + 🥤)...'
        println '-' * 70

        def gcl = new GroovyClassLoader(Thread.currentThread().contextClassLoader)
        try {
            gcl.parseClass(SOURCE, 'CommutativeGlue.groovy')
            println '  [!] UNEXPECTED: compilation succeeded — the false law slipped through.'
            System.exit(1)
        } catch (MultipleCompilationErrorsException e) {
            def messages = e.errorCollector.errors.collect { err ->
                err instanceof SyntaxErrorMessage ? err.cause.message : err.toString()
            }
            messages.each { m -> println "  ${m.replaceAll(/\n/, '\n  ')}" }
            assert messages.join('\n').contains('Cannot prove')
            println '-' * 70
            println '  [ok] The non-law was refuted at compile time, with a witness.'
        } finally {
            try { gcl.close() } catch (Throwable ignored) {}
        }
    }
}
