package makcon.queries.app.acceptance.event

import io.kotest.matchers.shouldBe
import makcon.queries.app.acceptance.ATAbstractTest
import makcon.queries.app.event.consumer.UserEventsConsumer
import makcon.queries.app.mother.EventV1Mother
import makcon.queries.app.mother.UserCreatedEventMother
import makcon.queries.app.mother.UserDeletedEventMother
import makcon.queries.app.mother.UserUpdatedEventMother
import makcon.queries.domain.model.Model
import makcon.queries.domain.model.UserData
import makcon.queries.domain.port.UserRepositoryPort
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ATUserEventsConsumerShould : ATAbstractTest() {
    
    @Autowired
    lateinit var consumer: UserEventsConsumer
    
    @Autowired
    lateinit var repository: UserRepositoryPort
    
    @Test
    fun `handle created event`() {
        // given
        val givenEventBody = UserCreatedEventMother.of()
        val givenEvent = EventV1Mother.of(body = givenEventBody)
        
        // when
        consumer.onCreated(givenEvent, givenEventBody)
        
        // then
        repository.findById(givenEventBody.userId) shouldBe Model(
            id = givenEventBody.userId,
            createdAt = givenEvent.createdAt,
            updatedAt = givenEvent.createdAt,
            data = UserData(
                firstName = givenEventBody.data.firstName,
                lastName = givenEventBody.data.lastName,
                email = givenEventBody.data.email,
            )
        )
    }
    
    @Test
    fun `handle updated event`() {
        // given
        val storedUser = createUser()
        val storedUser2 = createUser()
        val givenEventBody = UserUpdatedEventMother.of(userId = storedUser.id)
        val givenEvent = EventV1Mother.of(body = givenEventBody)
        
        // when
        consumer.onUpdated(givenEvent, givenEventBody)
        
        // then
        repository.findById(storedUser2.id) shouldBe storedUser2
        repository.findById(storedUser.id) shouldBe Model(
            id = storedUser.id,
            createdAt = storedUser.createdAt,
            updatedAt = givenEvent.createdAt,
            data = UserData(
                firstName = givenEventBody.dataNext.firstName,
                lastName = givenEventBody.dataNext.lastName,
                email = givenEventBody.dataNext.email,
            )
        )
    }
    
    @Test
    fun `not update when event time is behind`() {
        // given
        val storedUser = createUser()
        val givenEventBody = UserUpdatedEventMother.of(userId = storedUser.id)
        val givenEvent = EventV1Mother.of(
            createdAt = storedUser.createdAt.minusMillis(1),
            body = givenEventBody
        )
        
        // when
        consumer.onUpdated(givenEvent, givenEventBody)
        
        // then
        repository.findById(storedUser.id) shouldBe storedUser
    }
    
    @Test
    fun `handle deleted event`() {
        // given
        val storedUser = createUser()
        val givenEventBody = UserDeletedEventMother.of(userId = storedUser.id)
        val givenEvent = EventV1Mother.of(body = givenEventBody)
        
        // when
        consumer.onDeleted(givenEvent, givenEventBody)
        
        // then
        repository.findById(storedUser.id) shouldBe null
    }
    
    private fun createUser(): Model<UserData> {
        val givenEventBody = UserCreatedEventMother.of()
        val givenEvent = EventV1Mother.of(body = givenEventBody)
        consumer.onCreated(givenEvent, givenEventBody)
        
        return repository.findById(givenEventBody.userId)!!
    }
}