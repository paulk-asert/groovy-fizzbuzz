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

// Data, not control flow. This is the shape of Eclipse Collections' CaseFunction
// (Don Raab's predicate-based variant): an ordered list of (predicate, result)
// cases, first match wins, with the number as the default. New rules are added
// by appending to the list rather than editing a switch — the cases are values
// you can build, pass around, and reuse.
//   🥤 = Fizz (÷3),  🐝 = Buzz (÷5),  🥤🐝 = FizzBuzz (÷15)

def cases = [
    [{ int n -> n % 15 == 0 }, '🥤🐝'],
    [{ int n -> n % 3 == 0 },  '🥤'],
    [{ int n -> n % 5 == 0 },  '🐝'],
]

def fizzbuzz = { int n ->
    cases.find { pred, _ -> pred(n) }?.last() ?: n.toString()
}

def result = (1..20).collect { fizzbuzz(it) }

assert result == ['1', '2', '🥤', '4', '🐝', '🥤', '7', '8', '🥤', '🐝',
                  '11', '🥤', '13', '14', '🥤🐝', '16', '17', '🥤', '19', '🐝']

println result.join(' ')
// 1 2 🥤 4 🐝 🥤 7 8 🥤 🐝 11 🥤 13 14 🥤🐝 16 17 🥤 19 🐝
