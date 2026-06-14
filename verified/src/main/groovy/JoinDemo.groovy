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

// Plain Groovy that exercises the proven Join combiner. Join itself compiled
// only because both checkers agreed (CombinerChecker on the shape, the Z3-backed
// verifier on the semantics — the monoid laws over arbitrary strings). Here we
// just run it, and confirm what the laws guarantee: joining the tokens in
// parallel gives exactly the same string as joining them sequentially.
//   🥤 = Fizz (÷3),  🐝 = Buzz (÷5),  🥤🐝 = FizzBuzz (÷15)

def tokens = (1..20).collect { Join.token(it) + ' ' }

def sequential = tokens.inject('') { a, b -> Join.glue(a, b) }
def parallel = ParallelScope.withPool(Pool.virtual()) { tokens.sumParallel(Join::glue) }

// The very same proven combiner satisfies java.util.stream.Stream.reduce's
// documented contract: reduce(identity, accumulator) requires identity to be an
// identity ('') and accumulator to be associative (glue) — exactly the monoid
// laws we proved. So it also drives a parallel-stream reduce, correctly.
def viaStream = tokens.parallelStream().reduce('', Join::glue)

assert sequential == parallel                       // guaranteed by the proven monoid laws
assert sequential == viaStream                      // ditto — Stream.reduce needs the same monoid
assert sequential.trim() == '1 2 🥤 4 🐝 🥤 7 8 🥤 🐝 11 🥤 13 14 🥤🐝 16 17 🥤 19 🐝'

println parallel.trim()
println 'Parallel join == sequential join == parallel-stream reduce —'
println 'all guaranteed by the one compile-time monoid proof.'
