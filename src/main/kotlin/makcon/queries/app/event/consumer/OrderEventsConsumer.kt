package makcon.queries.app.event.consumer

import makcon.queries.app.event.mapper.toCommand
import makcon.queries.domain.command.handler.OrderCommandHandler
import makcon.queries.external.event.dto.EventV1
import makcon.queries.external.order.OrderCreatedEventV1
import makcon.queries.external.order.OrderDeletedEventV1
import makcon.queries.external.order.OrderUpdatedEventV1
import org.springframework.stereotype.Component

@Component
class OrderEventsConsumer(
    private val handler: OrderCommandHandler,
) {
    
    fun onCreated(event: EventV1, body: OrderCreatedEventV1) {
        handler.create(body.toCommand(event))
    }
    
    fun onUpdated(event: EventV1, body: OrderUpdatedEventV1) {
        handler.update(body.toCommand(event))
    }
    
    fun onDeleted(event: EventV1, body: OrderDeletedEventV1) {
        handler.delete(body.toCommand(event))
    }
}