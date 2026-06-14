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
import groovy.concurrent.Pool
import groovy.concurrent.ParallelScope

// FizzBuzz in parallel. The gpars-meets-virtual-threads blog ran this on the
// GPars library; in Groovy 6 the GPars patterns are integrated into the core
// (GEP-18). `ParallelScope.withPool(Pool.virtual())` binds a virtual-thread
// pool for the scope, and `collectParallel` is the parallel `collect`. The
// mapping is unchanged from the ordinary-stream version next door, and
// `collectParallel` preserves encounter order, so the result is identical.
//   🥤 = Fizz (÷3),  🐝 = Buzz (÷5),  🥤🐝 = FizzBuzz (÷15)

def fizzbuzz = { int n ->
    n % 15 == 0 ? '🥤🐝'
        : n % 3 == 0 ? '🥤'
        : n % 5 == 0 ? '🐝'
        : n.toString()
}

def result = ParallelScope.withPool(Pool.virtual()) { scope ->
    (1..20).collectParallel { fizzbuzz(it) }
}

assert result == ['1', '2', '🥤', '4', '🐝', '🥤', '7', '8', '🥤', '🐝',
                  '11', '🥤', '13', '14', '🥤🐝', '16', '17', '🥤', '19', '🐝']

println result.join(' ')
// 1 2 🥤 4 🐝 🥤 7 8 🥤 🐝 11 🥤 13 14 🥤🐝 16 17 🥤 19 🐝
