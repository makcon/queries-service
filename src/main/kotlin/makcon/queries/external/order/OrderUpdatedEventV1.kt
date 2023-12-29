package makcon.queries.external.order

data class OrderUpdatedEventV1(
    val orderId: String,
    val dataPrev: OrderEventDataV1,
    val dataNext: OrderEventDataV1,
)
