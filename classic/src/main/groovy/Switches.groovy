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

// The idiomatic Groovy way: a switch *expression* with closure cases.
// Each `case { … }` is a predicate applied to the switch subject (`it`),
// first match wins. This is the form the gpars-meets-virtual-threads blog
// used for the word version of FizzBuzz — here with emoji.
//   🥤 = Fizz (÷3),  🐝 = Buzz (÷5),  🥤🐝 = FizzBuzz (÷15)

def fizzbuzz = { int n ->
    switch (n) {
        case { it % 15 == 0 } -> '🥤🐝'
        case { it % 3 == 0 }  -> '🥤'
        case { it % 5 == 0 }  -> '🐝'
        default               -> n.toString()
    }
}

def result = (1..20).collect { fizzbuzz(it) }

assert result == ['1', '2', '🥤', '4', '🐝', '🥤', '7', '8', '🥤', '🐝',
                  '11', '🥤', '13', '14', '🥤🐝', '16', '17', '🥤', '19', '🐝']

println result.join(' ')
// 1 2 🥤 4 🐝 🥤 7 8 🥤 🐝 11 🥤 13 14 🥤🐝 16 17 🥤 19 🐝
