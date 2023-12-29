package makcon.queries.domain.command.handler

import makcon.queries.domain.command.CreateObjectCommand
import makcon.queries.domain.command.DeleteObjectCommand
import makcon.queries.domain.command.UpdateObjectCommand
import makcon.queries.domain.model.ProductData
import makcon.queries.domain.port.ProductRepositoryPort
import org.springframework.stereotype.Component

@Component
class ProductCommandsHandler(
    private val repository: ProductRepositoryPort,
) {
    
    fun create(command: CreateObjectCommand<ProductData>) {
        repository.create(command)
    }
    
    fun update(command: UpdateObjectCommand<ProductData>) {
        repository.upsert(command)
    }
    
    fun delete(command: DeleteObjectCommand) {
        repository.delete(command)
    }
}