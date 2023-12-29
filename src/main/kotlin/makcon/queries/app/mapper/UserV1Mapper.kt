package makcon.queries.app.mapper

import makcon.queries.domain.model.Model
import makcon.queries.domain.model.UserData
import makcon.queries.dto.UserV1

fun Model<UserData>.toDto() = UserV1(
    id = id,
    createdAt = createdAt,
    updatedAt = updatedAt,
    firstName = data.firstName,
    lastName = data.lastName,
    email = data.email,
)