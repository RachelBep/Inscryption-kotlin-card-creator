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
) {
    override fun toString(): String {
        val sigilString = sigils.joinToString()
        if (costType.equals("Mox")) {
            var moxString = ""
            if (moxCost[0]) {
                moxString = moxString + "R"
            }
            if (moxCost[1]) {
                moxString = moxString + "G"
            }
            if (moxCost[2]) {
                moxString = moxString + "b"
            }
            return "Name: ${cardName}, hp: ${hp}, dmg: ${dmg}, tribe: ${tribe}, Cost: ${moxString} ${costType}, Sigils: ${sigilString}"
        } else {
            return "Name: ${cardName}, hp: ${hp}, dmg: ${dmg}, tribe: ${tribe}, Cost: ${cost} ${costType}, Sigils: ${sigilString}"
        }
    }
}