package makcon.queries.app.acceptance.event

import io.kotest.matchers.shouldBe
import makcon.queries.app.acceptance.ATAbstractTest
import makcon.queries.app.event.consumer.ProductEventsConsumer
import makcon.queries.app.mother.EventV1Mother
import makcon.queries.app.mother.ProductCreatedEventV1Mother
import makcon.queries.app.mother.ProductDeletedEventV1Mother
import makcon.queries.app.mother.ProductUpdatedEventV1Mother
import makcon.queries.domain.model.Model
import makcon.queries.domain.model.ProductData
import makcon.queries.domain.model.ProductRawData
import makcon.queries.domain.port.ProductRepositoryPort
import makcon.queries.external.product.ProductEventDataV1
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ATProductEventsConsumerShould : ATAbstractTest() {
    
    @Autowired
    lateinit var consumer: ProductEventsConsumer
    
    @Autowired
    lateinit var repository: ProductRepositoryPort
    
    @Test
    fun `handle created event`() {
        // given
        val givenEventBody = ProductCreatedEventV1Mother.of()
        val givenEvent = EventV1Mother.of(body = givenEventBody)
        
        // when
        consumer.onCreated(givenEvent, givenEventBody)
        
        // then
        val product = repository.findById(givenEventBody.productId)
        product shouldBe Model(
            id = givenEventBody.productId,
            createdAt = givenEvent.createdAt,
            updatedAt = givenEvent.createdAt,
            data = buildExpectedProductData(givenEventBody.data)
        )
    }
    
    @Test
    fun `handle updated event`() {
        // given
        val storedProduct = createProduct()
        val storedProduct2 = createProduct()
        val givenEventBody = ProductUpdatedEventV1Mother.of(productId = storedProduct.id)
        val givenEvent = EventV1Mother.of(body = givenEventBody)
        
        // when
        consumer.onUpdated(givenEvent, givenEventBody)
        
        // then
        repository.findById(storedProduct2.id) shouldBe storedProduct2
        repository.findById(storedProduct.id) shouldBe Model(
            id = storedProduct.id,
            createdAt = storedProduct.createdAt,
            updatedAt = givenEvent.createdAt,
            data = buildExpectedProductData(givenEventBody.dataNext)
        )
    }
    
    @Test
    fun `not update when event time is behind`() {
        // given
        val storedProduct = createProduct()
        val givenEventBody = ProductUpdatedEventV1Mother.of(productId = storedProduct.id)
        val givenEvent = EventV1Mother.of(
            createdAt = storedProduct.createdAt.minusMillis(1),
            body = givenEventBody
        )
        
        // when
        consumer.onUpdated(givenEvent, givenEventBody)
        
        // then
        repository.findById(storedProduct.id) shouldBe storedProduct
    }
    
    @Test
    fun `handle deleted event`() {
        // given
        val storedProduct = createProduct()
        val givenEventBody = ProductDeletedEventV1Mother.of(productId = storedProduct.id)
        val givenEvent = EventV1Mother.of(body = givenEventBody)
        
        // when
        consumer.onDeleted(givenEvent, givenEventBody)
        
        // then
        repository.findById(storedProduct.id) shouldBe null
    }
    
    private fun createProduct(): Model<ProductData> {
        val givenEventBody = ProductCreatedEventV1Mother.of()
        val givenEvent = EventV1Mother.of(body = givenEventBody)
        consumer.onCreated(givenEvent, givenEventBody)
        
        return repository.findById(givenEventBody.productId)!!
    }
    
    private fun buildExpectedProductData(eventData: ProductEventDataV1) = ProductData(
        status = eventData.status,
        name = eventData.name,
        rawData = ProductRawData(
            price = eventData.price,
            params = eventData.params,
        )
    )
}