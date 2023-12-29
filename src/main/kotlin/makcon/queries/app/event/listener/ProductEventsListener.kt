package makcon.queries.app.event.listener

import makcon.queries.app.event.consumer.EventBodySerializer
import makcon.queries.app.event.consumer.ProductEventsConsumer
import makcon.queries.external.event.constant.EventAction.PRODUCT_CREATED
import makcon.queries.external.event.constant.EventAction.PRODUCT_DELETED
import makcon.queries.external.event.constant.EventAction.PRODUCT_UPDATED
import makcon.queries.external.event.dto.EventV1
import makcon.queries.external.product.ProductCreatedEventV1
import makcon.queries.external.product.ProductDeletedEventV1
import makcon.queries.external.product.ProductUpdatedEventV1
import org.springframework.stereotype.Component

@Component
class ProductEventsListener(
    private val consumer: ProductEventsConsumer,
    private val serializer: EventBodySerializer,
) : EventsListener {
    
    //    @KafkaListener(topics = [PRODUCTS], groupId = "test")
    override fun listen(event: EventV1) {
        when (event.action) {
            PRODUCT_CREATED -> consumer.onCreated(event, serializer.deserialize(event, ProductCreatedEventV1::class))
            PRODUCT_UPDATED -> consumer.onUpdated(event, serializer.deserialize(event, ProductUpdatedEventV1::class))
            PRODUCT_DELETED -> consumer.onDeleted(event, serializer.deserialize(event, ProductDeletedEventV1::class))
        }
    }
}