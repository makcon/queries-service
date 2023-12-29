package makcon.queries.dto

import makcon.queries.dto.ResultFieldV1.COUNT
import makcon.queries.dto.ResultFieldV1.ITEMS

data class QueryV1(
    val filters: Set<QueryFilterV1> = setOf(),
    val paging: PagingV1 = PagingV1(),
    val sorting: SortingV1? = null,
    val resultFields: Set<String> = setOf(COUNT, ITEMS),
    val connector: String = QueryConnectorV1.AND,
)

object ResultFieldV1 {
    
    const val COUNT = "COUNT"
    const val ITEMS = "ITEMS"
}

object QueryConnectorV1 {
    
    const val AND = "AND"
    const val OR = "OR"
}