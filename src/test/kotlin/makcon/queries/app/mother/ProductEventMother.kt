package makcon.queries.app.mother

import makcon.queries.app.utils.Rand
import makcon.queries.external.product.ProductCreatedEventV1
import makcon.queries.external.product.ProductDeletedEventV1
import makcon.queries.external.product.ProductEventDataV1
import makcon.queries.external.product.ProductUpdatedEventV1

object ProductCreatedEventV1Mother {
    
    fun of(
        productId: String = Rand.uuidStr(),
        data: ProductEventDataV1 = ProductEventDataV1Mother.of(),
    ) = ProductCreatedEventV1(
        productId = productId,
        data = data,
    )
}

object ProductUpdatedEventV1Mother {
    
    fun of(
        productId: String = Rand.uuidStr(),
        dataPrev: ProductEventDataV1 = ProductEventDataV1Mother.of(),
        dataNext: ProductEventDataV1 = ProductEventDataV1Mother.of(),
    ) = ProductUpdatedEventV1(
        productId = productId,
        dataPrev = dataPrev,
        dataNext = dataNext,
    )
}

object ProductDeletedEventV1Mother {
    
    fun of(
        productId: String = Rand.uuidStr(),
    ) = ProductDeletedEventV1(
        productId = productId,
    )
}

object ProductEventDataV1Mother {
    
    fun of(
        name: String = Rand.string(),
        status: String = Rand.string(),
        params: Map<String, Any> = mapOf(Rand.string() to Rand.string()),
        price: Double = Rand.double(),
    ) = ProductEventDataV1(
        name = name,
        status = status,
        price = price,
        params = params,
    )
}