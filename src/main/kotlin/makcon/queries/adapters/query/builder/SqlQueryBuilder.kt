package makcon.queries.adapters.query.builder

import makcon.queries.adapters.query.model.FieldMapping
import makcon.queries.adapters.repository.table.BaseSqlTable
import makcon.queries.domain.model.Query
import mu.KotlinLogging
import org.mybatis.dynamic.sql.SqlBuilder
import org.mybatis.dynamic.sql.render.RenderingStrategies.SPRING_NAMED_PARAMETER
import org.mybatis.dynamic.sql.select.AbstractQueryExpressionDSL
import org.mybatis.dynamic.sql.select.CountDSL
import org.mybatis.dynamic.sql.select.QueryExpressionDSL
import org.mybatis.dynamic.sql.select.SelectModel
import org.mybatis.dynamic.sql.util.Buildable

class SqlQueryBuilder<T>(
    val table: BaseSqlTable<T>,
    private val query: Query,
    private val joinBuilders: List<JoinBuilder> = listOf(),
    private val fieldMapping: FieldMapping,
) {
    
    private val log = KotlinLogging.logger { }
    
    fun buildSelect(): QueryExpressionDSL<SelectModel> {
        val queryExpression = SqlBuilder
            .select(table.getColumns())
            .from(table)
        
        addJoin(queryExpression)
        addWhere(queryExpression)
        
        query.sorting.toSql(fieldMapping)?.let { queryExpression.orderBy(it) }
        queryExpression
            .limit(query.paging.size.toLong())
            .offset(query.paging.toOffset())
        
        logQuery(queryExpression)
        
        return queryExpression
    }
    
    fun buildCount(): CountDSL<SelectModel>? {
        val queryExpression = SqlBuilder
            .countFrom(table)
        
        addJoin(queryExpression)
        addWhere(queryExpression)
        
        logQuery(queryExpression)
        
        return queryExpression
    }
    
    private fun addJoin(queryExpression: AbstractQueryExpressionDSL<*, *>) {
        joinBuilders.forEach { it.build(queryExpression, query) }
    }
    
    private fun addWhere(queryExpression: AbstractQueryExpressionDSL<*, *>) {
        val criteria = fieldMapping.whereBuilders.mapNotNull { it.build(query) }
        if (criteria.isEmpty()) return
        
        queryExpression.where(criteria)
    }
    
    private fun logQuery(queryExpression: Buildable<SelectModel>) {
        if (!log.isDebugEnabled) return
        
        val query = queryExpression.build().render(SPRING_NAMED_PARAMETER)
        log.debug { "Query: ${query.selectStatement} with params: ${query.parameters}" }
    }
}