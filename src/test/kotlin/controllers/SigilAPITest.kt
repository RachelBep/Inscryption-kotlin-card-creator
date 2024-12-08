package controllers

import ie.setu.controllers.SigilAPI
import ie.setu.models.Sigil
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File

class SigilAPITest {

    private var esplode: Sigil? = null
    private var bistrike: Sigil? = null
    private var brittle: Sigil? = null
    private var diving: Sigil? = null
    private var flying: Sigil? = null
    private var populatedSigils: SigilAPI? = SigilAPI(XMLSerializer(File("sigils.xml")))
    private var emptySigils: SigilAPI? = SigilAPI(XMLSerializer(File("empty-sigils.xml")))

    @BeforeEach
    fun setup() {
        esplode = Sigil("Exploding", "when this dies it deals 5 damage to all cards around it", 10)
        bistrike = Sigil("Bifrucating Strike", "this deals damage to the left and right", 6)
        brittle = Sigil("Brittle", "at the end of turn, this card dies", -3)
        diving = Sigil("Diving", "this cannot be attacked", 3)
        flying = Sigil("Flying", "this attacks directly", 6)

        // adding 5 Sigil to the sigils api
        populatedSigils!!.add(esplode!!)
        populatedSigils!!.add(bistrike!!)
        populatedSigils!!.add(brittle!!)
        populatedSigils!!.add(diving!!)
        populatedSigils!!.add(flying!!)
    }

    @AfterEach
    fun tearDown() {
        esplode = null
        bistrike = null
        brittle = null
        diving = null
        flying = null
        populatedSigils = null
        emptySigils = null
    }

    @Nested
    inner class AddSigils {
        @Test
        fun `adding a Sigil to a populated list adds to ArrayList`() {
            val newSigil = Sigil("Exploding", "when this dies it deals 5 damage to all cards around it", 10)
            assertEquals(5, populatedSigils!!.numberOfSigils())
            assertTrue(populatedSigils!!.add(newSigil))
            assertEquals(6, populatedSigils!!.numberOfSigils())
            assertEquals(newSigil, populatedSigils!!.findSigil(populatedSigils!!.numberOfSigils() - 1))
        }

        @Test
        fun `adding a Sigil to an empty list adds to ArrayList`() {
            val newSigil = Sigil("Exploding", "when this dies it deals 5 damage to all cards around it", 10)
            assertEquals(0, emptySigils!!.numberOfSigils())
            assertTrue(emptySigils!!.add(newSigil))
            assertEquals(1, emptySigils!!.numberOfSigils())
            assertEquals(newSigil, emptySigils!!.findSigil(emptySigils!!.numberOfSigils() - 1))
        }
    }

    @Nested
    inner class ListSigils {

        @Test
        fun `listAllSigils returns No Sigils Stored message when ArrayList is empty`() {
            assertEquals(0, emptySigils!!.numberOfSigils())
            assertTrue(emptySigils!!.listAllSigils().lowercase().contains("no sigils"))
        }

        @Test
        fun `listAllSigils returns Sigils when ArrayList has sigils stored`() {
            assertEquals(5, populatedSigils!!.numberOfSigils())
            val sigilsString = populatedSigils!!.listAllSigils().lowercase()
            assertTrue(sigilsString.contains("exploding"))
            assertTrue(sigilsString.contains("strike"))
            assertTrue(sigilsString.contains("brittle"))
            assertTrue(sigilsString.contains("flying"))
            assertTrue(sigilsString.contains("diving"))
        }

    }

    @Nested
    inner class DeleteSigils {

        @Test
        fun `deleting a Sigil that does not exist, returns null`() {
            assertNull(emptySigils!!.deleteSigil(0))
            assertNull(populatedSigils!!.deleteSigil(-1))
            assertNull(populatedSigils!!.deleteSigil(5))
        }

        @Test
        fun `deleting a sigil that exists delete and returns deleted object`() {
            assertEquals(5, populatedSigils!!.numberOfSigils())
            assertEquals(flying, populatedSigils!!.deleteSigil(4))
            assertEquals(4, populatedSigils!!.numberOfSigils())
            assertEquals(esplode, populatedSigils!!.deleteSigil(0))
            assertEquals(3, populatedSigils!!.numberOfSigils())
        }
    }

    @Nested
    inner class UpdateSigils {
        @Test
        fun `updating a sigil that does not exist returns false`() {
            assertFalse(populatedSigils!!.updateSigil(6, Sigil("Updated", "Updated", 5)))
            assertFalse(populatedSigils!!.updateSigil(-1, Sigil("Updated", "Updated", 5)))
            assertFalse(emptySigils!!.updateSigil(0, Sigil("Updated", "Updated", 5)))
        }

        @Test
        fun `updating a sigil that exists returns true and updates`() {
            // check sigil 5 exists and check the contents
            assertEquals(flying, populatedSigils!!.findSigil(4))
            assertEquals("Flying", populatedSigils!!.findSigil(4)!!.sigilName)
            assertEquals("this attacks directly", populatedSigils!!.findSigil(4)!!.sigilDescription)
            assertEquals(6, populatedSigils!!.findSigil(4)!!.power)

            // update sigil 5 with new information and ensure contents updated successfully
            assertTrue(populatedSigils!!.updateSigil(4, Sigil("Updated", "Updated", 5)))
            assertEquals("Updated", populatedSigils!!.findSigil(4)!!.sigilName)
            assertEquals("Updated", populatedSigils!!.findSigil(4)!!.sigilDescription)
            assertEquals(5, populatedSigils!!.findSigil(4)!!.power)
        }
    }

    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty sigils.XML file.
            val storingSigils = SigilAPI(XMLSerializer(File("sigils.xml")))
            storingSigils.store()

