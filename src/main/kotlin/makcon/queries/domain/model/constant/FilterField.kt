package makcon.queries.domain.model.constant

import java.time.Instant
import kotlin.reflect.KClass

enum class FilterField(val type: KClass<*>) {
    ID(String::class),
    CREATED_AT(Instant::class),
    UPDATED_AT(Instant::class),
    PRODUCT_ID(String::class),
    PRODUCT_NAME(String::class),
    PRODUCT_STATUS(String::class),
    STATUS(String::class),
    NAME(String::class),
    FIRST_NAME(String::class),
    LAST_NAME(String::class),
    EMAIL(String::class),
    USER_ID(String::class),
    USER_FIRST_NAME(String::class),
    USER_LAST_NAME(String::class),
    USER_EMAIL(String::class),
}