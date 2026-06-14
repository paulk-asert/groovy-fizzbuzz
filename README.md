<!--
SPDX-License-Identifier: Apache-2.0

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->

groovy-fizzbuzz
===============

Companion code for the blog post
**["FizzBuzz with Groovy and Emojis"](https://groovy.apache.org/blog/fizzbuzz-with-groovy-and-emojis)**.

The point isn't to find the *one best* FizzBuzz — it's to tour a bunch of
fun, genuinely different ways to write it, then render them all with the same
emoji for consistency:

> 🥤 = Fizz (÷3),  🐝 = Buzz (÷5),  🥤🐝 = FizzBuzz (÷15)

The emoji flourish is borrowed, with thanks, from Don Raab's
[*Ternary, Predicate, and Pattern Matching for FizzBuzz with Java 26*](https://donraab.medium.com/ternary-predicate-and-pattern-matching-for-fizzbuzz-with-java-26-646c812a137b).

Every runnable example maps the same range `(1..20)` and asserts the same
canonical output, so they're directly comparable:

    1 2 🥤 4 🐝 🥤 7 8 🥤 🐝 11 🥤 13 14 🥤🐝 16 17 🥤 19 🐝

Modules
-------

| Module         | Theme                                                                 |
|----------------|-----------------------------------------------------------------------|
| `classic`      | Nine everyday ways: closure-case `switch`, nested ternary, string-concat + Elvis, the periodic-cycle (zip) trick (finite and lazily-infinite), a 2&times;2 truth table (as a lookup and as a stringified-key `switch`), a CaseFunction-style predicate list, and GINQ |
| `concurrency`  | FizzBuzz in parallel — Groovy 6's integrated parallel collections (GPars folded into core, GEP-18) and the ordinary Java-Streams form |
| `verified`     | Element-wise array correctness proven at compile time by the [groovy-verify](https://github.com/paulk-asert/groovy-verify) SMT engine — plus a two-checker (`CombinerChecker` + `VerifyChecker`) string-concat join combiner, proven a monoid over the emoji |
| `diabolical`   | The expressive end — metaprogramming variants (an `Integer.fizzBuzz` property, an overloaded `Integer * String`) plus a plain-arithmetic one; powerful techniques, to reach for deliberately |
| `speculative/` | Not a module — a single illustrative file sketching the Groovy 7 GEP-19 primitive-pattern form (does **not** compile on Groovy 6) |

`classic` scripts (one run task each):

| Script             | Technique                                                       |
|--------------------|----------------------------------------------------------------|
| `Switches.groovy`  | `switch` expression with closure cases — the idiomatic Groovy form |
| `Ternary.groovy`   | Nested ternary — the most concise form (and the verified `spec`) |
| `Concat.groovy`    | Glue 🥤/🐝 fragments, let the empty string vanish, Elvis for the number |
| `Cycle.groovy`     | Index two periodic patterns — "the most elegant FizzBuzz", no divisibility test in the mapping |
| `LazyCycle.groovy` | The genuinely lazy version — `repeat()`/`zip`/`indexed`/`collecting`/`take` over two infinite streams (the Haskell `zip (cycle…)` verbatim) |
| `Table.groovy`     | Index a 2&times;2 truth table — FizzBuzz as a function of two bits (÷3?, ÷5?) |
| `TruthSwitch.groovy`| The same truth table as a `switch` — stringify the pair of bits so the four cases match by equality |
| `Predicates.groovy`| An ordered list of (predicate, result) cases — CaseFunction as data |
| `Ginq.groovy`      | `from … select …` — the SQL-shaped Groovy-Integrated Query form |

Groovy / build
--------------

Everything resolves **Groovy `6.0.0-SNAPSHOT` from the ASF snapshot
repository**, the same self-contained way the sibling
[`groovy-verify`](https://github.com/paulk-asert/groovy-verify) project does — no
hand-built Groovy in `mavenLocal` required. The `verified` module additionally
pulls the verification engine via a Gradle composite build
(`includeBuild('../groovy-verify')`); check that repo out alongside this one.

Run an example from a module directory (or with the fully-qualified task name):

    ./gradlew :classic:run.Switches
    ./gradlew :classic:run.Cycle
    ./gradlew :concurrency:run.Parallel
    ./gradlew :concurrency:run.Streams
    ./gradlew :verified:run.FizzBuzz      # builds the proven array and prints it
    ./gradlew :verified:run.BadFizzBuzz   # shows the off-by-one refuted, with a counterexample
    ./gradlew :verified:run.JoinDemo      # parallel join == sequential join (Join is a proven monoid)
    ./gradlew :verified:run.BadJoin       # shows a false string law (commutativity) refuted, with an emoji witness

    ./gradlew :diabolical:run.NumbersKnow    # Integer.fizzBuzz property via ExpandoMetaClass
    ./gradlew :diabolical:run.CursedMultiply # overloaded Integer * String
    ./gradlew :diabolical:run.Arithmetic     # plain arithmetic, no metaprogramming

    ./gradlew :classic:run.classic.All    # every classic script
    ./gradlew compileGroovy               # compiling :verified IS the proof

The `verified` module compiles only because the Z3-backed checker can prove
every slot of the built array is faithful to `spec(i + 1)`; introduce the
off-by-one and compilation fails with a named counterexample (see
`BadFizzBuzz`, which provokes exactly that on the fly so this module stays
green).
