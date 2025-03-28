/**
 * A map that associates English number words with their numerical values.
 * Used for converting word-based numbers into numeric representations.
 */
val americanNumberSystem = mutableMapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9,
    "ten" to 10,
    "eleven" to 11,
    "twelve" to 12,
    "thirteen" to 13,
    "fourteen" to 14,
    "fifteen" to 15,
    "sixteen" to 16,
    "seventeen" to 17,
    "eighteen" to 18,
    "nineteen" to 19,
    "twenty" to 20,
    "thirty" to 30,
    "forty" to 40,
    "fifty" to 50,
    "sixty" to 60,
    "seventy" to 70,
    "eighty" to 80,
    "ninety" to 90,
    "hundred" to 100,
    "thousand" to 1000,
    "million" to 1000000,
    "billion" to 1000000000,
    "point" to '.',
    "and" to -1
)

/** List of words representing decimal digits (0-9). */
val decimalWords = listOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

/**
 * Converts a list of number words into its corresponding numeric value.
 *
 * @param numberWords A list of words representing a number (e.g., ["two", "hundred", "thirty"]).
 * @return The numerical value represented by the words.
 */
fun numberFormation(numberWords: List<String>): Long{
    if(numberWords.isEmpty()){
        return 0L
    }

    var totalValue = 0L
    var currentValue = 0L

    for(word in numberWords){

        if(word == "and"){
            continue
        }

        val value = when (val numberValue = americanNumberSystem[word]){
            is Int -> numberValue.toLong()
            is Long -> numberValue
            else -> return 0L
        }

        when{
            value == 100L ->{
                if (currentValue == 0L) return 0L
                currentValue *= 100L
            }
            value == 1000L || value == 1_000_000L || value == 1_000_000_000L ->{
                if(currentValue == 0L){
                    return 0L
                }
                totalValue += currentValue * value
                currentValue = 0L
            }
            value in 1L..99L ->{
                if(currentValue >= 100L){
                    currentValue += value
                } else if(currentValue in 1L..99L){
                    currentValue = currentValue + value  // Not *10L, just add
                } else{
                    currentValue = value
                }
            }
            else -> return 0L
        }
    }
    totalValue += currentValue  // Correct line
    return totalValue
}

/**
 * Converts a list of decimal digit words into a decimal number.
 *
 * @param decimalDigitWord A list of words representing decimal digits (e.g., ["one", "two", "five"] for 0.125).
 * @return The decimal representation or `null` if the input is invalid.
 */
fun getDecimalSum(decimalDigitWord: List<String>): Double? {
    val decimalNumberStr = StringBuilder("0.")

    for(decimalWord in decimalDigitWord){
        val numValue = americanNumberSystem[decimalWord] as? Int
        if(numValue == null || decimalWord !in decimalWords){
            return null
        }
        decimalNumberStr.append(numValue)
    }
    return decimalNumberStr.toString().toDoubleOrNull()
}

/**
 * Computes the numeric sum of word-based numbers, considering billion, million, and thousand separations.
 *
 * @param cleanNumbers A list of words representing a whole number.
 * @param billionIndex Index of "billion" in the list, or -1 if not present.
 * @param millionIndex Index of "million" in the list, or -1 if not present.
 * @param thousandIndex Index of "thousand" in the list, or -1 if not present.
 * @param cleanDecimalNumbers A list of words representing the decimal portion.
 * @return The computed numeric value as a `Double`.
 */
