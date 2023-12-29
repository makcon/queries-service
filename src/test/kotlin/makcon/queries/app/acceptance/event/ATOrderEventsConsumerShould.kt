package makcon.queries.app.acceptance.event

import io.kotest.matchers.shouldBe
import makcon.queries.app.acceptance.ATAbstractTest
import makcon.queries.app.event.consumer.OrderEventsConsumer
import makcon.queries.app.mother.EventV1Mother
import makcon.queries.app.mother.OrderCreatedEventV1Mother
import makcon.queries.app.mother.OrderDeletedEventV1Mother
import makcon.queries.app.mother.OrderUpdatedEventV1Mother
import makcon.queries.domain.model.Model
import makcon.queries.domain.model.OrderData
import makcon.queries.domain.model.OrderRawData
import makcon.queries.domain.port.OrderRepositoryPort
import makcon.queries.external.order.OrderEventDataV1
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ATOrderEventsConsumerShould : ATAbstractTest() {
    
    @Autowired
    lateinit var consumer: OrderEventsConsumer
    
    @Autowired
    lateinit var repository: OrderRepositoryPort
    
    @Test
    fun `handle created event`() {
        // given
        val givenEventBody = OrderCreatedEventV1Mother.of()
        val givenEvent = EventV1Mother.of(body = givenEventBody)
        
        // when
        consumer.onCreated(givenEvent, givenEventBody)
        
        // then
        val order = repository.findById(givenEventBody.orderId)
        order shouldBe Model(
            id = givenEventBody.orderId,
            createdAt = givenEvent.createdAt,
            updatedAt = givenEvent.createdAt,
            data = buildExpectedOrderData(givenEventBody.data)
        )
    }
    
    @Test
    fun `handle updated event`() {
        // given
        val storedOrder = createOrder()
        val storedOrder2 = createOrder()
        val givenEventBody = OrderUpdatedEventV1Mother.of(orderId = storedOrder.id)
        val givenEvent = EventV1Mother.of(body = givenEventBody)
        
        // when
        consumer.onUpdated(givenEvent, givenEventBody)
        
        // then
        repository.findById(storedOrder2.id) shouldBe storedOrder2
        repository.findById(storedOrder.id) shouldBe Model(
            id = storedOrder.id,
            createdAt = storedOrder.createdAt,
            updatedAt = givenEvent.createdAt,
            data = buildExpectedOrderData(givenEventBody.dataNext)
        )
    }
    
    @Test
    fun `not update when event time is behind`() {
        // given
        val storedOrder = createOrder()
        val givenEventBody = OrderUpdatedEventV1Mother.of(orderId = storedOrder.id)
        val givenEvent = EventV1Mother.of(
            createdAt = storedOrder.createdAt.minusMillis(1),
            body = givenEventBody
        )
        
        // when
        consumer.onUpdated(givenEvent, givenEventBody)
        
        // then
        repository.findById(storedOrder.id) shouldBe storedOrder
    }
    
    @Test
    fun `handle deleted event`() {
        // given
        val storedOrder = createOrder()
        val givenEventBody = OrderDeletedEventV1Mother.of(orderId = storedOrder.id)
        val givenEvent = EventV1Mother.of(body = givenEventBody)
        
        // when
        consumer.onDeleted(givenEvent, givenEventBody)
        
        // then
        val order = repository.findById(storedOrder.id)
        order shouldBe null
    }
    
    private fun createOrder(): Model<OrderData> {
        val givenEventBody = OrderCreatedEventV1Mother.of()
        val givenEvent = EventV1Mother.of(body = givenEventBody)
        consumer.onCreated(givenEvent, givenEventBody)
        
        return repository.findById(givenEventBody.orderId)!!
    }
    
    private fun buildExpectedOrderData(eventData: OrderEventDataV1) = OrderData(
        productId = eventData.productId,
        userId = eventData.userId,
        status = eventData.status,
        name = eventData.name,
        rawData = OrderRawData(
            quantity = eventData.quantity,
            address = eventData.address,
            comment = eventData.comment,
        )
    )
}