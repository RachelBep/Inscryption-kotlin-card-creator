package ie.setu.controllers

import ie.setu.models.Sigil

class SigilAPI {

    private var sigils = ArrayList<Sigil>()
    fun add(sigil: Sigil): Boolean{
        return sigils.add(sigil)
    }





}

