package makcon.queries.app.rest

import makcon.queries.app.mapper.toDto
import makcon.queries.app.mapper.toModel
import makcon.queries.domain.port.OrderRepositoryPort
import makcon.queries.dto.OrderV1
import makcon.queries.dto.QueryV1
import makcon.queries.dto.SearchResultV1
import org.mybatis.dynamic.sql.SqlBuilder.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderControllerV1(
    private val repository: OrderRepositoryPort,
) {
    
    @PostMapping("/v1/orders")
    fun find(@RequestBody query: QueryV1): SearchResultV1<OrderV1> =
        repository.find(query.toModel()).toDto { it.toDto() }
}