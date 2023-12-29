package makcon.queries.adapters.repository.adapter

import makcon.queries.adapters.query.handler.ProductQueryHandler
import makcon.queries.adapters.repository.CrudRepository
import makcon.queries.adapters.repository.mapper.toTimestamp
import makcon.queries.adapters.repository.table.productTable
import makcon.queries.domain.command.CreateObjectCommand
import makcon.queries.domain.command.DeleteObjectCommand
import makcon.queries.domain.command.UpdateObjectCommand
import makcon.queries.domain.model.Model
import makcon.queries.domain.model.ProductData
import makcon.queries.domain.model.Query
import makcon.queries.domain.model.query.SearchResult
import makcon.queries.domain.port.ProductRepositoryPort
import org.mybatis.dynamic.sql.util.kotlin.KotlinGeneralInsertBuilder
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class ProductRepositoryAdapter(
    template: NamedParameterJdbcTemplate,
    private val queryHandler: ProductQueryHandler,
) : CrudRepository<Model<ProductData>>(template, productTable), ProductRepositoryPort {
    
    override fun create(command: CreateObjectCommand<ProductData>): Boolean = create(
        completer = buildCreateCompleter(
            productId = command.objectId,
            createdAt = command.createdAt,
            updatedAt = command.createdAt,
            data = command.data
        )
    )
    
    override fun upsert(command: UpdateObjectCommand<ProductData>): Boolean = upsert(
        entityId = command.objectId,
        createCompleter = buildCreateCompleter(
            productId = command.objectId,
            createdAt = command.updatedAt,
            updatedAt = command.updatedAt,
            data = command.data
        )
    ) {
        set(productTable.updatedAt) equalTo command.updatedAt.toTimestamp()
        set(productTable.status) equalTo command.data.status
        set(productTable.name) equalTo command.data.name
        set(productTable.rawData) equalTo command.data.rawData
        where {
            productTable.id isEqualTo command.objectId
            and { productTable.updatedAt isLessThan command.updatedAt.toTimestamp() }
        }
    }
    
    override fun delete(command: DeleteObjectCommand): Boolean = deleteById(
        entityId = command.objectId,
        deletedAt = command.deletedAt
    )
    
    override fun find(query: Query): SearchResult<Model<ProductData>> = queryHandler.find(query)
    
    private fun buildCreateCompleter(
        productId: String,
        createdAt: Instant,
        updatedAt: Instant,
        data: ProductData,
    ): KotlinGeneralInsertBuilder.() -> Unit = {
        set(productTable.id) toValue productId
        set(productTable.createdAt) toValue createdAt.toTimestamp()
        set(productTable.updatedAt) toValue updatedAt.toTimestamp()
        set(productTable.status) toValue data.status
        set(productTable.name) toValue data.name
        set(productTable.rawData) toValue data.rawData
    }
}