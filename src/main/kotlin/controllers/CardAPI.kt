package ie.setu.controllers

import ie.setu.models.Card
import ie.setu.models.Sigil
import persistence.Serializer
import utils.isValidListIndex
import java.util.*
import kotlin.collections.ArrayList

/**
 * Controller class for managing a collection of cards.
 * Provides functionality for adding, updating, deleting, and searching cards.
 *
 * @param serializerType the serializer used to persist and load card data
 */
class CardAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType
    private var cards = ArrayList<Card>()

    /**
     * Adds a new card to the collection.
     *
     * @param card the card to be added
     * @return true if the card was successfully added, false otherwise
     */
    fun add(card: Card): Boolean {
        return cards.add(card)
    }


    /**
     * Lists all cards in the collection.
     *
     * @return a formatted string containing all cards, or a message if no cards are stored
     */
    fun listAllCards(): String =
        if (cards.isEmpty()) {
            "No cards stored"
        } else {
            formatListString(cards)
        }

    /**
     * Finds a card by its index.
     *
     * @param index the index of the card to find
     * @return the card at the specified index, or null if the index is invalid
     */
    fun findCard(index: Int): Card? {
        return if (isValidListIndex(index, cards)) {
            cards[index]
        } else {
            null
        }
    }
    /**
     * Deletes a card by its index.
     *
     * @param indexToDelete the index of the card to delete
     * @return the deleted card, or null if the index is invalid
     */
    fun deleteCard(indexToDelete: Int): Card? {
        return if (isValidListIndex(indexToDelete, cards)) {
            cards.removeAt(indexToDelete)
        } else {
            null
        }
    }
    /**
     * Updates the sigils of a card.
     *
     * @param indexToUpdate the index of the card to update
     * @param sigils the new sigils to assign to the card
     * @return true if the update was successful, false otherwise
     */
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
    /**
     * Searches for cards containing a specific sigil.
     *
     * @param searchString the sigil name to search for
     * @return a formatted string containing the matching cards, or an empty result if none match
     */
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


    /**
     * Updates the main attributes of a card.
     *
     * @param indexToUpdate the index of the card to update
     * @param cardName the new name of the card
     * @param hp the new health points of the card
     * @param dmg the new damage points of the card
     * @param tribe the new tribe of the card
     * @param cost the new cost of the card
     * @param costType the new type of cost for the card
     * @param moxCost the new mox cost for the card
     * @return true if the update was successful, false otherwise
     */
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

    /**
     * Gets the total number of cards in the collection.
     *
     * @return the size of the card collection
     */
    fun numberOfCards() = cards.size
    /**
     * Checks if the provided index is valid for the card collection.
     *
     * @param index the index to check
     * @return true if the index is valid, false otherwise
     */
    fun isValidIndex(index: Int): Boolean {
        return isValidListIndex(index, cards)
    }
    /**
     * Loads the card data from persistent storage.
     *
     * @throws Exception if an error occurs during loading
     */
    @Throws(Exception::class)
    fun load() {
        cards = serializer.read() as ArrayList<Card>
    }
    /**
     * Stores the card data to persistent storage.
     *
     * @throws Exception if an error occurs during saving
     */
    @Throws(Exception::class)
    fun store() {
        serializer.write(cards)
    }
    /**
     * Formats a list of cards into a readable string.
     *
     * @param cardsToFormat the list of cards to format
     * @return a formatted string with card details
     */
    private fun formatListString(cardsToFormat: List<Card>): String =
        cardsToFormat
            .joinToString(separator = "\n") { card ->
                cards.indexOf(card).toString() + ": " + card.toString()
            }
}