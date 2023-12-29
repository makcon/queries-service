package makcon.queries.domain.model.query

import makcon.queries.domain.model.constant.FilterField

data class QueryFilter(
    val field: FilterField,
    val value: Any,
    val operator: QueryOperator,
)

enum class QueryOperator {
    EQ,
    IN,
    CONTAINS,
    START_WITH,
    RANGE
}
