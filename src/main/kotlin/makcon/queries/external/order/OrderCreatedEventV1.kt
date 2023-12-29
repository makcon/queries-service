package makcon.queries.external.order

data class OrderCreatedEventV1(
    val orderId: String,
    val data: OrderEventDataV1,
)
