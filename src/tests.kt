fun main(){
    println(wordToNum("Eighteen"))
    println(wordToNum("Four"))
    println(wordToNum("Eighty five"))
    println(wordToNum("Four hundred and two"))

    println("STRESS TESTS:")

    println(wordToNum("Three point one"))
    println(wordToNum("Five million four thousand two hundred and fourteen"))
    println(wordToNum("six hundred twenty five"))
    println(wordToNum("million million")) //should break after this frfr
    println(wordToNum("Hello, World!"))
}