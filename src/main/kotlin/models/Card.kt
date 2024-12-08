package ie.setu.models

class Card(
    var cardName: String,
    var hp: Int = 0,
    var dmg: Int = 0,
    var tribe: String,
    var cost: Int = 0,
    var costType: String = "",
    var sigils: ArrayList<Sigil?> = ArrayList(),
    var moxCost: ArrayList<Boolean> = ArrayList()
) {
    override fun toString(): String {
        val sigilString = sigils.joinToString()
        if (costType == "Mox") {
            var moxString = ""
            if (moxCost[0]) {
                moxString += "R"
            }
            if (moxCost[1]) {
                moxString += "G"
            }
            if (moxCost[2]) {
                moxString += "b"
            }
            return "Name: ${cardName}, hp: ${hp}, dmg: ${dmg}, tribe: ${tribe}, Cost: $moxString ${costType}, Sigils: $sigilString"
        } else {
            return "Name: ${cardName}, hp: ${hp}, dmg: ${dmg}, tribe: ${tribe}, Cost: $cost ${costType}, Sigils: $sigilString"
        }
    }
}