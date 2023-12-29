package makcon.queries.adapters.query.handler

import makcon.queries.adapters.query.builder.JoinBuilder
import makcon.queries.adapters.query.model.FieldMapping
import makcon.queries.adapters.repository.table.productTable
import makcon.queries.domain.model.Model
import makcon.queries.domain.model.ProductData
import makcon.queries.domain.model.constant.FilterField.NAME
import makcon.queries.domain.model.constant.FilterField.STATUS
import org.mybatis.dynamic.sql.util.spring.NamedParameterJdbcTemplateExtensions
import org.springframework.stereotype.Component

@Component
class ProductQueryHandler(
    template: NamedParameterJdbcTemplateExtensions,
) : AbstractQueryHandler<Model<ProductData>>(template, productTable) {
    
    private val fieldMapping = FieldMapping(
        table = productTable,
        searchFieldsMap = mapOf(
            NAME to productTable.name,
            STATUS to productTable.status,
        )
    )
    
    override fun getFieldMapping(): FieldMapping = fieldMapping
    
    override fun getJoinBuilders(): List<JoinBuilder> = listOf()
}