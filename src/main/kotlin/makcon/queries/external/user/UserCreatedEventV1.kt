package makcon.queries.external.user

data class UserCreatedEventV1(
    val userId: String,
    val data: UserEventDataV1,
)