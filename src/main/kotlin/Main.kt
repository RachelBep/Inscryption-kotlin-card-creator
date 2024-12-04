package ie.setu


import ie.setu.controllers.CardAPI
import ie.setu.controllers.SigilAPI
import ie.setu.models.Card
import ie.setu.models.Sigil
import utils.*
import kotlin.system.exitProcess

fun main() {
    mainMenu()
}

private val sigilAPI = SigilAPI()
private val cardAPI = CardAPI()



fun mainMenu() {
    do {
        when (val option = mainMenuDisplay()) {
            1 -> addSigil()
            2 -> listSigils()
            3 ->  updateSigil()
            4 -> deleteSigil()
            5 -> searchSigils()

            6 -> addCard()
            7 -> listCards()
            8 ->  updateCard()
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

fun addCard() {
    val cardName = readNextLine("Enter a name for the card:")
    val hp = readNextInt("how much hp does this card have:")
    val dmg = readNextInt("how much damage does this card do:")
    val tribe = readNextLine("What tribe is this card in:")
    val costType = readNextLine("What type of cost does this card use:")
    var cost = 0
    var moxCost: ArrayList<Boolean> = ArrayList<Boolean>()
    if (costType == "Mox") {
        val redCost = readNextLine("Type 1 if this card uses Red Mox, otherwise type anything else:")
        val greenCost = readNextLine("Type 1 if this card uses Green Mox, otherwise type anything else:")
        val blueCost = readNextLine("Type 1 if this card uses Blue Mox, otherwise type anything else:")
        moxCost = arrayListOf(redCost.equals("1"), greenCost.equals("1"), blueCost.equals("1",))
    } else {
        cost = readNextInt("what is this cards cost:")
        moxCost = arrayListOf(false, false, false)
    }
    var sigils: ArrayList<Sigil?> = ArrayList<Sigil?>()
    if(sigilAPI.numberOfSigils() != 0) {
        var sigilCount = readNextInt("how many sigils does this card have")
        var i = 0
        while (i < sigilCount) {
            listSigils()
            val indexToAdd = readNextInt("Enter the index of a sigil to add: ")
            if (sigilAPI.findSigil(indexToAdd) != null) {
                sigils.add(sigilAPI.findSigil(indexToAdd)!!)
            } else {
                println("invalid index please try again")
                sigilCount += 1
            }
            sigilAPI.findSigil(indexToAdd)?.let { sigils.add(it) }
            i++
        }

    }



    val isAdded = cardAPI.add(Card(cardName, hp, dmg, tribe, cost, costType, sigils, moxCost))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}
fun listCards() {
    println(cardAPI.listAllCards())
}

fun updateCard() {
    listCards()
    if (cardAPI.numberOfCards() > 0) {
        // only ask the user to choose the note if cards exist
        val indexToUpdate = readNextInt("Enter the index of the card to update: ")
        if (cardAPI.isValidIndex(indexToUpdate)) {
            val whatUpdate = readNextLine("Type 1 if you want to update sigils, otherwise type anything else")
            if (whatUpdate == "1") { //sigil updating
                var sigils: ArrayList<Sigil?> = ArrayList<Sigil?>()
                if(sigilAPI.numberOfSigils() != 0) {
                    var sigilCount = readNextInt("how many sigils does this card have")
                    var i = 0
                    while (i < sigilCount) {
                        listSigils()
                        val indexToAdd = readNextInt("Enter the index of a sigil to add: ")
                        if (sigilAPI.findSigil(indexToAdd) != null) {
                            sigils.add(sigilAPI.findSigil(indexToAdd)!!)
                        } else {
                            println("invalid index please try again")
                            sigilCount += 1
                        }
                        sigilAPI.findSigil(indexToAdd)?.let { sigils.add(it) }
                        i++
                    }
                    if (cardAPI.updateCardSigil(indexToUpdate, sigils)) {
                        println("Update Successful")
                    } else {
                        println("Update Failed")
                    }
                } else {
                    println("No sigils found")
                }
            } else { //non-sigil updating
                val cardName = readNextLine("Enter a name for the card:")
                val hp = readNextInt("how much hp does this card have:")
                val dmg = readNextInt("how much damage does this card do:")
                val tribe = readNextLine("What tribe is this card in:")
                val costType = readNextLine("What type of cost does this card use:")
                var cost = 0
                var moxCost: ArrayList<Boolean> = ArrayList<Boolean>()
                if (costType == "Mox") {
                    val redCost = readNextLine("Type 1 if this card uses Red Mox, otherwise type anything else:")
                    val greenCost = readNextLine("Type 1 if this card uses Green Mox, otherwise type anything else:")
                    val blueCost = readNextLine("Type 1 if this card uses Blue Mox, otherwise type anything else:")
                    moxCost = arrayListOf(redCost.equals("1"), greenCost.equals("1"), blueCost.equals("1",))
                } else {
                    cost = readNextInt("what is this cards cost:")
                    moxCost = arrayListOf(false, false, false)
                }
                if (cardAPI.updateCardNormal(indexToUpdate, cardName, hp, dmg, tribe,  cost, costType, moxCost)) {
                    println("Update Successful")
                } else {
                    println("Update Failed")
                }
            }

        } else {
            println("There are no notes for this index number")
        }
    }
}

fun addSigil() {
    val sigilName = readNextLine("Enter a name for the sigil:")
    val sigilDescription = readNextLine("Enter a description of the sigil")
    val power = readNextInt("Enter the Sigil's power level")

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
            val sigilName = readNextLine("Enter a name for the sigil:")
            val sigilDescription = readNextLine("Enter a description of the sigil")
            println("Enter the Sigil's power level")
            val power = readNextInt("Enter the Sigil's power level")

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



