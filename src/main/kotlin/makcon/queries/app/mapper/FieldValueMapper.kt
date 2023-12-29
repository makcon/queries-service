package makcon.queries.app.mapper

import makcon.queries.app.exception.IncorrectValueType
import makcon.queries.domain.model.constant.FilterField
import makcon.queries.domain.model.query.QueryOperator
import makcon.queries.domain.model.query.Range
import makcon.queries.dto.RangeV1

fun Any.toValue(operator: QueryOperator, field: FilterField): Any = when (operator) {
    QueryOperator.RANGE -> mapRange(field)
    QueryOperator.IN -> mapIn(field)
    else -> checkAndGetValue(this, field)
}

private fun Any.mapRange(field: FilterField): Range {
    if (this is RangeV1) {
        return Range(
            from = checkAndGetValue(from, field),
            to = checkAndGetValue(to, field),
        )
    }
    
    if (this !is Map<*, *>) throw IncorrectValueType("Incorrect value for RANGE operator")
    
    val from = this[RangeV1::from.name] ?: throw IncorrectValueType("Field 'from' must be present for RANGE value")
    val to = this[RangeV1::to.name] ?: throw IncorrectValueType("Field 'to' must be present for RANGE value")
    
    return Range(
        from = checkAndGetValue(from, field),
        to = checkAndGetValue(to, field)
    )
}

fun Any.mapIn(field: FilterField): Any {
    if (this !is Collection<*>) throw IncorrectValueType("Value must be a collection for IN operator")
    
    return map { checkAndGetValue(it!!, field) }
}

private fun checkAndGetValue(value: Any, field: FilterField): Any {
    if (field.type.isInstance(value)) return value
    
    throw IncorrectValueType("Expected type: ${field.type.simpleName} for field: $field")
}