package makcon.queries.dto

import makcon.queries.dto.QueryOperatorV1.CONTAINS

data class QueryFilterV1(
    val field: String,
    val value: Any,
    val operator: String = CONTAINS,
)

object QueryOperatorV1 {
    
    const val EQ = "EQ"
    const val IN = "IN"
    const val CONTAINS = "CONTAINS"
    const val START_WITH = "START_WITH"
    const val RANGE = "RANGE"
}
