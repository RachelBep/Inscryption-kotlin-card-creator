package ie.setu

import utils.readNextInt

fun main() {
    mainMenu()
}


fun mainMenu() {
    do {
        when (val option = mainMenuDisplay()) {
            //1 -> addSigil()
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


            else -> println("Invalid option entered: $option")
        }
    } while (true)
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