fun calculateSum(cleanNumbers: List<String>, billionIndex: Int, millionIndex: Int, thousandIndex: Int, cleanDecimalNumbers: List<String>): Double {
    if (cleanNumbers.isEmpty()){ // Change to 0.0
        return 0.0
    }

    var totalSum: Double = 0.0  // Change to Double

    // Handle billion
    if(billionIndex > -1){
        val billionPart = cleanNumbers.subList(0, billionIndex)
        totalSum += (numberFormation(billionPart) ?: 0) * 1_000_000_000
    }

    // Handle million
    if(millionIndex > -1){
        val millionStartIndex = if (billionIndex > -1) billionIndex + 1 else 0
        val millionPart = cleanNumbers.subList(millionStartIndex, millionIndex)
        totalSum += (numberFormation(millionPart) ?: 0) * 1_000_000
    }

    // Handle thousand
    if(thousandIndex > -1){
        val thousandStartIndex = when{
            millionIndex > -1 -> millionIndex + 1
            billionIndex > -1 -> billionIndex + 1
            else -> 0
        }
        val thousandPart = cleanNumbers.subList(thousandStartIndex, thousandIndex)
        totalSum += (numberFormation(thousandPart) ?: 0) * 1_000
    }

    // Handle the rest (hundreds and below)
    val remainingStartIndex = when{
        thousandIndex > -1 -> thousandIndex + 1
        millionIndex > -1 -> millionIndex + 1
        billionIndex > -1 -> billionIndex + 1
        else -> 0
    }

    val remainingPart = cleanNumbers.subList(remainingStartIndex, cleanNumbers.size)
    totalSum += numberFormation(remainingPart) ?: 0

    // Handle decimal numbers if present
    // Handle decimal numbers if present
    if(cleanDecimalNumbers.isNotEmpty()){
        val decimalPart = getDecimalSum(cleanDecimalNumbers) ?: 0.0
        return totalSum.toDouble() + decimalPart  // Return a double instead of adding as long
    }

    return totalSum
}

/**
 * Converts a word-based number representation into a numerical value.
 *
 * @param numberSentence A string containing a number in words (e.g., "two million twenty-three thousand and forty-nine").
 * @return The corresponding numerical value, either as `Int` or `Double` if a decimal part is present.
 * @throws IllegalArgumentException If the input is invalid or malformed.
 */
fun wordToNum(numberSentence: String): Number {
    val numberSentence = numberSentence.replace('-', ' ').lowercase()

    // If it's a pure numeric string, return as whole number or decimal
    val numericParse = numberSentence.toDoubleOrNull()
    if (numericParse != null) {
        return if (numericParse.toInt().toDouble() == numericParse) numericParse.toInt() else numericParse
    }

    val splitWords = numberSentence.split(" ")
    val cleanNumbers = mutableListOf<String>()
    val cleanDecimalNumbers = mutableListOf<String>()

    for(word in splitWords){
        if(word in americanNumberSystem){
            cleanNumbers.add(word)
        } else if(word in decimalWords){
            cleanDecimalNumbers.add(word)
        }
    }

    if(cleanNumbers.isEmpty()){
        throw IllegalArgumentException("No valid number words found! Please enter a valid number word (eg. two million twenty three thousand and forty nine)")
    }

    if(cleanNumbers.count { it == "thousand" } > 1 || cleanNumbers.count { it == "million" } > 1 || cleanNumbers.count { it == "billion" } > 1 || cleanNumbers.count { it == "point" } > 1){
        throw IllegalArgumentException("Redundant number word! Please enter a valid number word (eg. two million twenty three thousand and forty nine)")
    }

    // Handle point case
    if(cleanNumbers.count { it == "point" } == 1){
        val pointIndex = cleanNumbers.indexOf("point")
        cleanDecimalNumbers.addAll(cleanNumbers.subList(pointIndex + 1, cleanNumbers.size))
        cleanNumbers.subList(pointIndex, cleanNumbers.size).clear()
    }

    // Ensure the order is correct
    val billionIndex = cleanNumbers.indexOf("billion")
    val millionIndex = cleanNumbers.indexOf("million")
    val thousandIndex = cleanNumbers.indexOf("thousand")

    if(thousandIndex > -1 && (thousandIndex < millionIndex || thousandIndex < billionIndex) || (millionIndex > -1 && millionIndex < billionIndex)){
        throw IllegalArgumentException("Malformed number!")
    }

    // If no decimal part, return as whole number
    if(cleanDecimalNumbers.isEmpty()){
        return calculateSum(cleanNumbers, billionIndex, millionIndex, thousandIndex, cleanDecimalNumbers).toInt()
    }

    // If decimal part exists, combine whole and decimal parts
    val wholePart = calculateSum(cleanNumbers, billionIndex, millionIndex, thousandIndex, listOf())
    val decimalPart = getDecimalSum(cleanDecimalNumbers) ?: 0.0
    return wholePart + decimalPart
}