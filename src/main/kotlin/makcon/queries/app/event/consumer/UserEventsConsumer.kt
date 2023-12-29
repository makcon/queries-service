package makcon.queries.app.event.consumer

import makcon.queries.app.event.mapper.toCommand
import makcon.queries.domain.command.handler.UserCommandsHandler
import makcon.queries.external.event.dto.EventV1
import makcon.queries.external.user.UserCreatedEventV1
import makcon.queries.external.user.UserDeletedEventV1
import makcon.queries.external.user.UserUpdatedEventV1
import org.springframework.stereotype.Component

@Component
class UserEventsConsumer(
    private val handler: UserCommandsHandler,
) {
    
    fun onCreated(event: EventV1, body: UserCreatedEventV1) {
        handler.create(body.toCommand(event))
    }
    
    fun onUpdated(event: EventV1, body: UserUpdatedEventV1) {
        handler.update(body.toCommand(event))
    }
    
    fun onDeleted(event: EventV1, body: UserDeletedEventV1) {
        handler.delete(body.toCommand(event))
    }
}