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

// Metaprogramming plus operator overloading: teach Integer a `multiply(String)`,
// so `(it % 3) * '🥤'` — an integer times an emoji — emits the emoji exactly when
// the integer is zero. The call site `((it % 3) * '🥤' + (it % 5) * '🐝') ?: it`
// reads almost normally; the cleverness all lives in the metaclass. Powerful, and
// best reserved for places it genuinely pays off (DSLs, builders) — not a kata.
//   🥤 = Fizz (÷3),  🐝 = Buzz (÷5),  🥤🐝 = FizzBuzz (÷15)

Integer.metaClass.multiply = { String s -> delegate == 0 ? s : '' }

def result = (1..20).collect { ((it % 3) * '🥤' + (it % 5) * '🐝') ?: it }

assert result.join(' ') ==
    '1 2 🥤 4 🐝 🥤 7 8 🥤 🐝 11 🥤 13 14 🥤🐝 16 17 🥤 19 🐝'

println result.join(' ')
// 1 2 🥤 4 🐝 🥤 7 8 🥤 🐝 11 🥤 13 14 🥤🐝 16 17 🥤 19 🐝

// One warning from bitter experience: a flashier version hides a divisibility
// detector in the overload itself and feeds it the RAW remainder —
//   Integer.metaClass.multiply = { String s -> ((delegate + s.size() - 1) / s.size() as int % 2 ^ 1) ? s : '' }
//   (((it % 3) * '🥤' + (it % 5) * '🐝') ?: it)
// That works for four-letter words ('Fizz'/'Buzz') but SILENTLY returns wrong
// answers with emoji: the decode is load-bearing on the string length, and an
// emoji's .size() is 2 (a surrogate pair). A reminder to keep clever
// metaprogramming both well-tested and well-justified.
