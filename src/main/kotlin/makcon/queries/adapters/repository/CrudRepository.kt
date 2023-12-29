package makcon.queries.adapters.repository

import makcon.queries.adapters.repository.mapper.toTimestamp
import makcon.queries.adapters.repository.table.BaseSqlTable
import mu.KotlinLogging
import org.mybatis.dynamic.sql.util.kotlin.GeneralInsertCompleter
import org.mybatis.dynamic.sql.util.kotlin.UpdateCompleter
import org.mybatis.dynamic.sql.util.kotlin.spring.*
import org.springframework.dao.DuplicateKeyException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.Instant

abstract class CrudRepository<T>(
    private val template: NamedParameterJdbcTemplate,
    private val table: BaseSqlTable<T>,
) {
    
    private val log = KotlinLogging.logger { }
    
    fun create(completer: GeneralInsertCompleter): Boolean = try {
        val rows = template.insertInto(table, completer)
        
        rows == 1
    } catch (e: DuplicateKeyException) {
        false
    }
    
    fun upsert(
        entityId: String,
        createCompleter: GeneralInsertCompleter,
        updateCompleter: UpdateCompleter,
    ): Boolean {
        val rows = template.update(table, updateCompleter)
        
        if (rows == 0 && !existsById(entityId)) {
            log.info { "Trying to create entity: $entityId in table ${table::class.simpleName}" }
            return create(createCompleter)
        }
        
        return rows == 1
    }
    
    fun deleteById(entityId: String, deletedAt: Instant): Boolean {
        val rows = template.deleteFrom(table) {
            where {
                table.id isEqualTo entityId
                and { table.updatedAt isLessThan deletedAt.toTimestamp() }
            }
        }
        
        return rows == 1
    }
    
    fun existsById(entityId: String): Boolean {
        val count = template.count(table.id) {
            from(table)
            where { table.id isEqualTo entityId }
        }
        
        return count == 1L
    }
    
    fun findById(id: String): T? = template
        .selectOne(table.getColumns()) {
            from(table)
            where { table.id isEqualTo id }
        }
        .withRowMapper(table.getRowMapper())
}

