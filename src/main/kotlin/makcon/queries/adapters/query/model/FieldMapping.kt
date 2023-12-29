package makcon.queries.adapters.query.model

import makcon.queries.adapters.exception.FieldMappingException
import makcon.queries.adapters.query.builder.GeneralWhereBuilder
import makcon.queries.adapters.query.builder.WhereBuilder
import makcon.queries.adapters.repository.table.BaseSqlTable
import makcon.queries.domain.model.constant.FilterField
import org.mybatis.dynamic.sql.SqlColumn
import java.time.Instant

@Suppress("UNCHECKED_CAST")
class FieldMapping(
    private val table: BaseSqlTable<*>,
    private val searchFieldsMap: Map<FilterField, SqlColumn<*>>,
) {
    
    private val allFieldsMap: Map<FilterField, SqlColumn<*>> = createMap()
    internal val whereBuilders: List<WhereBuilder<*>> = createWhereBuilders()
    
    fun getSqlColumn(field: FilterField): SqlColumn<*> = allFieldsMap[field]
        ?: throw FieldMappingException(
            field,
            "Sql column is not mapped to filter field"
        )
    
    private fun createWhereBuilders(): List<WhereBuilder<*>> = allFieldsMap.entries.map {
        when (it.key.type) {
            Instant::class -> GeneralWhereBuilder(it.key, it.value as SqlColumn<Instant>)
            String::class -> GeneralWhereBuilder(it.key, it.value as SqlColumn<String>)
            else -> throw FieldMappingException(
                it.key,
                "The type ${it.key.type.simpleName} is not mapped to where builder"
            )
        }
    }
    
    private fun createMap(): Map<FilterField, SqlColumn<*>> {
        val map = mutableMapOf<FilterField, SqlColumn<*>>(
            FilterField.ID to table.id,
            FilterField.CREATED_AT to table.createdAt,
            FilterField.UPDATED_AT to table.updatedAt,
        )
        
        map.putAll(searchFieldsMap)
        
        return map.toMap()
    }
}