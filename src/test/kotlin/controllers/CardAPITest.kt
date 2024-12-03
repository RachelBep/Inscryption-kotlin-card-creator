package controllers

import ie.setu.controllers.CardAPI
import ie.setu.models.Card
import ie.setu.models.Sigil
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class CardAPITest {
    private var fecundity: Sigil? = null
    private var undying: Sigil? = null
    private var regenerating: Sigil? = null
    private var ourobouros: Card? = null
    private var rainbow: Card? = null
    private var squirrel: Card? = null
    private var populatedCards: CardAPI? = CardAPI()
    private var emptyCards: CardAPI? = CardAPI()
    @BeforeEach
    fun setup() {
        fecundity = Sigil("Fecundity", "adds a copy of this card to your hand when played", 6)
        undying = Sigil("Undying", "when this card perishes put it in your hand instead", 5)
        regenerating = Sigil("Regenerating", "regains 1 life each turn", 3)
        ourobouros =
            arrayListOf(undying)?.let {
                Card("Ouroborous", 1, 1, "none", 2, "Blood",
                    it, arrayListOf(false, false, false))
            }
        rainbow =
            Card("Rainbow Wizard", 2, 4, "Wizard", 0, "Mox",
                arrayListOf(fecundity, regenerating), arrayListOf(true, true, true))
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
}