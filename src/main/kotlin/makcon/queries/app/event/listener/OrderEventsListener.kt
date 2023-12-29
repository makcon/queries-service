package makcon.queries.app.event.listener

import makcon.queries.app.event.consumer.EventBodySerializer
import makcon.queries.app.event.consumer.OrderEventsConsumer
import makcon.queries.external.event.constant.EventAction.ORDER_CREATED
import makcon.queries.external.event.constant.EventAction.ORDER_DELETED
import makcon.queries.external.event.constant.EventAction.ORDER_UPDATED
import makcon.queries.external.event.dto.EventV1
import makcon.queries.external.order.OrderCreatedEventV1
import makcon.queries.external.order.OrderDeletedEventV1
import makcon.queries.external.order.OrderUpdatedEventV1
import org.springframework.stereotype.Component

@Component
class OrderEventsListener(
    private val consumer: OrderEventsConsumer,
    private val serializer: EventBodySerializer,
) : EventsListener {
    
    //    @KafkaListener(topics = [ORDERS], groupId = "test")
    override fun listen(event: EventV1) {
        when (event.action) {
            ORDER_CREATED -> consumer.onCreated(event, serializer.deserialize(event, OrderCreatedEventV1::class))
            ORDER_UPDATED -> consumer.onUpdated(event, serializer.deserialize(event, OrderUpdatedEventV1::class))
            ORDER_DELETED -> consumer.onDeleted(event, serializer.deserialize(event, OrderDeletedEventV1::class))
        }
    }
}