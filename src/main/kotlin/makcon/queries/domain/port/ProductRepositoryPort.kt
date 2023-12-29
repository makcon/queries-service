package makcon.queries.domain.port

import makcon.queries.domain.command.CreateObjectCommand
import makcon.queries.domain.command.DeleteObjectCommand
import makcon.queries.domain.command.UpdateObjectCommand
import makcon.queries.domain.model.Model
import makcon.queries.domain.model.ProductData
import makcon.queries.domain.model.Query
import makcon.queries.domain.model.query.SearchResult

interface ProductRepositoryPort {
    
    fun create(command: CreateObjectCommand<ProductData>): Boolean
    
    fun upsert(command: UpdateObjectCommand<ProductData>): Boolean
    
    fun delete(command: DeleteObjectCommand): Boolean
    
    fun findById(id: String): Model<ProductData>?
    
    fun find(query: Query): SearchResult<Model<ProductData>>
}