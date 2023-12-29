package makcon.queries.adapters.repository.table

import makcon.queries.adapters.repository.constant.ColumnName.NAME
import makcon.queries.adapters.repository.constant.ColumnName.RAW_DATA
import makcon.queries.adapters.repository.constant.ColumnName.STATUS
import makcon.queries.adapters.repository.constant.TableName
import makcon.queries.adapters.repository.mapper.Json
import makcon.queries.adapters.repository.mapper.getInstantRequired
import makcon.queries.adapters.repository.mapper.getJsonRequired
import makcon.queries.adapters.repository.mapper.getStrRequired
import makcon.queries.domain.model.Model
import makcon.queries.domain.model.ProductData
import makcon.queries.domain.model.ProductRawData
import org.mybatis.dynamic.sql.BasicColumn
import org.mybatis.dynamic.sql.util.kotlin.elements.column
import java.sql.ResultSet

val productTable = ProductTable()

class ProductTable : BaseSqlTable<Model<ProductData>>(TableName.PRODUCTS) {
    
    val status = column<String>(name = STATUS)
    val name = column<String>(name = NAME)
    val rawData = column<ProductRawData>(
        name = RAW_DATA,
        parameterTypeConverter = { it?.let { Json.write(it) } },
    )
    
    override fun getColumns(): List<BasicColumn> = listOf(
        id,
        createdAt,
        updatedAt,
        status,
        name,
        rawData,
    )
    
    override fun getRowMapper(): (ResultSet, Int) -> Model<ProductData> = { rs, _ ->
        Model(
            id = rs.getStrRequired(id),
            createdAt = rs.getInstantRequired(createdAt),
            updatedAt = rs.getInstantRequired(updatedAt),
            data = ProductData(
                status = rs.getStrRequired(status),
                name = rs.getStrRequired(name),
                rawData = rs.getJsonRequired(rawData, ProductRawData::class.java),
            )
        )
    }
}
