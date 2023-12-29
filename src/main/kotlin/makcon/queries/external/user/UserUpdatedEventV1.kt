package makcon.queries.external.user

data class UserUpdatedEventV1(
    val userId: String,
    val dataPrev: UserEventDataV1,
    val dataNext: UserEventDataV1,
)
