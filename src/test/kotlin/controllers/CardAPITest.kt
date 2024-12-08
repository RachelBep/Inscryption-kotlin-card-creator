package controllers

import ie.setu.controllers.CardAPI
import ie.setu.models.Card
import ie.setu.models.Sigil
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File

class CardAPITest {
    private var fecundity: Sigil? = null
    private var undying: Sigil? = null
    private var regenerating: Sigil? = null
    private var ourobouros: Card? = null
    private var rainbow: Card? = null
    private var squirrel: Card? = null
    private var populatedCards: CardAPI? = CardAPI(XMLSerializer(File("cardTest.xml")))
    private var emptyCards: CardAPI? = CardAPI(XMLSerializer(File("emptyCards.xml")))
    @BeforeEach
    fun setup() {
        fecundity = Sigil("Fecundity", "adds a copy of this card to your hand when played", 6)
        undying = Sigil("Undying", "when this card perishes put it in your hand instead", 5)
        regenerating = Sigil("Regenerating", "regains 1 life each turn", 3)
        ourobouros =
            Card("Ouroborous", 1, 1, "none", 2, "Blood",
                arrayListOf(undying, fecundity), arrayListOf(false, false, false))
        rainbow =
            Card("Rainbow Wizard", 2, 4, "Wizard", 0, "Mox",
                arrayListOf(regenerating), arrayListOf(true, true, true))
        squirrel = Card("Squirrel", 1, 0, "Squirrel", 0, "", arrayListOf(), arrayListOf(false, false, false))

        // adding 3 Card to the cards api
        populatedCards!!.add(ourobouros!!)
        populatedCards!!.add(rainbow!!)
        populatedCards!!.add(squirrel!!)

    }

    @AfterEach
    fun tearDown() {
        fecundity = null
        undying = null
        regenerating = null
        ourobouros = null
        squirrel = null
        populatedCards = null
        emptyCards = null
    }




    @Nested
    inner class AddCards {

        @Test
        fun `adding a Card to a populated list adds to ArrayList`() {
            val newCard = Card("Isaac", 2, 1, "zombie", 4, "Bones", arrayListOf(), arrayListOf(false, false, false))
            assertEquals(3, populatedCards!!.numberOfCards())
            assertTrue(populatedCards!!.add(newCard))
            assertEquals(4, populatedCards!!.numberOfCards())
            assertEquals(newCard, populatedCards!!.findCard(populatedCards!!.numberOfCards() - 1))
        }

        @Test
        fun `adding a Card to an empty list adds to ArrayList`() {
            val newCard = Card("Isaac", 2, 1, "zombie", 4, "Bones", arrayListOf(), arrayListOf(false, false, false))
            assertEquals(0, emptyCards!!.numberOfCards())
            assertTrue(emptyCards!!.add(newCard))
            assertEquals(1, emptyCards!!.numberOfCards())
            assertEquals(newCard, emptyCards!!.findCard(emptyCards!!.numberOfCards() - 1))
        }


    }


    @Nested
    inner class ListCards {

        @Test
        fun `listAllCards returns No Cards Stored message when ArrayList is empty`() {
            assertEquals(0, emptyCards!!.numberOfCards())
            assertTrue(emptyCards!!.listAllCards().lowercase().contains("no cards"))
        }

        @Test
        fun `listAllCards returns Cards when ArrayList has cards stored`() {
            assertEquals(3, populatedCards!!.numberOfCards())
            val cardsString = populatedCards!!.listAllCards().lowercase()
            assertTrue(cardsString.contains("squirrel"))
            assertTrue(cardsString.contains("mox"))
            assertTrue(cardsString.contains("wizard"))
        }


    }

