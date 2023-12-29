package makcon.queries.app.event.fetcher

import makcon.queries.app.event.listener.OrderEventsListener
import makcon.queries.app.event.listener.ProductEventsListener
import makcon.queries.app.event.listener.UserEventsListener
import makcon.queries.external.event.constant.Topic.ORDERS
import makcon.queries.external.event.constant.Topic.PRODUCTS
import makcon.queries.external.event.constant.Topic.USERS
import makcon.queries.external.event.dto.EventV1
import org.springframework.stereotype.Component

@Component
class EventListener(
    private val userEventListener: UserEventsListener,
    private val productEventsListener: ProductEventsListener,
    private val orderEventsListener: OrderEventsListener,
) {
    
    fun listen(topic: String, event: EventV1) {
        when (topic) {
            USERS -> userEventListener.listen(event)
            PRODUCTS -> productEventsListener.listen(event)
            ORDERS -> orderEventsListener.listen(event)
        }
    }
}