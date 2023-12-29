package makcon.queries.dto

data class ErrorV1(
    val code: String,
    val message: String,
    val attributes: Map<String, Any?> = mapOf(),
)

object ErrorCode {
    
    const val NOT_FOUND = "NotFound"
    const val UNSUPPORTED_VALUE = "UnsupportedValue"
    const val INCORRECT_MAPPING = "IncorrectMapping"
    const val INCORRECT_VALUE_TYPE = "IncorrectValueType"
}
