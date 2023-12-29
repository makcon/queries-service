package makcon.queries.adapters.repository.table

import makcon.queries.adapters.repository.constant.ColumnName.CREATED_AT
import makcon.queries.adapters.repository.constant.ColumnName.ID
import makcon.queries.adapters.repository.constant.ColumnName.UPDATED_AT
import org.mybatis.dynamic.sql.BasicColumn
import org.mybatis.dynamic.sql.SqlTable
import org.mybatis.dynamic.sql.util.kotlin.elements.column
import java.sql.ResultSet
import java.sql.Timestamp

abstract class BaseSqlTable<T>(tableName: String) : SqlTable(tableName) {
    
    val id = column<String>(name = ID)
    val createdAt = column<Timestamp>(name = CREATED_AT)
    val updatedAt = column<Timestamp>(name = UPDATED_AT)
    
    abstract fun getColumns(): List<BasicColumn>
    
    abstract fun getRowMapper(): (ResultSet, Int) -> T
}