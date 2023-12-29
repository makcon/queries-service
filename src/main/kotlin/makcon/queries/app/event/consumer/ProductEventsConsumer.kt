package makcon.queries.app.event.consumer

import makcon.queries.app.event.mapper.toCommand
import makcon.queries.domain.command.handler.ProductCommandsHandler
import makcon.queries.external.event.dto.EventV1
import makcon.queries.external.product.ProductCreatedEventV1
import makcon.queries.external.product.ProductDeletedEventV1
import org.springframework.stereotype.Component
import makcon.queries.external.product.ProductUpdatedEventV1 as ProductUpdatedEventV11

@Component
class ProductEventsConsumer(
    private val handler: ProductCommandsHandler,
) {
    
    fun onCreated(event: EventV1, body: ProductCreatedEventV1) {
        handler.create(body.toCommand(event))
    }
    
    fun onUpdated(event: EventV1, body: ProductUpdatedEventV11) {
        handler.update(body.toCommand(event))
    }
    
    fun onDeleted(event: EventV1, body: ProductDeletedEventV1) {
        handler.delete(body.toCommand(event))
    }
}