package makcon.queries.app.mapper

import io.kotest.matchers.shouldBe
import makcon.queries.app.exception.IncorrectValueType
import makcon.queries.app.utils.Rand
import makcon.queries.domain.model.constant.FilterField
import makcon.queries.domain.model.query.QueryOperator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.Instant
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class FieldValueInMapperShould {
    
    private val givenOperator = QueryOperator.IN
    
    @ParameterizedTest
    @MethodSource("getPrimitiveValueAsList")
    fun `return the same primitive value`(givenValue: Any, givenField: FilterField) {
        // when
        val value = givenValue.toValue(givenOperator, givenField)
        
        // then
        value shouldBe givenValue
    }
    
    @Test
    fun `throw exception when value is not list`() {
        // given
        val givenValue = Rand.string()
        val givenField = FilterField.NAME
        
        // when - then
        assertThrows<IncorrectValueType> { givenValue.toValue(givenOperator, givenField) }
    }
    
    @Test
    fun `throw exception when value in list not corresponding to type`() {
        // given
        val givenValue = listOf(Instant.now(), Rand.string())
        val givenField = FilterField.CREATED_AT
        
        // when - then
        assertThrows<IncorrectValueType> { givenValue.toValue(givenOperator, givenField) }
    }
    
    private fun getPrimitiveValueAsList(): Stream<Arguments> = Stream.of(
        Arguments.of(listOf("abc"), FilterField.FIRST_NAME),
        Arguments.of(listOf(Instant.now()), FilterField.CREATED_AT),
    )
}