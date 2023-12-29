package makcon.queries.adapters.query.handler

import makcon.queries.adapters.query.builder.JoinBuilder
import makcon.queries.adapters.query.builder.SqlQueryBuilder
import makcon.queries.adapters.query.model.FieldMapping
import makcon.queries.adapters.repository.table.BaseSqlTable
import makcon.queries.domain.model.Query
import makcon.queries.domain.model.ResultField.COUNT
import makcon.queries.domain.model.ResultField.ITEMS
import makcon.queries.domain.model.query.SearchResult
import org.mybatis.dynamic.sql.util.spring.NamedParameterJdbcTemplateExtensions

abstract class AbstractQueryHandler<T>(
    private val template: NamedParameterJdbcTemplateExtensions,
    private val table: BaseSqlTable<T>,
) {
    
    abstract fun getFieldMapping(): FieldMapping
    
    abstract fun getJoinBuilders(): List<JoinBuilder>
    
    fun find(query: Query): SearchResult<T> {
        val sqlQuery = SqlQueryBuilder(
            table = table,
            query = query,
            joinBuilders = getJoinBuilders(),
            fieldMapping = getFieldMapping()
        )
        
        return doFind(query, sqlQuery)
    }
    
    private fun <T> doFind(query: Query, builder: SqlQueryBuilder<T>): SearchResult<T> {
        val totalCount =
            if (query.resultFields.contains(COUNT)) template.count(builder.buildCount())
            else null
        val items =
            if (query.resultFields.contains(ITEMS)) template.selectList(
                builder.buildSelect(),
                builder.table.getRowMapper()
            )
            else null
        
        return SearchResult(totalCount, items)
    }
}