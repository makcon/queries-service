package makcon.queries.app.rest

import makcon.queries.app.mapper.toDto
import makcon.queries.app.mapper.toModel
import makcon.queries.domain.port.ProductRepositoryPort
import makcon.queries.dto.ProductV1
import makcon.queries.dto.QueryV1
import makcon.queries.dto.SearchResultV1
import org.mybatis.dynamic.sql.SqlBuilder.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductControllerV1(
    private val repository: ProductRepositoryPort,
) {
    
    @PostMapping("/v1/products")
    fun find(@RequestBody query: QueryV1): SearchResultV1<ProductV1> =
        repository.find(query.toModel()).toDto { it.toDto() }
}