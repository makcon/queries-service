package makcon.queries.domain.port

import makcon.queries.domain.command.CreateObjectCommand
import makcon.queries.domain.command.DeleteObjectCommand
import makcon.queries.domain.command.UpdateObjectCommand
import makcon.queries.domain.model.Model
import makcon.queries.domain.model.OrderData
import makcon.queries.domain.model.Query
import makcon.queries.domain.model.query.SearchResult

interface OrderRepositoryPort {
    
    fun create(command: CreateObjectCommand<OrderData>): Boolean
    
    fun upsert(command: UpdateObjectCommand<OrderData>): Boolean
    
    fun delete(command: DeleteObjectCommand): Boolean
    
    fun findById(id: String): Model<OrderData>?
    
    fun find(query: Query): SearchResult<Model<OrderData>>
}