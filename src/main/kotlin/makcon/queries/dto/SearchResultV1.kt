package makcon.queries.dto

data class SearchResultV1<T>(
    val totalCount: Long?,
    val items: List<T>?,
)
