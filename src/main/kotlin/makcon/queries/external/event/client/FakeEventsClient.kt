package makcon.queries.external.event.client

import io.github.serpro69.kfaker.Faker
import makcon.queries.external.event.constant.EventAction.ORDER_CREATED
import makcon.queries.external.event.constant.EventAction.ORDER_DELETED
import makcon.queries.external.event.constant.EventAction.ORDER_UPDATED
import makcon.queries.external.event.constant.EventAction.PRODUCT_CREATED
import makcon.queries.external.event.constant.EventAction.PRODUCT_DELETED
import makcon.queries.external.event.constant.EventAction.PRODUCT_UPDATED
import makcon.queries.external.event.constant.EventAction.USER_CREATED
import makcon.queries.external.event.constant.EventAction.USER_DELETED
import makcon.queries.external.event.constant.EventAction.USER_UPDATED
import makcon.queries.external.event.constant.Topic.ORDERS
import makcon.queries.external.event.constant.Topic.PRODUCTS
import makcon.queries.external.event.constant.Topic.USERS
import makcon.queries.external.event.dto.EventV1
import makcon.queries.external.event.dto.EventsRequestParamsV1
import makcon.queries.external.event.dto.EventsResultV1
import makcon.queries.external.order.OrderCreatedEventV1
import makcon.queries.external.order.OrderDeletedEventV1
import makcon.queries.external.order.OrderEventDataV1
import makcon.queries.external.order.OrderUpdatedEventV1
import makcon.queries.external.product.ProductCreatedEventV1
import makcon.queries.external.product.ProductDeletedEventV1
import makcon.queries.external.product.ProductEventDataV1
import makcon.queries.external.product.ProductUpdatedEventV1
import makcon.queries.external.user.UserCreatedEventV1
import makcon.queries.external.user.UserDeletedEventV1
import makcon.queries.external.user.UserEventDataV1
import makcon.queries.external.user.UserUpdatedEventV1
import java.util.*
import kotlin.random.Random

class FakeEventsClient(private val topic: String) : EventsClient {
    
    private val faker = Faker()
    
    private val userIds = (1..100).map { randomString() }
    private val productIds = (1..150).map { randomString() }
    private val orderIds = (1..300).map { randomString() }
    private val orderStatuses = listOf("CREATED", "ACCEPTED", "PAID", "DELIVERED")
    private val productStatuses = listOf("IN_STOCK", "UNAVAILABLE")
    
    override fun fetch(params: EventsRequestParamsV1): EventsResultV1 = EventsResultV1(
        totalCount = params.pageSize.toLong(),
        pageNumber = params.pageNumber,
        pageSize = params.pageSize,
        items = if (params.pageNumber > 1) listOf() else randomEvents(params)
    )
    
    private fun randomEvents(params: EventsRequestParamsV1) =
        (1..params.pageSize).map { topicEvents().random() }
    
    private fun topicEvents() = when (topic) {
        USERS -> listOf(
            EventV1(
                action = USER_CREATED,
                createdBy = randomString(),
                body = UserCreatedEventV1(
                    userId = userIds.random(),
                    data = randomUserData()
                )
            ),
            EventV1(
                action = USER_UPDATED,
                createdBy = randomString(),
                body = UserUpdatedEventV1(
                    userId = userIds.random(),
                    dataPrev = randomUserData(),
                    dataNext = randomUserData(),
                )
            ),
            EventV1(
                action = USER_DELETED,
                createdBy = randomString(),
                body = UserDeletedEventV1(
                    userId = userIds.random(),
                )
            ),
        )
        
        PRODUCTS -> listOf(
            EventV1(
                action = PRODUCT_CREATED,
                createdBy = randomString(),
                body = ProductCreatedEventV1(
                    productId = productIds.random(),
                    data = randomProductData(),
                )
            ),
            EventV1(
                action = PRODUCT_UPDATED,
                createdBy = randomString(),
                body = ProductUpdatedEventV1(
                    productId = productIds.random(),
                    dataPrev = randomProductData(),
                    dataNext = randomProductData(),
                )
            ),
            EventV1(
                action = PRODUCT_DELETED,
                createdBy = randomString(),
                body = ProductDeletedEventV1(
                    productId = productIds.random(),
                )
            ),
        )
        
        ORDERS -> listOf(
            EventV1(
                action = ORDER_CREATED,
                createdBy = randomString(),
                body = OrderCreatedEventV1(
                    orderId = orderIds.random(),
                    data = randomOrderData(),
                )
            ),
            EventV1(
                action = ORDER_UPDATED,
                createdBy = randomString(),
                body = OrderUpdatedEventV1(
                    orderId = orderIds.random(),
                    dataPrev = randomOrderData(),
                    dataNext = randomOrderData(),
                )
            ),
            EventV1(
                action = ORDER_DELETED,
                createdBy = randomString(),
                body = OrderDeletedEventV1(
                    orderId = orderIds.random(),
                )
            ),
        )
        
        else -> throw IllegalArgumentException("Unknown topic: $topic")
    }
    
    private fun randomUserData() = UserEventDataV1(
        firstName = faker.name.firstName(),
        lastName = faker.name.lastName(),
        email = faker.internet.email()
    )
    
    private fun randomProductData() = ProductEventDataV1(
        name = faker.commerce.productName(),
        status = productStatuses.random(),
        price = Random.nextDouble(),
        params = mapOf(randomString() to randomString())
    )
    
    private fun randomOrderData() = OrderEventDataV1(
        productId = productIds.random(),
        userId = userIds.random(),
        name = faker.funnyName.name(),
        status = orderStatuses.random(),
        quantity = Random.nextInt(),
        address = faker.address.fullAddress(),
        comment = faker.community.quotes(),
    )
    
    private fun randomString() = UUID.randomUUID().toString()
}