package makcon.queries.app.acceptance

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import makcon.queries.app.mother.CreateObjectCommandMother
import makcon.queries.app.mother.UpdateObjectCommandMother
import makcon.queries.app.mother.UserDataMother
import makcon.queries.domain.model.Model
import makcon.queries.domain.model.UserData
import makcon.queries.domain.port.UserRepositoryPort
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class ATCrudRepositoryShould : ATAbstractTest() {
    
    @Autowired
    lateinit var repository: UserRepositoryPort
    
    @Test
    fun create_new_record() {
        // given
        val givenCommand = CreateObjectCommandMother.of(data = UserDataMother.of())
        
        // when
        val created = repository.create(givenCommand)
        
        // then
        created shouldBe true
        repository.findById(givenCommand.objectId) shouldNotBe null
    }
    
    @Test
    fun not_duplicate_the_same_record() {
        // given
        val givenData = UserDataMother.of()
        val givenCommand1 = CreateObjectCommandMother.of(data = givenData)
        val givenCommand2 = CreateObjectCommandMother.of(
            objectId = givenCommand1.objectId,
            data = givenData
        )
        
        // when
        val created1 = repository.create(givenCommand1)
        val created2 = repository.create(givenCommand2)
        
        // then
        created1 shouldBe true
        created2 shouldBe false
        repository.findById(givenCommand1.objectId)?.createdAt shouldBe givenCommand1.createdAt
    }
    
    @Test
    fun update_existing_record() {
        // given
        val storedRecord = createRecord()
        val givenCommand = UpdateObjectCommandMother.of(
            objectId = storedRecord.id,
            data = UserDataMother.of()
        )
        
        // when
        val updated = repository.upsert(givenCommand)
        
        // then
        updated shouldBe true
        val updatedRecord = repository.findById(storedRecord.id)
        updatedRecord?.createdAt shouldBe storedRecord.createdAt
        updatedRecord?.updatedAt shouldBe givenCommand.updatedAt
    }
    
    @Test
    fun create_new_record_when_not_exists() {
        // given
        val givenCommand = UpdateObjectCommandMother.of(
            data = UserDataMother.of()
        )
        
        // when
        val updated = repository.upsert(givenCommand)
        
        // then
        updated shouldBe true
        repository.findById(givenCommand.objectId) shouldNotBe null
    }
    
    private fun createRecord(): Model<UserData> {
        val command = CreateObjectCommandMother.of(data = UserDataMother.of())
        repository.create(command)
        
        return repository.findById(command.objectId)!!
    }
}