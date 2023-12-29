package makcon.queries.adapters.query.handler

import makcon.queries.adapters.query.builder.JoinBuilder
import makcon.queries.adapters.query.model.FieldMapping
import makcon.queries.adapters.repository.table.userTable
import makcon.queries.domain.model.Model
import makcon.queries.domain.model.UserData
import makcon.queries.domain.model.constant.FilterField.*
import org.mybatis.dynamic.sql.util.spring.NamedParameterJdbcTemplateExtensions
import org.springframework.stereotype.Component

@Component
class UserQueryHandler(
    template: NamedParameterJdbcTemplateExtensions,
) : AbstractQueryHandler<Model<UserData>>(template, userTable) {
    
    private val fieldMapping = FieldMapping(
        table = userTable,
        searchFieldsMap = mapOf(
            FIRST_NAME to userTable.firstName,
            LAST_NAME to userTable.lastName,
            EMAIL to userTable.email,
        )
    )
    
    override fun getFieldMapping(): FieldMapping = fieldMapping
    
    override fun getJoinBuilders(): List<JoinBuilder> = listOf()
}