package makcon.queries.app.event.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import makcon.queries.external.event.dto.EventV1
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class EventBodySerializer(private val objectMapper: ObjectMapper) {
    
    fun <T : Any> deserialize(event: EventV1, clazz: KClass<T>): T = objectMapper.convertValue(event.body, clazz.java)
}