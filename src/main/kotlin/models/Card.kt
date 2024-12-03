package ie.setu.models

class Card(
    var cardName: String,
    var hp: Int = 0,
    var dmg: Int = 0,
    var tribe: String,
    var cost: Int = 0,
    var costType: String = "",
    var sigils: ArrayList<Sigil?> = ArrayList<Sigil?>(),
    var moxCost: ArrayList<Boolean> = ArrayList<Boolean>()
)