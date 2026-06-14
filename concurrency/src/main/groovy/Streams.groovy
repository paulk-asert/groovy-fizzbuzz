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
import java.util.stream.IntStream

// Don Raab's IntStream rendition, in Groovy. `rangeClosed(1, 20)` is the
// natural-number source; `mapToObj` applies the mapping (here the same nested
// ternary the verified spec uses); `toList()` collects. Pure Java Streams —
// the point being that the very same mapping drops straight into the parallel
// version next door, where the only change is `parallel()`/`collectParallel`.
//   🥤 = Fizz (÷3),  🐝 = Buzz (÷5),  🥤🐝 = FizzBuzz (÷15)

def fizzbuzz = { int n ->
    n % 15 == 0 ? '🥤🐝'
        : n % 3 == 0 ? '🥤'
        : n % 5 == 0 ? '🐝'
        : n.toString()
}

def result = IntStream.rangeClosed(1, 20)
        .mapToObj { fizzbuzz(it) }
        .toList()

assert result == ['1', '2', '🥤', '4', '🐝', '🥤', '7', '8', '🥤', '🐝',
                  '11', '🥤', '13', '14', '🥤🐝', '16', '17', '🥤', '19', '🐝']

println result.join(' ')
// 1 2 🥤 4 🐝 🥤 7 8 🥤 🐝 11 🥤 13 14 🥤🐝 16 17 🥤 19 🐝
