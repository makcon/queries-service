@file:Suppress("UNCHECKED_CAST")

package makcon.queries.adapters.query.builder

import makcon.queries.adapters.query.model.FieldMapping
import makcon.queries.domain.model.query.*
import org.mybatis.dynamic.sql.SortSpecification
import org.mybatis.dynamic.sql.SqlBuilder
import org.mybatis.dynamic.sql.VisitableCondition

fun <T> QueryOperator.toSql(value: T): VisitableCondition<T> = when (this) {
    QueryOperator.EQ -> SqlBuilder.isEqualTo(value)
    QueryOperator.IN -> SqlBuilder.isIn(value)
    QueryOperator.START_WITH -> SqlBuilder.isLikeCaseInsensitive("$value%") as VisitableCondition<T>
    QueryOperator.CONTAINS -> SqlBuilder.isLikeCaseInsensitive("%$value%") as VisitableCondition<T>
    QueryOperator.RANGE -> value!!.toRange()
}

fun Sorting?.toSql(columnMap: FieldMapping): SortSpecification? {
    if (this == null) return null
    
    val sqlColumn = columnMap.getSqlColumn(field)
    
    if (dir == SortDir.ASC) return sqlColumn
    
    return sqlColumn.descending()
}

fun Paging.toOffset(): Long = (size * (number + 1) - size).toLong()

private fun <T> Any.toRange(): VisitableCondition<T> {
    require(this is Range) { "Value must be a Range" }
    
    return SqlBuilder.isBetween(from as T).and(to as T)
}