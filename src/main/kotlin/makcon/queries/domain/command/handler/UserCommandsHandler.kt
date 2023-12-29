package makcon.queries.domain.command.handler

import makcon.queries.domain.command.CreateObjectCommand
import makcon.queries.domain.command.DeleteObjectCommand
import makcon.queries.domain.command.UpdateObjectCommand
import makcon.queries.domain.model.UserData
import makcon.queries.domain.port.UserRepositoryPort
import org.springframework.stereotype.Component

@Component
class UserCommandsHandler(
    private val repository: UserRepositoryPort,
) {
    
    fun create(command: CreateObjectCommand<UserData>) {
        repository.create(command)
    }
    
    fun update(command: UpdateObjectCommand<UserData>) {
        repository.upsert(command)
    }
    
    fun delete(command: DeleteObjectCommand) {
        repository.delete(command)
    }
}