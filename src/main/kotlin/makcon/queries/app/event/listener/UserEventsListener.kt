package makcon.queries.app.event.listener

import makcon.queries.app.event.consumer.EventBodySerializer
import makcon.queries.app.event.consumer.UserEventsConsumer
import makcon.queries.external.event.constant.EventAction.USER_CREATED
import makcon.queries.external.event.constant.EventAction.USER_DELETED
import makcon.queries.external.event.constant.EventAction.USER_UPDATED
import makcon.queries.external.event.dto.EventV1
import makcon.queries.external.user.UserCreatedEventV1
import makcon.queries.external.user.UserDeletedEventV1
import makcon.queries.external.user.UserUpdatedEventV1
import org.springframework.stereotype.Component

@Component
class UserEventsListener(
    private val consumer: UserEventsConsumer,
    private val serializer: EventBodySerializer,
) : EventsListener {
    
    //    @KafkaListener(topics = [USERS], groupId = "test")
    override fun listen(event: EventV1) {
        when (event.action) {
            USER_CREATED -> consumer.onCreated(event, serializer.deserialize(event, UserCreatedEventV1::class))
            USER_UPDATED -> consumer.onUpdated(event, serializer.deserialize(event, UserUpdatedEventV1::class))
            USER_DELETED -> consumer.onDeleted(event, serializer.deserialize(event, UserDeletedEventV1::class))
        }
    }
}