    @Nested
    inner class UpdateCards {
        @Test
        fun `updating a card that does not exist returns false`() {
            assertFalse(populatedCards!!.updateCardNormal(6, "test", 5, 7, "tribe", 6, "Bones", arrayListOf(false, false, false)))
            assertFalse(populatedCards!!.updateCardNormal(-1,  "test", 5, 7, "tribe", 6, "Bones", arrayListOf(false, false, false)))
            assertFalse(emptyCards!!.updateCardNormal(0, "test", 5, 7, "tribe", 6, "Bones", arrayListOf(false, false, false)))
            assertFalse(populatedCards!!.updateCardSigil(6, arrayListOf(undying)))
            assertFalse(populatedCards!!.updateCardSigil(-1,  arrayListOf(undying)))
            assertFalse(emptyCards!!.updateCardSigil(0, arrayListOf(undying)))
        }

        @Test
        fun `updating a card with updateCardNormal that exists returns true and updates`() {
            // check card 5 exists and check the contents
            assertEquals(squirrel, populatedCards!!.findCard(2))
            assertEquals("Squirrel", populatedCards!!.findCard(2)!!.cardName)
            assertEquals(1, populatedCards!!.findCard(2)!!.hp)
            assertEquals(0, populatedCards!!.findCard(2)!!.dmg)
            assertEquals("Squirrel", populatedCards!!.findCard(2)!!.tribe)
            assertEquals(0, populatedCards!!.findCard(2)!!.cost)
            assertEquals("", populatedCards!!.findCard(2)!!.costType)
            assertEquals(arrayListOf(false, false, false), populatedCards!!.findCard(2)!!.moxCost)

            // update card 5 with new information and ensure contents updated successfully
            assertTrue(populatedCards!!.updateCardNormal(2, "Evil", 6, 7, "Goo", 4, "Bones", arrayListOf(true, true, false)))
            assertEquals("Evil", populatedCards!!.findCard(2)!!.cardName)
            assertEquals(6, populatedCards!!.findCard(2)!!.hp)
            assertEquals(7, populatedCards!!.findCard(2)!!.dmg)
            assertEquals("Goo", populatedCards!!.findCard(2)!!.tribe)
            assertEquals(4, populatedCards!!.findCard(2)!!.cost)
            assertEquals("Bones", populatedCards!!.findCard(2)!!.costType)
            assertEquals(arrayListOf(true, true, false), populatedCards!!.findCard(2)!!.moxCost)
        }

        @Test
        fun `updating a card with updateCardSigil that exists returns true and updates`() {
            // check card 5 exists and check the contents
            assertEquals(squirrel, populatedCards!!.findCard(2))
            val sigilsBlank: ArrayList<Sigil?> = ArrayList()
            assertEquals(sigilsBlank, populatedCards!!.findCard(2)!!.sigils)

            // update card 5 with new information and ensure contents updated successfully
            assertTrue(populatedCards!!.updateCardSigil(2, arrayListOf(undying)))
            assertEquals(arrayListOf(undying), populatedCards!!.findCard(2)!!.sigils)

        }
    }

    @Nested
    inner class DeleteCards {

        @Test
        fun `deleting a Card that does not exist, returns null`() {
            assertNull(emptyCards!!.deleteCard(0))
            assertNull(populatedCards!!.deleteCard(-1))
            assertNull(populatedCards!!.deleteCard(5))
        }

        @Test
        fun `deleting a card that exists delete and returns deleted object`() {
            assertEquals(3, populatedCards!!.numberOfCards())
            assertEquals(rainbow, populatedCards!!.deleteCard(1))
            assertEquals(2, populatedCards!!.numberOfCards())
            assertEquals(ourobouros, populatedCards!!.deleteCard(0))
            assertEquals(1, populatedCards!!.numberOfCards())
        }
    }


