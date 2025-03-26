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

val decimalWords = listOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

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
                    // If current value is already a hundreds value, add this as the last two digits
                    currentValue += value
                } else if(currentValue in 1L..99L){
                    // If current value is already a two-digit number, combine them
                    currentValue = currentValue * 10L + value
                } else{
                    // Otherwise, set as the current value
                    currentValue = value
                }
            }
            else -> return 0L
        }
    }
    totalValue += currentValue  // Correct line
    return totalValue
}

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

fun calculateSum(cleanNumbers: List<String>, billionIndex: Int, millionIndex: Int, thousandIndex: Int, cleanDecimalNumbers: List<String>): Long {
    if (cleanNumbers.isEmpty()) return 0

    var totalSum: Long = 0

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
    if(cleanDecimalNumbers.isNotEmpty()){
        val decimalPart = getDecimalSum(cleanDecimalNumbers) ?: 0.0
        totalSum += decimalPart.toLong()
    }

    return totalSum
}

fun wordToNum(numberSentence: String): Long {
    val numberSentence = numberSentence.replace('-', ' ').lowercase()

    if(numberSentence.toIntOrNull() != null){
        return numberSentence.toInt().toLong()
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

    return calculateSum(cleanNumbers, billionIndex, millionIndex, thousandIndex, cleanDecimalNumbers)
}