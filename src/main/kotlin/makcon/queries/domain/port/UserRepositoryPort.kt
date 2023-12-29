package makcon.queries.domain.port

import makcon.queries.domain.command.CreateObjectCommand
import makcon.queries.domain.command.DeleteObjectCommand
import makcon.queries.domain.command.UpdateObjectCommand
import makcon.queries.domain.model.Model
import makcon.queries.domain.model.Query
import makcon.queries.domain.model.UserData
import makcon.queries.domain.model.query.SearchResult

interface UserRepositoryPort {
    
    fun create(command: CreateObjectCommand<UserData>): Boolean
    
    fun upsert(command: UpdateObjectCommand<UserData>): Boolean
    
    fun delete(command: DeleteObjectCommand): Boolean
    
    fun findById(id: String): Model<UserData>?
    
    fun find(query: Query): SearchResult<Model<UserData>>
}