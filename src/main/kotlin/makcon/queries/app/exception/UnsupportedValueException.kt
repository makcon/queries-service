package makcon.queries.app.exception

class UnsupportedValueException(
    val field: String,
    val value: Any,
    val supportedValues: Array<*>,
) : RuntimeException("Unsupported value: $value, field: $field. Possible values: ${supportedValues.joinToString()}")