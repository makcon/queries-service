package makcon.queries.adapters.query.handler

import makcon.queries.adapters.query.builder.JoinBuilder
import makcon.queries.adapters.query.join.OrderJoins.toProduct
import makcon.queries.adapters.query.join.OrderJoins.toUser
import makcon.queries.adapters.query.model.FieldMapping
import makcon.queries.adapters.repository.table.orderTable
import makcon.queries.adapters.repository.table.productTable
import makcon.queries.adapters.repository.table.userTable
import makcon.queries.domain.model.Model
import makcon.queries.domain.model.OrderData
import makcon.queries.domain.model.constant.FilterField.*
import org.mybatis.dynamic.sql.util.spring.NamedParameterJdbcTemplateExtensions
import org.springframework.stereotype.Component

@Component
class OrderQueryHandler(
    template: NamedParameterJdbcTemplateExtensions,
) : AbstractQueryHandler<Model<OrderData>>(template, orderTable) {
    
    private val fieldMapping = FieldMapping(
        table = orderTable,
        searchFieldsMap = mapOf(
            NAME to orderTable.name,
            STATUS to orderTable.status,
            PRODUCT_ID to orderTable.productId,
            USER_ID to orderTable.userId,
            PRODUCT_NAME to productTable.name,
            PRODUCT_STATUS to productTable.status,
            USER_FIRST_NAME to userTable.firstName,
            USER_LAST_NAME to userTable.lastName,
            USER_EMAIL to userTable.email,
        )
    )
    
    override fun getFieldMapping(): FieldMapping = fieldMapping
    
    override fun getJoinBuilders(): List<JoinBuilder> = listOf(toProduct, toUser)
}