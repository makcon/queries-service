package makcon.queries.adapters.query.builder

import makcon.queries.domain.model.Query
import makcon.queries.domain.model.constant.FilterField
import org.mybatis.dynamic.sql.BasicColumn
import org.mybatis.dynamic.sql.SqlBuilder.equalTo
import org.mybatis.dynamic.sql.SqlTable
import org.mybatis.dynamic.sql.select.AbstractQueryExpressionDSL
import org.mybatis.dynamic.sql.select.join.JoinCriterion

interface JoinBuilder {
    
    fun build(queryExpression: AbstractQueryExpressionDSL<*, *>, query: Query) {
        if (!query.hasAnyField(getFields())) return
        
        doBuild(queryExpression)
    }
    
    fun getFields(): List<FilterField>
    
    fun doBuild(queryExpression: AbstractQueryExpressionDSL<*, *>)
}

fun AbstractQueryExpressionDSL<*, *>.join(
    table: SqlTable,
    leftColumn: BasicColumn,
    rightColumn: BasicColumn,
): AbstractQueryExpressionDSL<*, *> {
    join(
        table,
        JoinCriterion.Builder()
            .withConnector("on")
            .withJoinColumn(leftColumn)
            .withJoinCondition(equalTo(rightColumn))
            .build()
    )
    
    return this
}