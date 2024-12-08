package ie.setu.controllers

import ie.setu.models.Sigil
import persistence.Serializer
import utils.isValidListIndex
/**
 * Controller class for managing a collection of sigils.
 * Provides functionality for adding, updating, deleting, and searching sigils.
 *
 * @param serializerType the serializer used to persist and load sigil data
 */
class SigilAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType
    private var sigils = ArrayList<Sigil>()

    /**
     * Adds a new sigil to the collection.
     *
     * @param sigil the sigil to be added
     * @return true if the sigil was successfully added, false otherwise
     */
    fun add(sigil: Sigil): Boolean{
        return sigils.add(sigil)
    }
    /**
     * Deletes a sigil by its index.
     *
     * @param indexToDelete the index of the sigil to delete
     * @return the deleted sigil, or null if the index is invalid
     */
    fun deleteSigil(indexToDelete: Int): Sigil? {
        return if (isValidListIndex(indexToDelete, sigils)) {
            sigils.removeAt(indexToDelete)
        } else {
            null
        }
    }

    /**
     * Lists all sigils in the collection.
     *
     * @return a formatted string containing all sigils, or a message if no sigils are stored
     */
    fun listAllSigils(): String =
        if (sigils.isEmpty()) {
            "No sigils stored"
        } else {
            formatListString(sigils)
        }
    /**
     * Updates the attributes of a sigil at a specified index.
     *
     * @param indexToUpdate the index of the sigil to update
     * @param sigil the new sigil data to apply
     * @return true if the update was successful, false otherwise
     */
    fun updateSigil(indexToUpdate: Int, sigil: Sigil?): Boolean {
        // find the sigil object by the index number
        val foundSigil = findSigil(indexToUpdate)

        // if the sigil exists, use the sigil details passed as parameters to update the found sigil in the ArrayList.
        if ((foundSigil != null) && (sigil != null)) {
            foundSigil.sigilName = sigil.sigilName
            foundSigil.sigilDescription = sigil.sigilDescription
            foundSigil.power = sigil.power
            return true
        }

        // if the sigil was not found, return false, indicating that the update was not successful
        return false
    }
    /**
     * Finds a sigil by its index.
     *
     * @param index the index of the sigil to find
     * @return the sigil at the specified index, or null if the index is invalid
     */
    fun findSigil(index: Int): Sigil? {
        return if (isValidListIndex(index, sigils)) {
            sigils[index]
        } else {
            null
        }
    }

    /**
     * Searches for sigils with a description that contains a specific string.
     *
     * @param searchString the string to search for in sigil descriptions
     * @return a formatted string containing the matching sigils, or an empty result if none match
     */
    fun searchByDescription(searchString: String) =
        formatListString(
            sigils.filter { sigil -> sigil.sigilDescription.contains(searchString, ignoreCase = true) }
        )

    /**
     * Checks if the provided index is valid for the sigil collection.
     *
     * @param index the index to check
     * @return true if the index is valid, false otherwise
     */
    fun isValidIndex(index: Int): Boolean {
        return isValidListIndex(index, sigils)
    }
    /**
     * Formats a list of sigils into a readable string.
     *
     * @param sigilsToFormat the list of sigils to format
     * @return a formatted string with sigil details
     */
    private fun formatListString(sigilsToFormat: List<Sigil>): String =
        sigilsToFormat
            .joinToString(separator = "\n") { sigil ->
                sigils.indexOf(sigil).toString() + ": " + sigil.toString()
            }
    /**
     * Gets the total number of sigils in the collection.
     *
     * @return the size of the sigil collection
     */
    fun numberOfSigils() = sigils.size

    /**
     * Loads the sigil data from persistent storage.
     *
     * @throws Exception if an error occurs during loading
     */
    @Throws(Exception::class)
    fun load() {
        sigils = serializer.read() as ArrayList<Sigil>
    }
    /**
     * Stores the sigil data to persistent storage.
     *
     * @throws Exception if an error occurs during saving
     */
    @Throws(Exception::class)
    fun store() {
        serializer.write(sigils)
    }



}

