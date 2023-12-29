package makcon.queries.domain.command.handler

import makcon.queries.domain.command.CreateObjectCommand
import makcon.queries.domain.command.DeleteObjectCommand
import makcon.queries.domain.command.UpdateObjectCommand
import makcon.queries.domain.model.OrderData
import makcon.queries.domain.port.OrderRepositoryPort
import org.springframework.stereotype.Component

@Component
class OrderCommandHandler(
    private val repository: OrderRepositoryPort,
) {
    
    fun create(command: CreateObjectCommand<OrderData>) {
        repository.create(command)
    }
    
    fun update(command: UpdateObjectCommand<OrderData>) {
        repository.upsert(command)
    }
    
    fun delete(command: DeleteObjectCommand) {
        repository.delete(command)
    }
}