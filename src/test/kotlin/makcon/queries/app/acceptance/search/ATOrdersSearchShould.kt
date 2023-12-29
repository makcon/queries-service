package makcon.queries.app.acceptance.search

import io.kotest.matchers.shouldBe
import makcon.queries.adapters.repository.table.orderTable
import makcon.queries.adapters.repository.table.productTable
import makcon.queries.adapters.repository.table.userTable
import makcon.queries.app.mother.QueryFilterV1Mother
import makcon.queries.app.mother.QueryV1Mother
import makcon.queries.dto.OrderV1
import makcon.queries.dto.constant.FilterFieldV1
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mybatis.dynamic.sql.util.kotlin.spring.deleteFrom

class ATOrdersSearchShould : ATAbstractSearchTest<OrderV1>() {
    
    @BeforeEach
    internal fun setUp() {
        createOrder()
    }
    
    @AfterEach
    internal fun tearDown() {
        template.deleteFrom(userTable) {}
        template.deleteFrom(orderTable) {}
        template.deleteFrom(productTable) {}
    }
    
    @ParameterizedTest
    @CsvSource(
        value = [
            "${FilterFieldV1.USER_ID},id",
            "${FilterFieldV1.USER_FIRST_NAME},firstName",
            "${FilterFieldV1.USER_LAST_NAME},lastName",
            "${FilterFieldV1.USER_EMAIL},email",
        ]
    )
    fun `find 1 item by user fields`(filterField: String, objectField: String) {
        // given
        val storedOrder = createOrder()
        
        val givenRequest = QueryV1Mother.of(
            filters = setOf(
                QueryFilterV1Mother.ofEQ(
                    field = filterField,
                    value = findUser(storedOrder.userId).getValue(objectField),
                )
            )
        )
        
        // when
        val result = searchAndGetItems(givenRequest, "orders", OrderV1::class.java)
        
        // then
        result.map { it.id } shouldBe setOf(storedOrder.id)
    }
    
    @ParameterizedTest
    @CsvSource(
        value = [
            "${FilterFieldV1.PRODUCT_NAME},name",
            "${FilterFieldV1.PRODUCT_STATUS},status",
        ]
    )
    fun `find 1 item by product fields`(filterField: String, objectField: String) {
        // given
        val storedOrder = createOrder()
        
        val givenRequest = QueryV1Mother.of(
            filters = setOf(
                QueryFilterV1Mother.ofEQ(
                    field = filterField,
                    value = findProduct(storedOrder.productId).getValue(objectField),
                )
            )
        )
        
        // when
        val result = searchAndGetItems(givenRequest, "orders", OrderV1::class.java)
        
        // then
        result.map { it.id } shouldBe setOf(storedOrder.id)
    }
    
    @ParameterizedTest
    @CsvSource(
        value = [
            "${FilterFieldV1.ID},id",
            "${FilterFieldV1.NAME},name",
            "${FilterFieldV1.STATUS},status",
        ]
    )
    fun `find 1 item by order fields`(filterField: String, objectField: String) {
        // given
        val storedOrder = createOrder()
        
        val givenRequest = QueryV1Mother.of(
            filters = setOf(
                QueryFilterV1Mother.ofEQ(
                    field = filterField,
                    value = storedOrder.getValue(objectField),
                )
            )
        )
        
        // when
        val result = searchAndGetItems(givenRequest, "orders", OrderV1::class.java)
        
        // then
        result.map { it.id } shouldBe setOf(storedOrder.id)
    }
}