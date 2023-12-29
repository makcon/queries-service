package makcon.queries.adapters.exception

import org.mybatis.dynamic.sql.SqlColumn

class ColumnNotFoundException(columnName: SqlColumn<*>) :
    RuntimeException("Required value of '${columnName.name()}' not found in result set")