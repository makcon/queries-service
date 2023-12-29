package makcon.queries.app.mapper

import makcon.queries.domain.model.Model
import makcon.queries.domain.model.ProductData
import makcon.queries.dto.ProductV1

fun Model<ProductData>.toDto() = ProductV1(
    id = id,
    createdAt = createdAt,
    updatedAt = updatedAt,
    price = data.rawData.price,
    status = data.status,
    name = data.name,
    params = data.rawData.params,
)