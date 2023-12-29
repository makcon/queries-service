package makcon.queries.app.event.mapper

import makcon.queries.domain.command.CreateObjectCommand
import makcon.queries.domain.command.DeleteObjectCommand
import makcon.queries.domain.command.UpdateObjectCommand
import makcon.queries.domain.model.OrderData
import makcon.queries.domain.model.OrderRawData
import makcon.queries.external.event.dto.EventV1
import makcon.queries.external.order.OrderCreatedEventV1
import makcon.queries.external.order.OrderDeletedEventV1
import makcon.queries.external.order.OrderEventDataV1
import makcon.queries.external.order.OrderUpdatedEventV1

fun OrderCreatedEventV1.toCommand(event: EventV1) = CreateObjectCommand(
    objectId = orderId,
    createdAt = event.createdAt,
    data = data.toModel(),
)

fun OrderUpdatedEventV1.toCommand(event: EventV1) = UpdateObjectCommand(
    objectId = orderId,
    updatedAt = event.createdAt,
    data = dataNext.toModel(),
)

fun OrderDeletedEventV1.toCommand(event: EventV1) = DeleteObjectCommand(
    objectId = orderId,
    deletedAt = event.createdAt,
)

private fun OrderEventDataV1.toModel() = OrderData(
    productId = productId,
    userId = userId,
    name = name,
    status = status,
    rawData = OrderRawData(
        quantity = quantity,
        address = address,
        comment = comment
    ),
)