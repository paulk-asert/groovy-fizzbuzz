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

// The same 2x2 truth table, but as a switch. Groovy's switch matches a String
// label by equality, so stringifying the pair of bits gives the four cases
// something concrete to match — a decision-table switch that runs on Groovy 6
// today. (Groovy 7's GEP-19 list patterns will let you drop the .toString() and
// match the structure directly; see speculative/Groovy7Patterns.groovy.)
//   🥤 = Fizz (÷3),  🐝 = Buzz (÷5),  🥤🐝 = FizzBuzz (÷15)

def result = (1..20).collect { n ->
    switch ([n % 3 == 0, n % 5 == 0].toString()) {
        case '[true, true]'  -> '🥤🐝'
        case '[true, false]' -> '🥤'
        case '[false, true]' -> '🐝'
        default              -> n
    }
}

assert result.join(' ') ==
    '1 2 🥤 4 🐝 🥤 7 8 🥤 🐝 11 🥤 13 14 🥤🐝 16 17 🥤 19 🐝'

println result.join(' ')
// 1 2 🥤 4 🐝 🥤 7 8 🥤 🐝 11 🥤 13 14 🥤🐝 16 17 🥤 19 🐝
