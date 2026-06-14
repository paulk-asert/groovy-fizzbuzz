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

// The genuinely lazy rendition of the cycle trick — almost the Haskell version
// verbatim. `repeat()` cycles a finite iterator forever, `zip` pairs the two
// infinite streams, `indexed(1)` numbers them from 1, and nothing is computed
// until `take(20)` pulls a prefix. Compare Cycle.groovy, which fakes the cycle
// with `% period`; here the iterators really do cycle.
//
// This matches the number-retaining Haskell form (zip against [1..], keep the
// number when the glue is empty) — the Elvis `?: n` doing `if null s then show n`:
//   zipWith (\n s -> if null s then show n else s)
//           [1..]
//           (zipWith (++) (cycle ["","","🥤"]) (cycle ["","","","","🐝"]))
//
//   🥤 = Fizz (÷3),  🐝 = Buzz (÷5),  🥤🐝 = FizzBuzz (÷15)

def fizz = ['', '', '🥤'].iterator().repeat()          // ['', '', '🥤', '', '', '🥤', …]
def buzz = ['', '', '', '', '🐝'].iterator().repeat()  // ['', '', '', '', '🐝', …]

def result = fizz.zip(buzz)
        .indexed(1)
        .collecting { n, s -> s[0] + s[1] ?: n }       // glue the two fragments; Elvis → the number
        .take(20)
        .toList()

assert result.join(' ') ==
    '1 2 🥤 4 🐝 🥤 7 8 🥤 🐝 11 🥤 13 14 🥤🐝 16 17 🥤 19 🐝'

println result.join(' ')
// 1 2 🥤 4 🐝 🥤 7 8 🥤 🐝 11 🥤 13 14 🥤🐝 16 17 🥤 19 🐝
