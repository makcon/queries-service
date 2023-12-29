package makcon.queries.external.product

data class ProductCreatedEventV1(
    val productId: String,
    val data: ProductEventDataV1,
)
