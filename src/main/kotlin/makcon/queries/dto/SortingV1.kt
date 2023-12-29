package makcon.queries.dto

data class SortingV1(
    val field: String,
    val dir: String,
)

object SortDirV1 {
    
    const val ASC = "ASC"
    const val DESC = "DESC"
}