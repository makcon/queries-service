package makcon.queries.app.acceptance.search

import io.kotest.matchers.shouldBe
import makcon.queries.adapters.repository.table.productTable
import makcon.queries.app.mother.QueryFilterV1Mother
import makcon.queries.app.mother.QueryV1Mother
import makcon.queries.dto.ProductV1
import makcon.queries.dto.constant.FilterFieldV1
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mybatis.dynamic.sql.util.kotlin.spring.deleteFrom

class ATProductsSearchShould : ATAbstractSearchTest<ProductV1>() {
    
    @BeforeEach
    internal fun setUp() {
        createProduct()
    }
    
    @AfterEach
    internal fun tearDown() {
        template.deleteFrom(productTable) {}
    }
    
    @ParameterizedTest
    @CsvSource(
        value = [
            "${FilterFieldV1.ID},id",
            "${FilterFieldV1.NAME},name",
            "${FilterFieldV1.STATUS},status",
        ]
    )
    fun `find 1 item by product fields`(filterField: String, objectField: String) {
        // given
        val storedPG = createProduct()
        
        val givenRequest = QueryV1Mother.of(
            filters = setOf(
                QueryFilterV1Mother.ofEQ(
                    field = filterField,
                    value = storedPG.getValue(objectField),
                )
            )
        )
        
        // when
        val result = searchAndGetItems(givenRequest, "products", ProductV1::class.java)
        
        // then
        result.map { it.id } shouldBe setOf(storedPG.id)
    }
}