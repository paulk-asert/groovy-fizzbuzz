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

// GINQ — Groovy-Integrated Query — gives FizzBuzz a SQL-shaped spelling.
// `from … select …` over the range, with the mapping in the `select`.
// Contrived for a problem this small, but a genuinely different paradigm,
// and it scales to filtering, grouping and joins when the query grows.
//   🥤 = Fizz (÷3),  🐝 = Buzz (÷5),  🥤🐝 = FizzBuzz (÷15)

def result = GQ {
    from n in (1..20)
    select n % 15 == 0 ? '🥤🐝'
        : n % 3 == 0 ? '🥤'
        : n % 5 == 0 ? '🐝'
        : n.toString()
}.toList()

assert result == ['1', '2', '🥤', '4', '🐝', '🥤', '7', '8', '🥤', '🐝',
                  '11', '🥤', '13', '14', '🥤🐝', '16', '17', '🥤', '19', '🐝']

println result.join(' ')
// 1 2 🥤 4 🐝 🥤 7 8 🥤 🐝 11 🥤 13 14 🥤🐝 16 17 🥤 19 🐝
