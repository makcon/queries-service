package makcon.queries.app.mapper

import makcon.queries.domain.model.query.SearchResult
import makcon.queries.dto.SearchResultV1

fun <M, D> SearchResult<M>.toDto(itemMapper: (M) -> D) = SearchResultV1(
    totalCount = totalCount,
    items = items?.map { itemMapper(it) }
)