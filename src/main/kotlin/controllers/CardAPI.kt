package ie.setu.controllers

import ie.setu.models.Card
import ie.setu.models.Sigil
import utils.isValidListIndex

class CardAPI {
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

    fun updateCardSigil(indexToUpdate: Int, sigils: ArrayList<Sigil?> = ArrayList<Sigil?>()): Boolean {
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

    fun updateCardNormal(indexToUpdate: Int, cardName: String, hp: Int, dmg: Int, tribe: String, cost: Int, costType: String, moxCost: ArrayList<Boolean> = ArrayList<Boolean>()): Boolean {
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

    private fun formatListString(cardsToFormat: List<Card>): String =
        cardsToFormat
            .joinToString(separator = "\n") { card ->
                cards.indexOf(card).toString() + ": " + card.toString()
            }
}