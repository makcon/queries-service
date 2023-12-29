package makcon.queries.domain.model

import makcon.queries.domain.model.constant.FilterField
import makcon.queries.domain.model.query.Paging
import makcon.queries.domain.model.query.QueryFilter
import makcon.queries.domain.model.query.Sorting

data class Query(
    val filters: List<QueryFilter> = listOf(),
    val paging: Paging,
    val sorting: Sorting?,
    val resultFields: Set<ResultField> = setOf(),
    val connector: QueryConnector = QueryConnector.AND,
) {
    
    fun hasAnyField(names: List<FilterField>): Boolean = filters.find { names.contains(it.field) } != null
    
    fun getFilter(name: FilterField): QueryFilter? = filters.find { it.field == name }
}

enum class ResultField {
    COUNT,
    ITEMS,
}

enum class QueryConnector {
    AND,
    OR,
}