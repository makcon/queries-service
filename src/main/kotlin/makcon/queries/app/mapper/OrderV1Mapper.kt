package makcon.queries.app.mapper

import makcon.queries.domain.model.Model
import makcon.queries.domain.model.OrderData
import makcon.queries.dto.OrderV1

fun Model<OrderData>.toDto() = OrderV1(
    id = id,
    createdAt = createdAt,
    updatedAt = updatedAt,
    userId = data.userId,
    productId = data.productId,
    name = data.name,
    status = data.status,
    address = data.rawData.address,
    comment = data.rawData.comment,
    quantity = data.rawData.quantity,
)