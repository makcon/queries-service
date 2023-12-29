package makcon.queries.adapters.repository.table

import makcon.queries.adapters.repository.constant.ColumnName.NAME
import makcon.queries.adapters.repository.constant.ColumnName.PRODUCT_ID
import makcon.queries.adapters.repository.constant.ColumnName.RAW_DATA
import makcon.queries.adapters.repository.constant.ColumnName.STATUS
import makcon.queries.adapters.repository.constant.ColumnName.USER_ID
import makcon.queries.adapters.repository.constant.TableName
import makcon.queries.adapters.repository.mapper.Json
import makcon.queries.adapters.repository.mapper.getInstantRequired
import makcon.queries.adapters.repository.mapper.getJsonRequired
import makcon.queries.adapters.repository.mapper.getStrRequired
import makcon.queries.domain.model.Model
import makcon.queries.domain.model.OrderData
import makcon.queries.domain.model.OrderRawData
import org.mybatis.dynamic.sql.BasicColumn
import org.mybatis.dynamic.sql.util.kotlin.elements.column
import java.sql.ResultSet

val orderTable = OrderTable()

class OrderTable : BaseSqlTable<Model<OrderData>>(TableName.ORDERS) {
    
    val productId = column<String>(name = PRODUCT_ID)
    val userId = column<String>(name = USER_ID)
    val status = column<String>(name = STATUS)
    val name = column<String>(name = NAME)
    val rawData = column<OrderRawData>(
        name = RAW_DATA,
        parameterTypeConverter = { it?.let { Json.write(it) } },
    )
    
    override fun getColumns(): List<BasicColumn> = listOf(
        id,
        createdAt,
        updatedAt,
        productId,
        userId,
        status,
        name,
        rawData,
    )
    
    override fun getRowMapper(): (ResultSet, Int) -> Model<OrderData> = { rs, _ ->
        Model(
            id = rs.getStrRequired(id),
            createdAt = rs.getInstantRequired(createdAt),
            updatedAt = rs.getInstantRequired(updatedAt),
            data = OrderData(
                productId = rs.getStrRequired(productId),
                userId = rs.getStrRequired(userId),
                status = rs.getStrRequired(status),
                name = rs.getStrRequired(name),
                rawData = rs.getJsonRequired(rawData, OrderRawData::class.java),
            )
        )
    }
}