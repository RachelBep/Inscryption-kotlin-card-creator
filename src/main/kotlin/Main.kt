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
            2 -> listSigils()
            3 ->  updateSigil()
            4 -> deleteSigil()
            5 -> searchSigils()

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

fun listSigils() {
    println(sigilAPI.listAllSigils())
}


fun deleteSigil() {
    listSigils()
    if (sigilAPI.numberOfSigils() > 0) {
        // only ask the user to choose the sigil to delete if sigils exist
        val indexToDelete = readNextInt("Enter the index of the sigil to delete: ")
        // pass the index of the sigil to SigilAPI for deleting and check for success.
        val sigilToDelete = sigilAPI.deleteSigil(indexToDelete)
        if (sigilToDelete != null) {
            println("Delete Successful! Deleted sigil: ${sigilToDelete.sigilName}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun updateSigil() {
    listSigils()
    if (sigilAPI.numberOfSigils() > 0) {
        // only ask the user to choose the sigil if sigils exist
        val indexToUpdate = readNextInt("Enter the index of the sigil to update: ")
        if (sigilAPI.isValidIndex(indexToUpdate)) {
            val sigilName = readNextLine("Enter a name for the sigil:")1
            val sigilDescription = readNextLine("Enter a description of the sigil")
            println("Enter the Sigil's power level")
            val power = readIntNotNull()

            // pass the index of the sigil and the new sigil details to SigilAPI for updating and check for success.
            if (sigilAPI.updateSigil(indexToUpdate, Sigil(sigilName, sigilDescription, power))) {
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no sigils for this index number")
        }
    }
}

fun searchSigils() {
    val searchTitle = readNextLine("Enter the description to search by: ")
    val searchResults = sigilAPI.searchByDescription(searchTitle)
    if (searchResults.isEmpty()) {
        println("No sigils found")
    } else {
        println(searchResults)
    }
}



