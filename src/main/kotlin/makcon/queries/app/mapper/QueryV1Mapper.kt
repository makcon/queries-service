package makcon.queries.app.mapper

import makcon.queries.app.exception.UnsupportedValueException
import makcon.queries.domain.model.Query
import makcon.queries.domain.model.QueryConnector
import makcon.queries.domain.model.ResultField
import makcon.queries.domain.model.constant.FilterField
import makcon.queries.domain.model.query.*
import makcon.queries.dto.PagingV1
import makcon.queries.dto.QueryFilterV1
import makcon.queries.dto.QueryV1
import makcon.queries.dto.SortingV1
import kotlin.reflect.KProperty1

fun QueryV1.toModel() = Query(
    filters = filters.map { it.toModel() },
    paging = paging.toModel(),
    sorting = sorting?.toModel(),
    resultFields = resultFields.map { mapResultField(it) }.toSet(),
    connector = mapConnector(connector)
)

fun QueryFilterV1.toModel(): QueryFilter {
    val fieldModel = mapField(field, QueryFilterV1::field)
    val operatorModel = mapOperator(operator)
    
    return QueryFilter(
        field = fieldModel,
        operator = operatorModel,
        value = value.toValue(operatorModel, fieldModel)
    )
}

fun PagingV1.toModel() = Paging(
    number = number,
    size = size
)

fun SortingV1.toModel() = Sorting(
    field = mapField(field, SortingV1::field),
    dir = mapDir(dir),
)

private fun mapField(value: String, field: KProperty1<*, String>): FilterField = try {
    FilterField.valueOf(value)
} catch (e: IllegalArgumentException) {
    throw UnsupportedValueException(
        field = field.name,
        value = value,
        supportedValues = FilterField.values()
    )
}

private fun mapOperator(value: String): QueryOperator = try {
    QueryOperator.valueOf(value)
} catch (e: IllegalArgumentException) {
    throw UnsupportedValueException(
        field = QueryFilterV1::operator.name,
        value = value,
        supportedValues = QueryOperator.values()
    )
}

private fun mapConnector(value: String): QueryConnector = try {
    QueryConnector.valueOf(value)
} catch (e: IllegalArgumentException) {
    throw UnsupportedValueException(
        field = QueryV1::connector.name,
        value = value,
        supportedValues = QueryConnector.values()
    )
}

private fun mapDir(value: String): SortDir = try {
    SortDir.valueOf(value)
} catch (e: IllegalArgumentException) {
    throw UnsupportedValueException(
        field = SortingV1::dir.name,
        value = value,
        supportedValues = SortDir.values()
    )
}

private fun mapResultField(value: String): ResultField = try {
    ResultField.valueOf(value)
} catch (e: IllegalArgumentException) {
    throw UnsupportedValueException(
        field = QueryV1::resultFields.name,
        value = value,
        supportedValues = ResultField.values()
    )
}