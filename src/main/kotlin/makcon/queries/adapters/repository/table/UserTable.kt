package makcon.queries.adapters.repository.table

import makcon.queries.adapters.repository.constant.ColumnName.EMAIL
import makcon.queries.adapters.repository.constant.ColumnName.FIRST_NAME
import makcon.queries.adapters.repository.constant.ColumnName.LAST_NAME
import makcon.queries.adapters.repository.constant.TableName
import makcon.queries.adapters.repository.mapper.getInstantRequired
import makcon.queries.adapters.repository.mapper.getStrRequired
import makcon.queries.domain.model.Model
import makcon.queries.domain.model.UserData
import org.mybatis.dynamic.sql.BasicColumn
import org.mybatis.dynamic.sql.util.kotlin.elements.column
import java.sql.ResultSet

val userTable = UserTable()

class UserTable : BaseSqlTable<Model<UserData>>(TableName.USERS) {
    
    val firstName = column<String>(name = FIRST_NAME)
    val lastName = column<String>(name = LAST_NAME)
    val email = column<String>(name = EMAIL)
    
    override fun getColumns(): List<BasicColumn> = listOf(
        id,
        createdAt,
        updatedAt,
        firstName,
        lastName,
        email,
    )
    
    override fun getRowMapper(): (ResultSet, Int) -> Model<UserData> = { rs, _ ->
        Model(
            id = rs.getStrRequired(id),
            createdAt = rs.getInstantRequired(createdAt),
            updatedAt = rs.getInstantRequired(updatedAt),
            data = UserData(
                firstName = rs.getStrRequired(firstName),
                lastName = rs.getStrRequired(lastName),
                email = rs.getStrRequired(email),
            )
        )
    }
}