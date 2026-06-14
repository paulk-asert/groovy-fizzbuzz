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

// FizzBuzz is really a function of two yes/no questions — "divisible by 3?" and
// "divisible by 5?" — so the whole thing fits in a 2x2 truth table, indexed by
// those two bits. Row picks on ÷5, column picks on ÷3; Elvis fills in the number
// for the empty top-left cell.
//   🥤 = Fizz (÷3),  🐝 = Buzz (÷5),  🥤🐝 = FizzBuzz (÷15)

def fb = [['',   '🥤'],     //  not ÷5:  [number,  🥤  ]
          ['🐝', '🥤🐝']]    //      ÷5:  [🐝,      🥤🐝]

def fizzbuzz = { int n ->
    fb[n % 5 == 0 ? 1 : 0][n % 3 == 0 ? 1 : 0] ?: n.toString()
}

def result = (1..20).collect { fizzbuzz(it) }

assert result == ['1', '2', '🥤', '4', '🐝', '🥤', '7', '8', '🥤', '🐝',
                  '11', '🥤', '13', '14', '🥤🐝', '16', '17', '🥤', '19', '🐝']

println result.join(' ')
// 1 2 🥤 4 🐝 🥤 7 8 🥤 🐝 11 🥤 13 14 🥤🐝 16 17 🥤 19 🐝
