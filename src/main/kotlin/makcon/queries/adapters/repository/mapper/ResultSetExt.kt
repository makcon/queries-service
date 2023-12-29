package makcon.queries.adapters.repository.mapper

import makcon.queries.adapters.exception.ColumnNotFoundException
import org.mybatis.dynamic.sql.SqlColumn
import java.sql.ResultSet
import java.sql.SQLException
import java.time.Instant

fun ResultSet.getStr(columnName: SqlColumn<*>): String? = try {
    getString(findColumn(columnName.name()))
} catch (e: SQLException) {
    null
}

fun ResultSet.getStrRequired(columnName: SqlColumn<*>) = getStr(columnName)
    ?: throw ColumnNotFoundException(columnName)

fun ResultSet.getInstant(columnName: SqlColumn<*>): Instant? = try {
    getTimestamp(findColumn(columnName.name()))?.toInstant()
} catch (e: SQLException) {
    null
}

fun ResultSet.getInstantRequired(columnName: SqlColumn<*>) = getInstant(columnName)
    ?: throw ColumnNotFoundException(columnName)

fun <T> ResultSet.getJson(columnName: SqlColumn<*>, type: Class<T>): T? = try {
    Json.read(getString(findColumn(columnName.name())), type)
} catch (e: SQLException) {
    null
}

fun <T> ResultSet.getJsonRequired(columnName: SqlColumn<*>, type: Class<T>): T = getJson(columnName, type)
    ?: throw ColumnNotFoundException(columnName)

fun ResultSet.getBool(columnName: SqlColumn<*>): Boolean? = try {
    getBoolean(findColumn(columnName.name()))
} catch (e: SQLException) {
    null
}

fun ResultSet.getBoolRequired(columnName: SqlColumn<*>) = getBool(columnName)
    ?: throw ColumnNotFoundException(columnName)