@file:Suppress("UNCHECKED_CAST")

package makcon.queries.adapters.query.builder

import makcon.queries.domain.model.Query
import makcon.queries.domain.model.constant.FilterField
import makcon.queries.domain.model.constant.FilterField.*
import makcon.queries.domain.model.query.QueryOperator.*
import org.mybatis.dynamic.sql.*
import java.sql.JDBCType.*

interface WhereBuilder<T> {
    
    fun build(query: Query): AndOrCriteriaGroup? {
        val filter = query.getFilter(getField()) ?: return null
        
        val criterion = ColumnAndConditionCriterion
            .withColumn(getColumn())
            .withCondition(filter.operator.toSql(filter.value as T))
            .build()
        
        return AndOrCriteriaGroup.Builder()
            .withConnector(query.connector.name.lowercase())
            .withInitialCriterion(criterion)
            .build()
    }
    
    fun getField(): FilterField
    
    fun getColumn(): BindableColumn<T>
}

class GeneralWhereBuilder<T>(
    private val field: FilterField,
    private val column: SqlColumn<T>,
) : WhereBuilder<T> {
    
    override fun getField(): FilterField = field
    
    override fun getColumn(): BindableColumn<T> = column
}