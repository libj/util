# Changes by Version

## [v0.8.1](https://github.com/libj/util/compare/a806523104ea3762033fc1a0de12daf9176ee5d0..HEAD) (2020-05-23)
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

## v0.7.9 (2019-05-13)
* Initial public release.