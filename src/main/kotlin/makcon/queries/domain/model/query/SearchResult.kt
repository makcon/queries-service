package makcon.queries.domain.model.query

data class SearchResult<T>(
    val totalCount: Long?,
    val items: List<T>?,
)
