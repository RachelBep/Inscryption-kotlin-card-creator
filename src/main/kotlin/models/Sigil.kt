package ie.setu.models

class Sigil(
    var sigilName: String,
    var sigilDescription: String,
    var power: Int
    ) {
    override fun toString(): String {
        return sigilName + ", Power level " + power +  ": " + sigilDescription
    }
}