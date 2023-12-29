package makcon.queries.app.event.mapper

import makcon.queries.domain.command.CreateObjectCommand
import makcon.queries.domain.command.DeleteObjectCommand
import makcon.queries.domain.command.UpdateObjectCommand
import makcon.queries.domain.model.UserData
import makcon.queries.external.event.dto.EventV1
import makcon.queries.external.user.UserCreatedEventV1
import makcon.queries.external.user.UserDeletedEventV1
import makcon.queries.external.user.UserEventDataV1
import makcon.queries.external.user.UserUpdatedEventV1

fun UserCreatedEventV1.toCommand(event: EventV1) = CreateObjectCommand(
    objectId = userId,
    createdAt = event.createdAt,
    data = data.toModel(),
)

fun UserUpdatedEventV1.toCommand(event: EventV1) = UpdateObjectCommand(
    objectId = userId,
    updatedAt = event.createdAt,
    data = dataNext.toModel(),
)

fun UserDeletedEventV1.toCommand(event: EventV1) = DeleteObjectCommand(
    objectId = userId,
    deletedAt = event.createdAt,
)

private fun UserEventDataV1.toModel() = UserData(
    firstName = firstName,
    lastName = lastName,
    email = email,
)