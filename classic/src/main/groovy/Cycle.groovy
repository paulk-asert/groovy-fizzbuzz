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

// The "most elegant FizzBuzz" structure from the well-travelled Reddit thread:
// there is *no divisibility test in the mapping at all*. Two short periodic
// patterns — one of length 3, one of length 5 — are indexed by position and
// glued together. The classic Haskell rendition zips two infinite lazy cycles:
//
//   zipWith (++) (cycle ["","","🥤"]) (cycle ["","","","","🐝"])
//
// Indexing a period array by `(n - 1) % period` is the same idea without the
// laziness. As before, Elvis fills in the number when both fragments are empty.
//   🥤 = Fizz (÷3),  🐝 = Buzz (÷5),  🥤🐝 = FizzBuzz (÷15)

def fizz = ['', '', '🥤']         // period 3:  ".. .. 🥤"
def buzz = ['', '', '', '', '🐝'] // period 5:  ".. .. .. .. 🐝"

def fizzbuzz = { int n ->
    (fizz[(n - 1) % 3] + buzz[(n - 1) % 5]) ?: n.toString()
}

def result = (1..20).collect { fizzbuzz(it) }

assert result == ['1', '2', '🥤', '4', '🐝', '🥤', '7', '8', '🥤', '🐝',
                  '11', '🥤', '13', '14', '🥤🐝', '16', '17', '🥤', '19', '🐝']

println result.join(' ')
// 1 2 🥤 4 🐝 🥤 7 8 🥤 🐝 11 🥤 13 14 🥤🐝 16 17 🥤 19 🐝
