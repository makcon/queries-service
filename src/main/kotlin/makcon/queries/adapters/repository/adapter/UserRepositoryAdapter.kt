package makcon.queries.adapters.repository.adapter

import makcon.queries.adapters.query.handler.UserQueryHandler
import makcon.queries.adapters.repository.CrudRepository
import makcon.queries.adapters.repository.mapper.toTimestamp
import makcon.queries.adapters.repository.table.userTable
import makcon.queries.domain.command.CreateObjectCommand
import makcon.queries.domain.command.DeleteObjectCommand
import makcon.queries.domain.command.UpdateObjectCommand
import makcon.queries.domain.model.Model
import makcon.queries.domain.model.Query
import makcon.queries.domain.model.UserData
import makcon.queries.domain.model.query.SearchResult
import makcon.queries.domain.port.UserRepositoryPort
import org.mybatis.dynamic.sql.util.kotlin.KotlinGeneralInsertBuilder
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class UserRepositoryAdapter(
    template: NamedParameterJdbcTemplate,
    private val queryHandler: UserQueryHandler,
) : CrudRepository<Model<UserData>>(template, userTable), UserRepositoryPort {
    
    override fun create(command: CreateObjectCommand<UserData>): Boolean = create(
        completer = buildCreateCompleter(
            userId = command.objectId,
            createdAt = command.createdAt,
            updatedAt = command.createdAt,
            data = command.data
        )
    )
    
    override fun upsert(command: UpdateObjectCommand<UserData>): Boolean = upsert(
        entityId = command.objectId,
        createCompleter = buildCreateCompleter(
            userId = command.objectId,
            createdAt = command.updatedAt,
            updatedAt = command.updatedAt,
            data = command.data
        )
    ) {
        set(userTable.firstName) equalTo command.data.firstName
        set(userTable.lastName) equalTo command.data.lastName
        set(userTable.email) equalTo command.data.email
        set(userTable.updatedAt) equalTo command.updatedAt.toTimestamp()
        where {
            userTable.id isEqualTo command.objectId
            and { userTable.updatedAt isLessThan command.updatedAt.toTimestamp() }
        }
    }
    
    override fun delete(command: DeleteObjectCommand): Boolean = deleteById(
        entityId = command.objectId,
        deletedAt = command.deletedAt
    )
    
    override fun find(query: Query): SearchResult<Model<UserData>> = queryHandler.find(query)
    
    private fun buildCreateCompleter(
        userId: String,
        createdAt: Instant,
        updatedAt: Instant,
        data: UserData,
    ): KotlinGeneralInsertBuilder.() -> Unit = {
        set(userTable.id) toValue userId
        set(userTable.createdAt) toValue createdAt.toTimestamp()
        set(userTable.updatedAt) toValue updatedAt.toTimestamp()
        set(userTable.firstName) toValue data.firstName
        set(userTable.lastName) toValue data.lastName
        set(userTable.email) toValue data.email
    }
}