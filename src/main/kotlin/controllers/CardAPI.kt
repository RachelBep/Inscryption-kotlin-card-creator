package ie.setu.controllers

import ie.setu.models.Card
import ie.setu.models.Sigil
import persistence.Serializer
import utils.isValidListIndex
import java.util.*
import kotlin.collections.ArrayList

class CardAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType
    private var cards = ArrayList<Card>()
    fun add(card: Card): Boolean {
        return cards.add(card)
    }

    fun listAllCards(): String =
        if (cards.isEmpty()) {
            "No cards stored"
        } else {
            formatListString(cards)
        }


    fun findCard(index: Int): Card? {
        return if (isValidListIndex(index, cards)) {
            cards[index]
        } else {
            null
        }
    }

    fun deleteCard(indexToDelete: Int): Card? {
        return if (isValidListIndex(indexToDelete, cards)) {
            cards.removeAt(indexToDelete)
        } else {
            null
        }
    }

    fun updateCardSigil(indexToUpdate: Int, sigils: ArrayList<Sigil?> = ArrayList()): Boolean {
        // find the card object by the index number
        val foundCard = findCard(indexToUpdate)

        // if the card exists, use the card details passed as parameters to update the found card in the ArrayList.
        if (foundCard != null) {
            foundCard.sigils = sigils
            return true
        }

        // if the card was not found, return false, indicating that the update was not successful
        return false
    }

    fun searchBySigil(searchString: String): String {
        val filteredCards = ArrayList<Card>()
        for (cardFor: Card in cards) {
            for(sigil: Sigil? in cardFor.sigils) {
                if (sigil != null) {
                    if (sigil.sigilName.lowercase(Locale.getDefault()).contains(searchString.lowercase(Locale.getDefault()))) {
                        filteredCards.add(cardFor)
                        break
                    }
                }
            }
        }
        return formatListString(filteredCards)
    }


    fun updateCardNormal(indexToUpdate: Int, cardName: String, hp: Int, dmg: Int, tribe: String, cost: Int, costType: String, moxCost: ArrayList<Boolean> = ArrayList()): Boolean {
        // find the card object by the index number
        val foundCard = findCard(indexToUpdate)

        // if the card exists, use the card details passed as parameters to update the found card in the ArrayList.
        if ((foundCard != null)) {
            foundCard.cardName = cardName
            foundCard.hp = hp
            foundCard.dmg = dmg
            foundCard.tribe = tribe
            foundCard.cost = cost
            foundCard.costType = costType
            foundCard.moxCost = moxCost
            return true
        }

        // if the card was not found, return false, indicating that the update was not successful
        return false
    }


    fun numberOfCards() = cards.size

    fun isValidIndex(index: Int): Boolean {
        return isValidListIndex(index, cards)
    }

    @Throws(Exception::class)
    fun load() {
        cards = serializer.read() as ArrayList<Card>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(cards)
    }

    private fun formatListString(cardsToFormat: List<Card>): String =
        cardsToFormat
            .joinToString(separator = "\n") { card ->
                cards.indexOf(card).toString() + ": " + card.toString()
            }
}