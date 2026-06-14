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
import groovy.contracts.Decreases
import groovy.contracts.Ensures
import groovy.contracts.Invariant
import groovy.contracts.Requires
import groovy.transform.TypeChecked

// A small array-fill in the style Dafny tutorials use to teach element-wise
// verification — a pure specification `spec(n)`, a loop that fills
// `r[i] = spec(i + 1)`, and a postcondition saying *every* slot matches it.
// The groovy-verify SMT engine discharges the contracts at COMPILE time: this
// file only compiles because no slot can hold the wrong emoji.
//
// FizzBuzz counts from 1, so 0-based slot `i` holds the value for number
// `i + 1`. That `+ 1` is the bridge between array index and FizzBuzz number —
// and exactly what BadFizzBuzz gets wrong. The emoji flourish is borrowed, with
// thanks, from Don Raab's "Ternary, Predicate, and Pattern Matching for
// FizzBuzz with Java 26".
// The checker is turned on per method (it isn't inherited), so `main` below
// stays plain dynamic Groovy while spec/build carry the proof obligations.
//   🥤 = Fizz (÷3),  🐝 = Buzz (÷5),  🥤🐝 = FizzBuzz (÷15)
class FizzBuzz {

    // The spec restates itself as an @Ensures so it inlines equationally: the
    // body's spec(i + 1) and the invariant's spec(k + 1) become one term.
    @Ensures({ result == (n % 15 == 0 ? '🥤🐝' : (n % 3 == 0 ? '🥤' : (n % 5 == 0 ? '🐝' : n.toString()))) })
    @TypeChecked(extensions = 'verification.VerifyChecker')
    static String spec(int n) {
        n % 15 == 0 ? '🥤🐝' : (n % 3 == 0 ? '🥤' : (n % 5 == 0 ? '🐝' : n.toString()))
    }

    @Requires({ upTo >= 1 })
    @TypeChecked(extensions = 'verification.VerifyChecker')
    @Ensures({ result.length == upTo })                                           // exactly the size requested
    @Ensures({ (0..<upTo).every { int k -> result[k] == FizzBuzz.spec(k + 1) } }) // every element provably correct
    static String[] build(int upTo) {
        String[] r = new String[upTo]
        int i = 0
        @Invariant({ 0 <= i && i <= upTo && r.length == upTo && (0..<i).every { int k -> r[k] == FizzBuzz.spec(k + 1) } })
        @Decreases({ upTo - i })
        while (i < upTo) {
            r[i] = FizzBuzz.spec(i + 1)
            i = i + 1
        }
        return r
    }

    static void main(String[] args) {
        String[] result = build(20)
        assert result.toList() == ['1', '2', '🥤', '4', '🐝', '🥤', '7', '8', '🥤', '🐝',
                                   '11', '🥤', '13', '14', '🥤🐝', '16', '17', '🥤', '19', '🐝']
        println result.join(' ')
        // 1 2 🥤 4 🐝 🥤 7 8 🥤 🐝 11 🥤 13 14 🥤🐝 16 17 🥤 19 🐝
        println 'Proven at compile time: every slot is faithful to spec(i + 1).'
    }
}