            // Loading the empty sigils.xml file into a new object
            val loadedSigils = SigilAPI(XMLSerializer(File("sigils.xml")))
            loadedSigils.load()

            // Comparing the source of the sigils (storingSigils) with the XML loaded sigils (loadedSigils)
            assertEquals(0, storingSigils.numberOfSigils())
            assertEquals(0, loadedSigils.numberOfSigils())
            assertEquals(storingSigils.numberOfSigils(), loadedSigils.numberOfSigils())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 sigils to the sigils.XML file.
            val storingSigils = SigilAPI(XMLSerializer(File("sigils.xml")))
            storingSigils.add(diving!!)
            storingSigils.add(flying!!)
            storingSigils.add(bistrike!!)
            storingSigils.store()

            // Loading sigils.xml into a different collection
            val loadedSigils = SigilAPI(XMLSerializer(File("sigils.xml")))
            loadedSigils.load()

            // Comparing the source of the sigils (storingSigils) with the XML loaded sigils (loadedSigils)
            assertEquals(3, storingSigils.numberOfSigils())
            assertEquals(3, loadedSigils.numberOfSigils())
            assertEquals(storingSigils.numberOfSigils(), loadedSigils.numberOfSigils())
            assertEquals(storingSigils.findSigil(0).toString(), loadedSigils.findSigil(0).toString())
            assertEquals(storingSigils.findSigil(1).toString(), loadedSigils.findSigil(1).toString())
            assertEquals(storingSigils.findSigil(2).toString(), loadedSigils.findSigil(2).toString())
        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty sigils.json file.
            val storingSigils = SigilAPI(JSONSerializer(File("sigils.json")))
            storingSigils.store()

            // Loading the empty sigils.json file into a new object
            val loadedSigils = SigilAPI(JSONSerializer(File("sigils.json")))
            loadedSigils.load()

            // Comparing the source of the sigils (storingSigils) with the json loaded sigils (loadedSigils)
            assertEquals(0, storingSigils.numberOfSigils())
            assertEquals(0, loadedSigils.numberOfSigils())
            assertEquals(storingSigils.numberOfSigils(), loadedSigils.numberOfSigils())
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            // Storing 3 sigils to the sigils.json file.
            val storingSigils = SigilAPI(JSONSerializer(File("sigils.json")))
            storingSigils.add(diving!!)
            storingSigils.add(flying!!)
            storingSigils.add(bistrike!!)
            storingSigils.store()

            // Loading sigils.json into a different collection
            val loadedSigils = SigilAPI(JSONSerializer(File("sigils.json")))
            loadedSigils.load()

            // Comparing the source of the sigils (storingSigils) with the json loaded sigils (loadedSigils)
            assertEquals(3, storingSigils.numberOfSigils())
            assertEquals(3, loadedSigils.numberOfSigils())
            assertEquals(storingSigils.numberOfSigils(), loadedSigils.numberOfSigils())
            assertEquals(storingSigils.findSigil(0).toString(), loadedSigils.findSigil(0).toString())
            assertEquals(storingSigils.findSigil(1).toString(), loadedSigils.findSigil(1).toString())
            assertEquals(storingSigils.findSigil(2).toString(), loadedSigils.findSigil(2).toString())
        }
    }


    @Nested
    inner class CountingMethods {

        @Test
        fun numberOfSigilsCalculatedCorrectly() {
            assertEquals(5, populatedSigils!!.numberOfSigils())
            assertEquals(0, emptySigils!!.numberOfSigils())
        }

    }

    @Nested
    inner class SearchMethods {
        @Test
        fun `search sigils by desc returns no sigils when no sigils with that desc exist`() {
            // Searching a populated collection for a title that doesn't exist.
            assertEquals(5, populatedSigils!!.numberOfSigils())
            val searchResults = populatedSigils!!.searchByDescription("no results expected")
            assertTrue(searchResults.isEmpty())
            // Searching an empty collection
            assertEquals(0, emptySigils!!.numberOfSigils())
            assertTrue(emptySigils!!.searchByDescription("").isEmpty())
        }

        @Test
        fun `search sigils by title returns sigils when sigils with that title exist`() {
            assertEquals(5, populatedSigils!!.numberOfSigils())
            // Searching a populated collection for a full desc that exists (case matches exactly)
            var searchResults = populatedSigils!!.searchByDescription("when this dies it deals 5 damage to all cards around it")
            assertTrue(searchResults.contains("Exploding"))
            assertFalse(searchResults.contains("flying"))
            // Searching a populated collection for a partial desc that exists (case matches exactly)
            searchResults = populatedSigils!!.searchByDescription("card")
            assertTrue(searchResults.contains("Exploding"))
            assertTrue(searchResults.contains("turn"))
            assertFalse(searchResults.contains("flying"))
            // Searching a populated collection for a partial desc that exists (case doesn't match)
            searchResults = populatedSigils!!.searchByDescription("cARD")
            assertTrue(searchResults.contains("Exploding"))
            assertTrue(searchResults.contains("turn"))
            assertFalse(searchResults.contains("flying"))
        }
    }
}