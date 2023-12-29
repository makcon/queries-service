package makcon.queries.app.mother

import makcon.queries.app.utils.Rand
import makcon.queries.external.order.OrderCreatedEventV1
import makcon.queries.external.order.OrderDeletedEventV1
import makcon.queries.external.order.OrderEventDataV1
import makcon.queries.external.order.OrderUpdatedEventV1

object OrderCreatedEventV1Mother {
    
    fun of(
        orderId: String = Rand.uuidStr(),
        data: OrderEventDataV1 = OrderEventDataV1Mother.of(),
    ) = OrderCreatedEventV1(
        orderId = orderId,
        data = data,
    )
}

object OrderUpdatedEventV1Mother {
    
    fun of(
        orderId: String = Rand.uuidStr(),
        dataPrev: OrderEventDataV1 = OrderEventDataV1Mother.of(),
        datNext: OrderEventDataV1 = OrderEventDataV1Mother.of(),
    ) = OrderUpdatedEventV1(
        orderId = orderId,
        dataPrev = dataPrev,
        dataNext = datNext,
    )
}

object OrderDeletedEventV1Mother {
    
    fun of(
        orderId: String = Rand.uuidStr(),
    ) = OrderDeletedEventV1(
        orderId = orderId,
    )
}

object OrderEventDataV1Mother {
    
    fun of(
        productId: String = Rand.uuidStr(),
        userId: String = Rand.uuidStr(),
        name: String = Rand.string(),
        status: String = Rand.string(),
        quantity: Int = Rand.int(),
        address: String = Rand.string(),
        comment: String = Rand.string(),
    ) = OrderEventDataV1(
        productId = productId,
        userId = userId,
        name = name,
        status = status,
        quantity = quantity,
        address = address,
        comment = comment,
    )
}