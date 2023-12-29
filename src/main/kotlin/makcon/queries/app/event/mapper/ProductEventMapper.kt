package makcon.queries.app.event.mapper

import makcon.queries.domain.command.CreateObjectCommand
import makcon.queries.domain.command.DeleteObjectCommand
import makcon.queries.domain.command.UpdateObjectCommand
import makcon.queries.domain.model.ProductData
import makcon.queries.domain.model.ProductRawData
import makcon.queries.external.event.dto.EventV1
import makcon.queries.external.product.ProductCreatedEventV1
import makcon.queries.external.product.ProductDeletedEventV1
import makcon.queries.external.product.ProductEventDataV1
import makcon.queries.external.product.ProductUpdatedEventV1

fun ProductCreatedEventV1.toCommand(event: EventV1) = CreateObjectCommand(
    objectId = productId,
    createdAt = event.createdAt,
    data = data.toModel(),
)

fun ProductUpdatedEventV1.toCommand(event: EventV1) = UpdateObjectCommand(
    objectId = productId,
    updatedAt = event.createdAt,
    data = dataNext.toModel(),
)

fun ProductDeletedEventV1.toCommand(event: EventV1) = DeleteObjectCommand(
    objectId = productId,
    deletedAt = event.createdAt,
)

private fun ProductEventDataV1.toModel() = ProductData(
    name = name,
    status = status,
    rawData = ProductRawData(
        price = price,
        params = params,
    ),
)