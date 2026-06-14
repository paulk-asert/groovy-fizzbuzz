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

// The same monoid proof as Join.groovy, trimmed. Newer groovy-verify *detects
// @Reducer automatically* and discharges the associativity and identity laws
// implicitly — so the explicit `associative` and `identity` law methods are no
// longer needed (and `emojiGlue` was only ever a bonus concrete-emoji check).
// All that remains is the combiner itself, carrying @Reducer and its defining
// equation; compiling this class still proves the full monoid contract that
// makes the seedless sumParallel site safe.
//   🥤 = Fizz (÷3),  🐝 = Buzz (÷5),  🥤🐝 = FizzBuzz (÷15)
@TypeChecked(extensions = ['groovy.typecheckers.CombinerChecker', 'verification.VerifyChecker'])
class JoinAuto {

    @Reducer(zero = '""')             // @Reducer alone now drives the implicit associativity + identity proofs
    @Ensures({ result == a + b })     // the combiner's defining equation, proven
    static String glue(String a, String b) { a + b }

    // The per-number renderer — the same FizzBuzz spec proven in FizzBuzz.groovy.
    static String spec(int n) {
        n % 15 == 0 ? '🥤🐝' : (n % 3 == 0 ? '🥤' : (n % 5 == 0 ? '🐝' : n.toString()))
    }

    static void parallelGlue() {
        // CombinerChecker certifies this seedless parallel site (glue is @Reducer);
        // groovy-verify has implicitly proven glue a monoid, so partition-and-
        // recombine is safe.
        (1..20).collect { spec(it) + ' ' }.sumParallel(JoinAuto::glue)
    }
}
// Compiling this class IS the demo: the monoid laws are proven from @Reducer alone.
