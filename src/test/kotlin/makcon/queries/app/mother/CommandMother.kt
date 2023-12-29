package makcon.queries.app.mother

import makcon.queries.app.utils.Rand
import makcon.queries.domain.command.CreateObjectCommand
import makcon.queries.domain.command.UpdateObjectCommand
import java.time.Instant

object CreateObjectCommandMother {
    
    fun <T> of(
        objectId: String = Rand.uuidStr(),
        createdAt: Instant = Rand.instantNow(),
        data: T,
    ) = CreateObjectCommand(
        objectId = objectId,
        createdAt = createdAt,
        data = data
    )
}

object UpdateObjectCommandMother {
    
    fun <T> of(
        objectId: String = Rand.string(),
        updatedAt: Instant = Rand.instantNow(),
        data: T,
    ) = UpdateObjectCommand(
        objectId = objectId,
        updatedAt = updatedAt,
        data = data
    )
}