# Changes by Version

## [v0.9.1-SNAPSHOT](https://github.com/libj/util/compare/d593a84124808733f13036e96f551e2fb8cf6088..HEAD)

## [v0.9.0](https://github.com/libj/util/compare/d8bc651739ee7c7a945c8ffad2a1dc6c6c0af904..d593a84124808733f13036e96f551e2fb8cf6088) (2023-09-20)
* #47 Implement DiscreteTopology
* #46 Support boolean in primitive collections
* #45 Add Dates.MIN_VALUE and Dates.MAX_VALUE
* #43 Add ObjBi<X>Predicate
* #42 Implement Unmodifiable* alternatives to Collections.unmodifiable*
* #41 Add UnsynchronizedGZIPOutputStream
* #40 Add DelegateOutputStream and ObservableOutputStream
* #39 Implement Dates.durationToString() and Dates.stringToDuration()
* #38 Implement MultiMap
* #37 StringPaths.canonicalize(...) mishandles '^../../'
* #36 Implement ArrayUtil.dedupe(...) and CollectionUtil.dedupe(...)
* #35 Externalize RetryOn as FunctionalInterface in RetryPolicy.run(...)
* #34 Implement Shutdownable and Shutdownables
* #33 Implement ExecutorServices.interruptAfterTimeout(...)
* #32 Implement ExecutorServices.invokeAll(...)
* #31 Support RetryPolicy.run(Runnable) and RetryPolicy.run(Callable)
* #30 RetryFailureException.addSuppressed for all unique exceptions in Retry
* #29 Implement fast CRC32 and CRC64
* #28 ISO_8601 and ISO_1123 in SimpleDateFormats
* #27 Support override of equals(Object,Object) test
* #26 Implement Zip enum
* #25 Implement ArrayUtil.shift(...)
* #24 Add ArrayUtil.subArray(...) overloads
* #22 Add BiObj<X>Function and BiObjBi<X>Function templates
* #21 Implement Functions utility with and(Function...) and or(Function...)
* #20 Make RetryFailureException a checked exception
* #19 Implement ConcurrentNullHashMap
* #18 Transition to GitHub Actions
* #17 StringPaths.getParent(path) should return null in case of bare scheme
* #16 Add Iterators.toEnumeration()

## [v0.8.1](https://github.com/libj/util/compare/a806523104ea3762033fc1a0de12daf9176ee5d0..d8bc651739ee7c7a945c8ffad2a1dc6c6c0af904) (2020-05-23)
* Add template generated rules for classes in `org.libj.util.function` and `org.libj.util.primitive` packages.
* Add `ArrayCharSequence`.
* Prevent `ArrayIndexOutOfBoundsException` in `Bytes`.
* Add `CursorListIterator`.
* Add `dropSeconds(long)` and `dropSeconds(Date` to `Dates`.
* Add `iso8601ToEpochMilli` and `epochMilliToIso8601` to `Dates`.
* Add `equals`, `hashCode`, `toString` to `DelegateIterator`
* Improve API for subclassing `Delegate*` and `Observable*` classes.
* Add `DelegateSpliterator`.
* Add `FlatArrayIterator`, `FlatCollectionIterator`, `FlatIterator`, `FlatIterableIterator`, `FlatListIterator`, `FlatSequentialIterator`, for `Iterators.flatIterator(Object[])` and `Iterators.flatIterator(List<Object>)`.
* Rename `Combinations` to `Groups`.
* Add `Groups.permute()`.
* Add `Iterables.singleton(Object)`.
* Add `Locales.fromRFC1766(String)`.
* Improve handling of `InvocationTargetException`.
* Add `SimpleDateFormats`.
* Add generic exception to `RetryPolicy`.
* Improve tests.
* Improve javadocs.

## [v0.8.0](https://github.com/libj/util/compare/9b9f4a830efeab9c423bac9275d91e08c1e14aaa..a806523104ea3762033fc1a0de12daf9176ee5d0) (2019-07-21)
* Add `ThrowingBiFunction`.
* Add `ThrowingBiPredicate`.
* Add `ThrowingRunnable`.
* Add `ThrowingSupplier`.
* Add `Strings#isLowerCase(CharSequence)`.
* Add `Strings#isUpperCase(CharSequence)`.
* Fix edge-cases in `Identifiers`.
* Upgrade `org.libj:logging:0.4.1` to `0.4.2`.
* Upgrade `org.libj:test:0.6.9` to `0.7.0`.

## [v0.7.9](https://github.com/entinae/pom/compare/dc070d21b42fa5a9059c3daf647d4e452c7744f3..9b9f4a830efeab9c423bac9275d91e08c1e14aaa) (2019-05-13)
* Initial public release.