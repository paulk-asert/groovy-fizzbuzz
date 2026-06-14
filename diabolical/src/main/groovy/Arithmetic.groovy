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

// No metaprogramming here — just plain arithmetic. It's dense, but everything is
// in plain sight at the call site: a 0/1 multiplier selects each fragment.
//   it % 3            is 0 when divisible, else 1 or 2
//   (it % 3 + 2) / 3  (integer div) is 0 when divisible, else 1
//   % 2 ^ 1           XOR-flips the bit: divisible -> 1, not -> 0
//   '🥤' * 1 == '🥤', '🥤' * 0 == '' (String.multiply repeats)
//   ?: it             empty string is falsy, so the number shows through
//   🥤 = Fizz (÷3),  🐝 = Buzz (÷5),  🥤🐝 = FizzBuzz (÷15)

def result = (1..20).collect {
    ('🥤' * ((it % 3 + 2) / 3 as int % 2 ^ 1) + '🐝' * ((it % 5 + 4) / 5 as int % 2 ^ 1)) ?: it
}

assert result.join(' ') ==
    '1 2 🥤 4 🐝 🥤 7 8 🥤 🐝 11 🥤 13 14 🥤🐝 16 17 🥤 19 🐝'

println result.join(' ')
// 1 2 🥤 4 🐝 🥤 7 8 🥤 🐝 11 🥤 13 14 🥤🐝 16 17 🥤 19 🐝
