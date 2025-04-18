# WordToNumber-for-Kotlin

***

## Overview
WordToNumber for Kotlin is a conversion of Python's [word2number](https://pypi.org/project/word2number/) library. The module converts spelled out number words into numberic digits. For example the number eighty five becomes 85.

---

## Usage

### Maven

Add the following to your ```pom.xml``` file,
```
<dependency>
  <groupId>com.Tomasz0720</groupId>
  <artifactId>wordtonumberforkotlin</artifactId>
  <version>1.1.1</version>
</dependency>
```

Then run,
```
mvn install
```

## Examples

Use the `wordToNumber` function,

```kotlin
println(wordToNumber("Four"))
// Output: 4

println(wordToNumber("Eighteen"))
// Output: 18

println(wordToNumber("Three point one"))
// Output: 3.1

println(wordToNumber("Seventy six"))
// Output: 76

println(wordToNumber("Four hundred and two"))
// Output: 402

println(wordToNumber("Five million four thousand two hundred and fourteen"))
// Output: 5004214

println(wordToNumber("million million"))
// Output: Redundant number word! Please enter a valid number word (e.g., two million twenty three thousand and forty nine)

println(wordToNumber("Hello, World!"))
// Output: No valid number words found! Please enter a valid number word (e.g., two million twenty three thousand and forty nine)
```

## Problems

If you find a bug or something doesn't work as expected,
- Open an issue on [Github](https://github.com/Tomasz0720/WordToNumberForKotlin.git)
- Or email me directly at [tomaszapuzio@gmail.com](mailto:tomaszapuzio@gmail.com)

## Contributers

Tomasz Puzio [(Tomasz0720)](https://github.com/Tomasz0720)

