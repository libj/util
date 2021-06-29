# LibJ Util

[![Build Status](https://github.com/libj/util/actions/workflows/build.yml/badge.svg)](https://github.com/libj/util/actions/workflows/build.yml)
[![Coverage Status](https://coveralls.io/repos/github/libj/util/badge.svg)](https://coveralls.io/github/libj/util)
[![Javadocs](https://www.javadoc.io/badge/org.libj/util.svg)](https://www.javadoc.io/doc/org.libj/util)
[![Released Version](https://img.shields.io/maven-central/v/org.libj/util.svg)](https://mvnrepository.com/artifact/org.libj/util)
![Snapshot Version](https://img.shields.io/nexus/s/org.libj/util?label=maven-snapshot&server=https%3A%2F%2Foss.sonatype.org)

## Introduction

LibJ Util provides Java API Extensions to `java.util`.

Java's `java.util` package provides a wide range of utility classes, yet many common patterns are left for developers to implement themselves. To bridge the gap, LibJ Util complements Java's standard utility classes with implementations of such patterns.

## Essential Patterns

1. **Primitive Collections**

    An implementation of the Java's Collections API for primitive types, replicating Java's Collection API as closely as possible.

1. **DataEncoding**

    An abstraction of data encoding, with implementations offering: `Base32` and `Hexadecimal` algorithms.

1. **Buffers**

    An implementation of Java's `java.util.BitSet` class expressed as functions that operate on `byte[]` references.

1. **Bytes**

    Functions implementing common operations on `byte[]` references.

1. **Classes**

    Utility providing implementations of methods missing from the API of `java.lang.Class`.

1. **CompositeList**

    A list that is comprised of lists that each maintain elements conforming to a particular criterial or type.

1. **ConcurrentHashSet**

    A concurrent `java.util.HashSet` implementation backed by a `java.util.ConcurrentHashMap`.

1. **DelegateCollection**, **DelegateList**, **DelegateMap**, **DelegateSet**

    Implementations of interfaces belonging to Java's Collections API that delegate method calls to a target reference.

1. **Diff**

    An algorithm and encoding (the diff) for the representation of the steps necessary to transform a _target_ string to the _source_ string.

1. **Digraph**, **RefDigraph**

    A directed graph of an arbitrary-sized set of arbitrary-typed vertices, permitting self-loops and parallel edges.

1. **Enums.Mask**

    A utility class that provides functions to encode and decode `enum` instances to and from `int` or `long` values, whereby the bits of the `int` or `long` values represent the ordinal numbers of the `enum` instances.

1. **FastArrays**

    Utility functions that complement `java.util.Arrays`.

1. **FastCollections**

    Utility functions that complement `java.util.Collections`.

1. **BiMap**, **HashBiMap**

    Bidirectional map that maintains both key-&gt;value and value-&gt;key mappings.

1.  **Identifiers**

    Utility functions for checking or creating valid Java Identifiers as defined in <a href="https://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.8">JLS 3.8 Identifiers</a>.

1.  **Numbers.Unsigned**

    Utility functions to convert between signed and unsigned numbers.

1.  **ObservableCollection**, **ObservableList**, **ObservableMap**, **ObservableSet**

    Implementations of interfaces belonging to Java's Collections API that provide callback methods for observation of collection modification events.

1.  **Repeat**

    An abstraction of operations that execute recursively or iteratively in order to process collections or arrays (containers), and return statically allocated arrays, the sizes of which are not able to be known until evaluation of each member of the specified container.

1.  **SortedList**

    A `java.util.List` that guarantees sorted order of its members.

1.  **StreamSearcher**

    An efficient stream searching class based on the Knuth-Morris-Pratt algorithm.

1.  **Strings**

    Utility functions that provide common operations pertaining to `String` and `StringBuilder`.

1.  **TieredRangeFetcher**

    A "data fetcher" that facilitates the retrieval of data representing information pertaining to a dimension that can be denoted as a "range" (such as time or distance).

1.  **SynchronizingExecutorService**

    An `java.util.concurrent.ExecutorService` that allows its threads to be synchronized.

1.  **Retry**

    An abstraction of conditions and timing of when retries should be performed.

1.  **ZipFiles**

    Convenience utility functions for operations pertaining to zip files.

1.  **and more...**

## Contributing

Pull requests are welcome. For major changes, please [open an issue](../../issues) first to discuss what you would like to change.

Please make sure to update tests as appropriate.

### License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.