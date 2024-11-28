package ie.setu


import ie.setu.controllers.SigilAPI
import ie.setu.models.Sigil
import utils.*
import kotlin.system.exitProcess

fun main() {
    mainMenu()
}

private val sigilAPI = SigilAPI()



fun mainMenu() {
    do {
        when (val option = mainMenuDisplay()) {
            1 -> addSigil()
            //2 -> listSigils()
            //3 ->  updateSigil()
            //4 -> deleteSigil()
            //5 -> searchSigil()

            //6 -> addCard()
            //7 -> listCards()
            //8 ->  updateCard()
            //9 -> deleteCard()
            //10 -> searchCard()

            //11 -> save()
            //12 -> load()

            0 -> exitApp()
            else -> println("Invalid option entered: $option")
        }
    } while (true)
}

fun exitApp() {
    println("Exiting...bye")
    exitProcess(0)
}

fun mainMenuDisplay(): Int {
    print(
        """ 
         > ----------------------------------
         > |      Inscryption Card App      |
         > ----------------------------------
         > | Sigil MENU                     |
         > |   1) Add a Sigil               |
         > |   2) List all sigils           |
         > |   3) Update a sigil            |
         > |   4) Delete a sigil            |
         > |   5) Search sigil (by desc)    |
         > ----------------------------------
         > | Card MENU                      |
         > |   6) Add a Card                |
         > |   7) List all Cards            |
         > |   8) Update a Card             |
         > |   9) Delete a Card             |
         > |   10) Search card (by sigil)   |
         > ----------------------------------
         > |   11) Save                     |
         > |   12) Load                     |
         > ----------------------------------
         > |   0) Exit                      |
         > ---------------------------------- 
         >""".trimMargin(">")
    )
    return readNextInt(" > ==>>")


}

fun addSigil() {
    val sigilName = readNextLine("Enter a name for the sigil:")
    val sigilDescription = readNextLine("Enter a description of the sigil")
    println("Enter the Sigil's power level")
    val power = readIntNotNull()

    val isAdded = sigilAPI.add(Sigil(sigilName, sigilDescription, power))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}



