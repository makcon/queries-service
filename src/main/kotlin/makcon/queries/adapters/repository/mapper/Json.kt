package makcon.queries.adapters.repository.mapper

import com.fasterxml.jackson.databind.ObjectMapper

private val objectMapper = ObjectMapper().findAndRegisterModules()

object Json {
    
    fun write(model: Any): String = objectMapper.writeValueAsString(model)
    
    fun <T> read(str: String, type: Class<T>): T = objectMapper.readValue(str, type)
}