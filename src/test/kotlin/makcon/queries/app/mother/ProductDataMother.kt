package makcon.queries.app.mother

import makcon.queries.app.utils.Rand
import makcon.queries.domain.model.ProductData
import makcon.queries.domain.model.ProductRawData

object ProductDataMother {
    
    fun of(
        status: String = Rand.string(),
        name: String = Rand.string(),
        rawData: ProductRawData = ProductRawDataMother.of(),
    ) = ProductData(
        status = status,
        name = name,
        rawData = rawData,
    )
}

object ProductRawDataMother {
    
    fun of(
        price: Double = Rand.double(),
        config: Map<String, Any> = mapOf(Rand.string() to Rand.string()),
    ) = ProductRawData(
        price = price,
        params = config,
    )
}