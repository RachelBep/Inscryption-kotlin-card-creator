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


    fun numberOfCards() = cards.size

    private fun formatListString(cardsToFormat: List<Card>): String =
        cardsToFormat
            .joinToString(separator = "\n") { card ->
                cards.indexOf(card).toString() + ": " + card.toString()
            }
}