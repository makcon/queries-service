package makcon.queries.app.rest

import makcon.queries.adapters.exception.FieldMappingException
import makcon.queries.app.exception.IncorrectValueType
import makcon.queries.app.exception.UnsupportedValueException
import makcon.queries.domain.exception.ModelNotFoundException
import makcon.queries.dto.ErrorCode
import makcon.queries.dto.ErrorV1
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

private val log = KotlinLogging.logger {}

@RestControllerAdvice
class RestExceptionHandler {
    
    @ExceptionHandler(ModelNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handle(exception: ModelNotFoundException): ErrorV1 = createError(
        code = ErrorCode.NOT_FOUND,
        exception = exception
    )
    
    @ExceptionHandler(UnsupportedValueException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(exception: UnsupportedValueException): ErrorV1 = createError(
        code = ErrorCode.UNSUPPORTED_VALUE,
        exception = exception,
        attributes = mapOf(
            "field" to exception.field,
            "value" to exception.value
        )
    )
    
    @ExceptionHandler(FieldMappingException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handle(exception: FieldMappingException): ErrorV1 = createError(
        code = ErrorCode.INCORRECT_MAPPING,
        exception = exception,
        attributes = mapOf(
            "field" to exception.field,
        )
    )
    
    @ExceptionHandler(IncorrectValueType::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(exception: IncorrectValueType): ErrorV1 = createError(
        code = ErrorCode.INCORRECT_VALUE_TYPE,
        exception = exception,
    )
    
    private fun createError(
        code: String,
        exception: Exception,
        attributes: Map<String, Any?> = mapOf(),
    ): ErrorV1 {
        log.warn { exception.message }
        return ErrorV1(
            code = code,
            message = exception.message ?: "Unknown message",
            attributes = attributes
        )
    }
}