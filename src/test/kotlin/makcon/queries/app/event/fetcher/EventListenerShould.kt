package makcon.queries.app.event.fetcher

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import makcon.queries.app.event.listener.OrderEventsListener
import makcon.queries.app.event.listener.ProductEventsListener
import makcon.queries.app.event.listener.UserEventsListener
import makcon.queries.app.mother.EventV1Mother
import makcon.queries.external.event.constant.Topic.ORDERS
import makcon.queries.external.event.constant.Topic.PRODUCTS
import makcon.queries.external.event.constant.Topic.USERS
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class EventListenerShould {
    
    @InjectMockKs
    lateinit var listener: EventListener
    
    @RelaxedMockK
    lateinit var userEventsListener: UserEventsListener
    
    @RelaxedMockK
    lateinit var orderEventsListener: OrderEventsListener
    
    @RelaxedMockK
    lateinit var productEventsListener: ProductEventsListener
    
    private val givenEvent = EventV1Mother.of()
    
    @Test
    fun `call users listener`() {
        // when
        listener.listen(USERS, givenEvent)
        
        // then
        verify { userEventsListener.listen(givenEvent) }
    }
    
    @Test
    fun `call orders listener`() {
        // when
        listener.listen(ORDERS, givenEvent)
        
        // then
        verify { orderEventsListener.listen(givenEvent) }
    }
    
    @Test
    fun `call products listener`() {
        // when
        listener.listen(PRODUCTS, givenEvent)
        
        // then
        verify { productEventsListener.listen(givenEvent) }
    }
}