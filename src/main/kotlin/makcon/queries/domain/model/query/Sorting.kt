package makcon.queries.domain.model.query

import makcon.queries.domain.model.constant.FilterField

data class Sorting(
    val field: FilterField,
    val dir: SortDir,
)

enum class SortDir {
    ASC,
    DESC
}
