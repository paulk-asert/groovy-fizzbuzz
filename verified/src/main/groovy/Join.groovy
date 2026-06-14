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
import groovy.contracts.Ensures
import groovy.transform.Reducer
import groovy.transform.TypeChecked

// Weaving the two prior examples together. The parallel version assembles its
// per-number tokens; to combine them in parallel — partition, render, recombine
// — the combiner has to be associative, or the answer can depend on how the work
// was split. The combiner here is the most natural one in FizzBuzz: gluing the
// rendered emoji tokens together, i.e. *string concatenation*, which is a monoid.
//
// This is a genuine two-checker compile (the shape of the groovy-verify
// CombinerChecker example): CombinerChecker checks the combiner's *shape* (it
// trusts @Reducer and certifies the seedless sumParallel site); groovy-verify
// proves the *semantics* — Groovy's `+` on String lowers to Z3's theory of
// sequences, so the monoid laws are proven over *arbitrary* strings, and it
// reasons about the actual emoji too.
//   🥤 = Fizz (÷3),  🐝 = Buzz (÷5),  🥤🐝 = FizzBuzz (÷15)
@TypeChecked(extensions = ['groovy.typecheckers.CombinerChecker', 'verification.VerifyChecker'])
class Join {

    @Reducer(zero = '""')             // string concat is a monoid; CombinerChecker trusts this & checks the seed
    @Ensures({ result == a + b })     // the combiner's defining equation, proven
    static String glue(String a, String b) { a + b }

    @Ensures({ (a + b) + c == a + (b + c) })   // associativity — proven for ALL strings (the law sumParallel needs)
    static void associative(String a, String b, String c) { }

    @Ensures({ a + '' == a && '' + a == a })   // identity — '' is a true zero, proven
    static void identity(String a) { }

    @Ensures({ '🥤' + '🐝' == '🥤🐝' })          // not just abstract strings — the actual emoji, proven
    static void emojiGlue() { }

    // The per-number renderer — the same FizzBuzz spec proven in FizzBuzz.groovy.
    static String spec(int n) {
        n % 15 == 0 ? '🥤🐝' : (n % 3 == 0 ? '🥤' : (n % 5 == 0 ? '🐝' : n.toString()))
    }

    static void parallelGlue() {
        // CombinerChecker certifies this seedless parallel site (glue is @Reducer).
        // Because glue is a proven monoid, the parallel join equals the sequential
        // one for any split — partition-and-recombine is safe.
        (1..20).collect { spec(it) + ' ' }.sumParallel(Join::glue)
    }
}
// Compiling this class IS the demo: both checkers agree. JoinDemo.groovy runs it.
