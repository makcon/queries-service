package makcon.queries.adapters.query.join

import makcon.queries.adapters.query.builder.JoinBuilder
import makcon.queries.adapters.query.builder.join
import makcon.queries.adapters.repository.table.orderTable
import makcon.queries.adapters.repository.table.productTable
import makcon.queries.adapters.repository.table.userTable
import makcon.queries.domain.model.constant.FilterField
import makcon.queries.domain.model.constant.FilterField.*
import org.mybatis.dynamic.sql.select.AbstractQueryExpressionDSL

object OrderJoins {
    
    val toProduct = object : JoinBuilder {
        
        override fun doBuild(queryExpression: AbstractQueryExpressionDSL<*, *>) {
            queryExpression
                .join(productTable, orderTable.productId, productTable.id)
        }
        
        override fun getFields(): List<FilterField> = listOf(
            PRODUCT_ID,
            PRODUCT_STATUS,
            PRODUCT_NAME,
        )
    }
    
    val toUser = object : JoinBuilder {
        
        override fun doBuild(queryExpression: AbstractQueryExpressionDSL<*, *>) {
            queryExpression
                .join(userTable, orderTable.userId, userTable.id)
        }
        
        override fun getFields(): List<FilterField> = listOf(
            USER_ID,
            USER_FIRST_NAME,
            USER_LAST_NAME,
            USER_EMAIL,
        )
    }
}