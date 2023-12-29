package makcon.queries.adapters.repository.adapter

import makcon.queries.adapters.repository.CrudRepository
import makcon.queries.adapters.repository.mapper.toTimestamp
import makcon.queries.adapters.repository.table.eventSyncTable
import makcon.queries.domain.model.EventSync
import makcon.queries.domain.port.EventSyncRepositoryPort
import org.mybatis.dynamic.sql.util.kotlin.spring.select
import org.mybatis.dynamic.sql.util.kotlin.spring.selectList
import org.mybatis.dynamic.sql.util.kotlin.spring.update
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class EventSyncRepositoryAdapter(
    private val template: NamedParameterJdbcTemplate,
) : CrudRepository<EventSync>(template, eventSyncTable), EventSyncRepositoryPort {
    
    override fun findNotSynced(): List<EventSync> {
        val statement = select(eventSyncTable.getColumns()) {
            from(eventSyncTable)
            where { eventSyncTable.synced isEqualTo false }
        }
        
        return template.selectList(statement, eventSyncTable.getRowMapper())
    }
    
    override fun findByTopic(topic: String): EventSync? = findById(topic)
    
    override fun update(eventSync: EventSync) {
        template.update(eventSyncTable) {
            set(eventSyncTable.updatedAt) equalTo Instant.now().toTimestamp()
            set(eventSyncTable.lastCreatedAt) equalToOrNull eventSync.lastCreatedAt?.toTimestamp()
            set(eventSyncTable.synced) equalTo eventSync.synced
        }
    }
}