    @Nested
    inner class SearchMethods {
        @Test
        fun `search cards by sigil returns no cards when no cards with that sigil exist`() {
            // Searching a populated collection for a sigil that doesn't exist.
            assertEquals(3, populatedCards!!.numberOfCards())
            val searchResults = populatedCards!!.searchBySigil("no results expected")
            assertTrue(searchResults.isEmpty())
            // Searching an empty collection
            assertEquals(0, emptyCards!!.numberOfCards())
            assertTrue(emptyCards!!.searchBySigil("").isEmpty())
        }

        @Test
        fun `search cards by sigils returns cards when card with that sigil exist`() {
            assertEquals(3, populatedCards!!.numberOfCards())
            // Searching a populated collection for a full sigil name that exists (case matches exactly)
            var searchResults = populatedCards!!.searchBySigil("Undying")
            assertTrue(searchResults.contains("Undying"))
            assertFalse(searchResults.contains("Regenerating"))
            // Searching a populated collection for a partial name that exists (case matches exactly)
            searchResults = populatedCards!!.searchBySigil("y")
            assertTrue(searchResults.contains("Undying"))
            assertTrue(searchResults.contains("Fecundity"))
            assertFalse(searchResults.contains("Regenerating"))
            // Searching a populated collection for a partial name that exists (case doesn't match)
            searchResults = populatedCards!!.searchBySigil("uN")
            assertTrue(searchResults.contains("Undying"))
            assertTrue(searchResults.contains("Fecundity"))
            assertFalse(searchResults.contains("Regenerating"))
        }
    }


    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty cardTest.XML file.
            val storingCard = CardAPI(XMLSerializer(File("cardTest.xml")))
            storingCard.store()

            // Loading the empty cardTest.xml file into a new object
            val loadedCard = CardAPI(XMLSerializer(File("cardTest.xml")))
            loadedCard.load()

            // Comparing the source of the cards (storingCard) with the XML loaded cards (loadedCard)
            assertEquals(0, storingCard.numberOfCards())
            assertEquals(0, loadedCard.numberOfCards())
            assertEquals(storingCard.numberOfCards(), loadedCard.numberOfCards())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 cards to the cardTest.XML file.
            val storingCard = CardAPI(XMLSerializer(File("cardTest.xml")))
            storingCard.add(rainbow!!)
            storingCard.add(squirrel!!)
            storingCard.add(ourobouros!!)
            storingCard.store()

            // Loading cardTest.xml into a different collection
            val loadedCard = CardAPI(XMLSerializer(File("cardTest.xml")))
            loadedCard.load()

            // Comparing the source of the cards (storingCard) with the XML loaded cards (loadedCard)
            assertEquals(3, storingCard.numberOfCards())
            assertEquals(3, loadedCard.numberOfCards())
            assertEquals(storingCard.numberOfCards(), loadedCard.numberOfCards())
            assertEquals(storingCard.findCard(0).toString(), loadedCard.findCard(0).toString())
            assertEquals(storingCard.findCard(1).toString(), loadedCard.findCard(1).toString())
            assertEquals(storingCard.findCard(2).toString(), loadedCard.findCard(2).toString())
        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty cardTest.json file.
            val storingCard = CardAPI(JSONSerializer(File("cardTest.json")))
            storingCard.store()

            // Loading the empty cards.json file into a new object
            val loadedCard = CardAPI(JSONSerializer(File("cardTest.json")))
            loadedCard.load()

            // Comparing the source of the cards (storingCard) with the json loaded cards (loadedCard)
            assertEquals(0, storingCard.numberOfCards())
            assertEquals(0, loadedCard.numberOfCards())
            assertEquals(storingCard.numberOfCards(), loadedCard.numberOfCards())
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            // Storing 3 cards to the cardTest.json file.
            val storingCard = CardAPI(JSONSerializer(File("cardTest.json")))
            storingCard.add(rainbow!!)
            storingCard.add(squirrel!!)
            storingCard.add(ourobouros!!)
            storingCard.store()

            // Loading cardTest.json into a different collection
            val loadedCard = CardAPI(JSONSerializer(File("cardTest.json")))
            loadedCard.load()

            // Comparing the source of the cards (storingCard) with the json loaded cards (loadedCard)
            assertEquals(3, storingCard.numberOfCards())
            assertEquals(3, loadedCard.numberOfCards())
            assertEquals(storingCard.numberOfCards(), loadedCard.numberOfCards())
            assertEquals(storingCard.findCard(0).toString(), loadedCard.findCard(0).toString())
            assertEquals(storingCard.findCard(1).toString(), loadedCard.findCard(1).toString())
            assertEquals(storingCard.findCard(2).toString(), loadedCard.findCard(2).toString())
        }
    }














}