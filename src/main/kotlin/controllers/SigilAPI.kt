package ie.setu.controllers

import ie.setu.models.Sigil
import persistence.Serializer
import utils.isValidListIndex

class SigilAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType
    private var sigils = ArrayList<Sigil>()
    fun add(sigil: Sigil): Boolean{
        return sigils.add(sigil)
    }
    fun deleteSigil(indexToDelete: Int): Sigil? {
        return if (isValidListIndex(indexToDelete, sigils)) {
            sigils.removeAt(indexToDelete)
        } else {
            null
        }
    }

    fun listAllSigils(): String =
        if (sigils.isEmpty()) {
            "No sigils stored"
        } else {
            formatListString(sigils)
        }

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

    fun findSigil(index: Int): Sigil? {
        return if (isValidListIndex(index, sigils)) {
            sigils[index]
        } else {
            null
        }
    }

    fun searchByDescription(searchString: String) =
        formatListString(
            sigils.filter { sigil -> sigil.sigilDescription.contains(searchString, ignoreCase = true) }
        )


    fun isValidIndex(index: Int): Boolean {
        return isValidListIndex(index, sigils)
    }

    private fun formatListString(sigilsToFormat: List<Sigil>): String =
        sigilsToFormat
            .joinToString(separator = "\n") { sigil ->
                sigils.indexOf(sigil).toString() + ": " + sigil.toString()
            }

    fun numberOfSigils() = sigils.size

    @Throws(Exception::class)
    fun load() {
        sigils = serializer.read() as ArrayList<Sigil>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(sigils)
    }



}

