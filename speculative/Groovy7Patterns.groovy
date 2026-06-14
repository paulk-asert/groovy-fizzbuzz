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

// ┌──────────────────────────────────────────────────────────────────────────┐
// │  SPECULATIVE — Groovy 7, NOT IMPLEMENTED. This file is illustrative only;  │
// │  it is deliberately kept OUT of the Gradle build and will not compile on   │
// │  Groovy 6. It sketches what FizzBuzz could look like under the primitive   │
// │  type-pattern + `when` guard syntax proposed by GEP-19 (Structural Pattern │
// │  Matching in switch), aligned with Java 26's primitive patterns (JEP 507). │
// └──────────────────────────────────────────────────────────────────────────┘
//
// Don Raab waited ten years to write the Java 26 version:
//
//   each -> switch (each) {
//       case int i when i % 15 == 0 -> "🥤🐝";
//       case int i when i % 3 == 0  -> "🥤";
//       case int i when i % 5 == 0  -> "🐝";
//       default -> Integer.toString(each);
//   };
//
// Groovy didn't have to wait, though: the closure-case switch in
// classic/Switches.groovy already expresses this elegantly and runs on every
// currently supported JDK. What GEP-19 adds is the *exact* match of Java's
// spelling — a primitive type pattern `int i` that binds and narrows within the
// case body and its `when` guard. It brings the syntax, not the power:
//   🥤 = Fizz (÷3),  🐝 = Buzz (÷5),  🥤🐝 = FizzBuzz (÷15)

static String fizzbuzz(int n) {
    switch (n) {
        case int i when i % 15 == 0 -> '🥤🐝'
        case int i when i % 3 == 0  -> '🥤'
        case int i when i % 5 == 0  -> '🐝'
        default                     -> n.toString()
    }
}

def result = (1..20).collect { fizzbuzz(it) }

assert result == ['1', '2', '🥤', '4', '🐝', '🥤', '7', '8', '🥤', '🐝',
                  '11', '🥤', '13', '14', '🥤🐝', '16', '17', '🥤', '19', '🐝']

println result.join(' ')
// Expected once Groovy 7 ships GEP-19:
// 1 2 🥤 4 🐝 🥤 7 8 🥤 🐝 11 🥤 13 14 🥤🐝 16 17 🥤 19 🐝

// GEP-19 also brings *list patterns* — the truth table as a literal decision
// table over the two remainders. Two consequences of the disambiguation rule:
// the wildcard is `var _` (bare `_` is an ordinary identifier), and an
// all-constant label like `[0, 0]` keeps its legacy isCase meaning, so the
// both-zero row carries a binding-and-guard rather than matching a constant pair.
static fizzbuzzTable(int n) {
    switch ([n % 3, n % 5]) {
        case [var a, var b] when a == 0 && b == 0 -> '🥤🐝'
        case [0, var _]                           -> '🥤'
        case [var _, 0]                           -> '🐝'
        default                                   -> n
    }
}
