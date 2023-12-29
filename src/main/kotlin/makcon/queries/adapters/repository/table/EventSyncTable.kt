package makcon.queries.adapters.repository.table

import makcon.queries.adapters.repository.constant.ColumnName.LAST_CREATED_AT
import makcon.queries.adapters.repository.constant.ColumnName.SYNCED
import makcon.queries.adapters.repository.constant.TableName
import makcon.queries.adapters.repository.mapper.getBoolRequired
import makcon.queries.adapters.repository.mapper.getInstant
import makcon.queries.adapters.repository.mapper.getStrRequired
import makcon.queries.domain.model.EventSync
import org.mybatis.dynamic.sql.BasicColumn
import org.mybatis.dynamic.sql.util.kotlin.elements.column
import java.sql.ResultSet
import java.sql.Timestamp

val eventSyncTable = EventSyncTable()

class EventSyncTable : BaseSqlTable<EventSync>(TableName.EVENTS_SYNC) {
    
    val lastCreatedAt = column<Timestamp>(name = LAST_CREATED_AT)
    val synced = column<Boolean>(name = SYNCED)
    
    override fun getColumns(): List<BasicColumn> = listOf(
        id,
        lastCreatedAt,
        synced
    )
    
    override fun getRowMapper(): (ResultSet, Int) -> EventSync = { rs, _ ->
        EventSync(
            topic = rs.getStrRequired(id),
            lastCreatedAt = rs.getInstant(lastCreatedAt),
            synced = rs.getBoolRequired(synced),
        )
    }
}