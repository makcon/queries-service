package makcon.queries.app.mapper

import io.kotest.matchers.shouldBe
import makcon.queries.app.exception.IncorrectValueType
import makcon.queries.app.utils.Rand
import makcon.queries.domain.model.constant.FilterField
import makcon.queries.domain.model.query.QueryOperator
import makcon.queries.domain.model.query.Range
import makcon.queries.dto.RangeV1
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import java.time.Instant

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class FieldValueRangeMapperShould {
    
    private val givenOperator = QueryOperator.RANGE
    private val givenStringField = FilterField.NAME
    
    @Test
    fun `map value from dto object`() {
        // given
        val givenValue = RangeV1(Rand.string(), Rand.string())
        
        // when
        val value = givenValue.toValue(givenOperator, givenStringField)
        
        // then
        value shouldBe Range(
            from = givenValue.from,
            to = givenValue.to,
        )
    }
    
    @Test
    fun `map value from dto map`() {
        // given
        val givenValue = mapOf(
            "from" to Rand.string(),
            "to" to Rand.string(),
        )
        
        // when
        val value = givenValue.toValue(givenOperator, givenStringField)
        
        // then
        value shouldBe Range(
            from = givenValue["from"]!!,
            to = givenValue["to"]!!,
        )
    }
    
    @Test
    fun `throw exception when value not corresponding to type`() {
        // given
        val givenValue = Rand.string()
        
        // when - then
        assertThrows<IncorrectValueType> { givenValue.toValue(givenOperator, givenStringField) }
    }
    
    @Test
    fun `throw exception when 'from' field is not found`() {
        // given
        val givenValue = mapOf("to" to Rand.string())
        
        // when - then
        assertThrows<IncorrectValueType> { givenValue.toValue(givenOperator, givenStringField) }
    }
    
    @Test
    fun `throw exception when 'to' field is not found`() {
        // given
        val givenValue = mapOf("from" to Rand.string())
        
        // when - then
        assertThrows<IncorrectValueType> { givenValue.toValue(givenOperator, givenStringField) }
    }
    
    @Test
    fun `throw exception when 'from' field type is not correct`() {
        // given
        val givenValue = mapOf(
            "from" to Instant.now(),
            "to" to Rand.string()
        )
        
        // when - then
        assertThrows<IncorrectValueType> { givenValue.toValue(givenOperator, givenStringField) }
    }
    
    @Test
    fun `throw exception when 'to' field type is not correct`() {
        // given
        val givenValue = mapOf(
            "from" to Rand.string(),
            "to" to Instant.now()
        )
        
        // when - then
        assertThrows<IncorrectValueType> { givenValue.toValue(givenOperator, givenStringField) }
    }
}