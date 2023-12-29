package makcon.queries.adapters.repository.adapter

import makcon.queries.adapters.query.handler.OrderQueryHandler
import makcon.queries.adapters.repository.CrudRepository
import makcon.queries.adapters.repository.mapper.toTimestamp
import makcon.queries.adapters.repository.table.orderTable
import makcon.queries.domain.command.CreateObjectCommand
import makcon.queries.domain.command.DeleteObjectCommand
import makcon.queries.domain.command.UpdateObjectCommand
import makcon.queries.domain.model.Model
import makcon.queries.domain.model.OrderData
import makcon.queries.domain.model.Query
import makcon.queries.domain.model.query.SearchResult
import makcon.queries.domain.port.OrderRepositoryPort
import org.mybatis.dynamic.sql.util.kotlin.KotlinGeneralInsertBuilder
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class OrderRepositoryAdapter(
    template: NamedParameterJdbcTemplate,
    private val queryHandler: OrderQueryHandler,
) : CrudRepository<Model<OrderData>>(template, orderTable), OrderRepositoryPort {
    
    override fun create(command: CreateObjectCommand<OrderData>): Boolean = create(
        completer = buildCreateCompleter(
            orderId = command.objectId,
            createdAt = command.createdAt,
            updatedAt = command.createdAt,
            command.data
        )
    )
    
    override fun upsert(command: UpdateObjectCommand<OrderData>): Boolean = upsert(
        entityId = command.objectId,
        createCompleter = buildCreateCompleter(
            orderId = command.objectId,
            createdAt = command.updatedAt,
            updatedAt = command.updatedAt,
            data = command.data
        )
    ) {
        set(orderTable.updatedAt) equalTo command.updatedAt.toTimestamp()
        set(orderTable.productId) equalTo command.data.productId
        set(orderTable.userId) equalTo command.data.userId
        set(orderTable.name) equalTo command.data.name
        set(orderTable.status) equalTo command.data.status
        set(orderTable.rawData) equalTo command.data.rawData
        where {
            orderTable.id isEqualTo command.objectId
            and { orderTable.updatedAt isLessThan command.updatedAt.toTimestamp() }
        }
    }
    
    override fun delete(command: DeleteObjectCommand): Boolean = deleteById(
        entityId = command.objectId,
        deletedAt = command.deletedAt
    )
    
    override fun find(query: Query): SearchResult<Model<OrderData>> = queryHandler.find(query)
    
    private fun buildCreateCompleter(
        orderId: String,
        createdAt: Instant,
        updatedAt: Instant,
        data: OrderData,
    ): KotlinGeneralInsertBuilder.() -> Unit = {
        set(orderTable.id) toValue orderId
        set(orderTable.createdAt) toValue createdAt.toTimestamp()
        set(orderTable.updatedAt) toValue updatedAt.toTimestamp()
        set(orderTable.productId) toValue data.productId
        set(orderTable.userId) toValue data.userId
        set(orderTable.name) toValue data.name
        set(orderTable.status) toValue data.status
        set(orderTable.rawData) toValue data.rawData
    }
}