package makcon.queries.app.mother

import makcon.queries.app.utils.Rand
import makcon.queries.domain.model.OrderData
import makcon.queries.domain.model.OrderRawData

object OrderDataMother {
    
    fun of(
        productId: String = Rand.uuidStr(),
        userId: String = Rand.uuidStr(),
        name: String = Rand.string(),
        status: String = Rand.string(),
        rawData: OrderRawData = OrderRawDataMother.of(),
    ) = OrderData(
        productId = productId,
        userId = userId,
        name = name,
        status = status,
        rawData = rawData,
    )
}

object OrderRawDataMother {
    
    fun of(
        address: String = Rand.string(),
        quantity: Int = Rand.int(),
        comment: String = Rand.string(),
    ) = OrderRawData(
        address = address,
        quantity = quantity,
        comment = comment,
    )
}