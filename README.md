# WordToNumber-for-Kotlin

***

## Overview
WordToNumber for Kotlin is a conversion of Python's [word2number](https://pypi.org/project/word2number/) library. The module converts spelled out number words into numberic digits. For example the number eighty five becomes 85.

---

## Usage

*Library functionality is coming soon*

You can use the `wordToNumber` function to convert numbers.

`println(wordToNumber("Four")`
> Prints `4` to the console.

`println(wordToNumber("Eighteen")`
> Prints `18` to the console.

`println(wordToNumber("Three point one")`
> Prints `3.1` to the console.

`println(wordToNumber("Seventy six")`
> Prints `76` to the console.

`println(wordToNumber("Four hundred and two")`
> Prints `402` to the console.

`println(wordToNumber("Five million four thousand two hundred and fourteen")`
> Prints `5004214` to the console.

`println(wordToNumber("million million")`
> Prints `Redundant number word! Please enter a valid number word (eg. two million twenty three thousand and forty nine)` to the console.

`println(wordToNumber("Hello, World!")`
> Prints `No valid number words found! Please enter a valid number word (eg. two million twenty three thousand and forty nine)` to the console.
