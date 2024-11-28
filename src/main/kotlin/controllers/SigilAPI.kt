package ie.setu.controllers

import ie.setu.models.Sigil
import utils.isValidListIndex

class SigilAPI {

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

    private fun formatListString(sigilsToFormat: List<Sigil>): String =
        sigilsToFormat
            .joinToString(separator = "\n") { sigil ->
                sigils.indexOf(sigil).toString() + ": " + sigil.toString()
            }

    fun numberOfSigils() = sigils.size



}

