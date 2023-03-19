package lk.nibm.holidayviewer.model

data class SeparatedHolidaysModel(
    val name: String,
    val date: String,
    val month : String,
    val primaryType : String,
    val description : String,
    var isVisible : Boolean = false
)
