package makcon.queries.app.acceptance.search

import io.kotest.matchers.shouldBe
import makcon.queries.adapters.repository.table.userTable
import makcon.queries.app.mother.*
import makcon.queries.app.utils.Rand
import makcon.queries.dto.*
import makcon.queries.dto.constant.FilterFieldV1
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mybatis.dynamic.sql.util.kotlin.spring.deleteFrom

class ATGeneralSearchShould : ATAbstractSearchTest<UserV1>() {
    
    @AfterEach
    internal fun tearDown() {
        template.deleteFrom(userTable) {}
    }
    
    @Test
    fun `find 2 items without filters`() {
        // given
        val storedObject = createUser()
        val storedObject2 = createUser()
        
        val givenRequest = QueryV1Mother.of(filters = setOf())
        
        // when
        val result = searchAndGetIds(givenRequest)
        
        // then
        result shouldBe setOf(storedObject.id, storedObject2.id)
    }
    
    @Test
    fun `find one item by 1 filter using EQ operator`() {
        // given
        val storedObject = createUser()
        createUser()
        
        val givenRequest = QueryV1Mother.of(
            filters = setOf(
                QueryFilterV1Mother.ofEQ(
                    field = FilterFieldV1.FIRST_NAME,
                    value = storedObject.firstName,
                )
            )
        )
        
        // when
        val result = searchAndGetIds(givenRequest)
        
        // then
        result shouldBe setOf(storedObject.id)
    }
    
    @Test
    fun `find items by 1 filter using IN operator`() {
        // given
        val storedObject = createUser()
        val storedObject2 = createUser()
        createUser()
        
        val givenRequest = QueryV1Mother.of(
            filters = setOf(
                QueryFilterV1Mother.of(
                    field = FilterFieldV1.FIRST_NAME,
                    value = listOf(storedObject.firstName, storedObject2.firstName),
                    operator = QueryOperatorV1.IN,
                )
            )
        )
        
        // when
        val result = searchAndGetIds(givenRequest)
        
        // then
        result shouldBe setOf(storedObject.id, storedObject2.id)
    }
    
    @Test
    fun `find items by 1 filter using RANGE operator`() {
        // given
        val storedObject = createUser(data = UserDataMother.of(firstName = "1"))
        val storedObject2 = createUser(data = UserDataMother.of(firstName = "2"))
        createUser(data = UserDataMother.of(firstName = "3"))
        
        val givenRequest = QueryV1Mother.of(
            filters = setOf(
                QueryFilterV1Mother.of(
                    field = FilterFieldV1.FIRST_NAME,
                    value = RangeV1("1", "2"),
                    operator = QueryOperatorV1.RANGE,
                )
            )
        )
        
        // when
        val result = searchAndGetIds(givenRequest)
        
        // then
        result shouldBe setOf(storedObject.id, storedObject2.id)
    }
    
    @Test
    fun `find one item by 1 filter using START_WITH operator`() {
        // given
        val storedObject = createUser(data = UserDataMother.of(firstName = "abc"))
        createUser(data = UserDataMother.of(firstName = "xyz"))
        
        val givenRequest = QueryV1Mother.of(
            filters = setOf(
                QueryFilterV1Mother.of(
                    field = FilterFieldV1.FIRST_NAME,
                    value = "ab",
                    operator = QueryOperatorV1.START_WITH
                )
            )
        )
        
        // when
        val result = searchAndGetIds(givenRequest)
        
        // then
        result shouldBe setOf(storedObject.id)
    }
    
    @Test
    fun `find one item by 1 filter using CONTAINS operator`() {
        // given
        val storedObject = createUser(data = UserDataMother.of(firstName = "abcd"))
        createUser(data = UserDataMother.of(firstName = "xyz"))
        
        val givenRequest = QueryV1Mother.of(
            filters = setOf(
                QueryFilterV1Mother.of(
                    field = FilterFieldV1.FIRST_NAME,
                    value = "bc",
                    operator = QueryOperatorV1.CONTAINS
                )
            )
        )
        
        // when
        val result = searchAndGetIds(givenRequest)
        
        // then
        result shouldBe setOf(storedObject.id)
    }
    
    @Test
    fun `find one item by 2 filters using AND connector`() {
        // given
        val givenName = Rand.string()
        val storedObject = createUser(data = UserDataMother.of(lastName = givenName))
        createUser(data = UserDataMother.of(lastName = givenName))
        createUser()
        
        val givenRequest = QueryV1Mother.of(
            filters = setOf(
                QueryFilterV1Mother.ofEQ(
                    field = FilterFieldV1.NAME,
                    value = givenName,
                ),
                QueryFilterV1Mother.ofEQ(
                    field = FilterFieldV1.ID,
                    value = storedObject.id,
                ),
            )
        )
        
        // when
        val result = searchAndGetIds(givenRequest)
        
        // then
        result shouldBe setOf(storedObject.id)
    }
    
    @Test
    fun `find one item by 2 filters using OR connector`() {
        // given
        val storedObject = createUser()
        val storedObject2 = createUser()
        createUser()
        
        val givenRequest = QueryV1Mother.of(
            filters = setOf(
                QueryFilterV1Mother.ofEQ(
                    field = FilterFieldV1.FIRST_NAME,
                    value = storedObject.firstName,
                ),
                QueryFilterV1Mother.ofEQ(
                    field = FilterFieldV1.ID,
                    value = storedObject2.id,
                ),
            ),
            connector = QueryConnectorV1.OR,
        )
        
        // when
        val result = searchAndGetIds(givenRequest)
        
        // then
        result shouldBe setOf(storedObject.id, storedObject2.id)
    }
    
    @Test
    fun `return first page`() {
        // given
        createUser()
        createUser()
        createUser()
        
        val givenRequest = QueryV1Mother.of(
            filters = setOf(),
            paging = PagingV1Mother.of(number = 0, size = 2)
        )
        
        // when
        val result = searchAndGetResponse(givenRequest, "users", UserV1::class.java)
        
        // then
        result.totalCount shouldBe 3
        result.items?.size shouldBe 2
    }
    
    @Test
    fun `return second page`() {
        // given
        createUser()
        createUser()
        createUser()
        
        val givenRequest = QueryV1Mother.of(
            filters = setOf(),
            paging = PagingV1Mother.of(number = 1, size = 2)
        )
        
        // when
        val result = searchAndGetResponse(givenRequest, "users", UserV1::class.java)
        
        // then
        result.totalCount shouldBe 3
        result.items?.size shouldBe 1
    }
    
    @Test
    fun `return sorted ASC result`() {
        // given
        val storedObject = createUser(data = UserDataMother.of(firstName = "c"))
        val storedObject2 = createUser(data = UserDataMother.of(firstName = "a"))
        val storedObject3 = createUser(data = UserDataMother.of(firstName = "b"))
        
        val givenRequest = QueryV1Mother.of(
            filters = setOf(),
            sorting = SortingV1Mother.of(
                field = FilterFieldV1.FIRST_NAME,
                dir = SortDirV1.ASC
            )
        )
        
        // when
        val result = searchAndGetItems(givenRequest, "users", UserV1::class.java).map { it.id }
        
        // then
        result shouldBe listOf(storedObject2.id, storedObject3.id, storedObject.id)
    }
    
    @Test
    fun `return sorted DESC result`() {
        // given
        val storedObject = createUser(data = UserDataMother.of(firstName = "c"))
        val storedObject2 = createUser(data = UserDataMother.of(firstName = "a"))
        val storedObject3 = createUser(data = UserDataMother.of(firstName = "b"))
        
        val givenRequest = QueryV1Mother.of(
            filters = setOf(),
            sorting = SortingV1Mother.of(
                field = FilterFieldV1.FIRST_NAME,
                dir = SortDirV1.DESC
            )
        )
        
        // when
        val result = searchAndGetItems(givenRequest, "users", UserV1::class.java).map { it.id }
        
        // then
        result shouldBe listOf(storedObject.id, storedObject3.id, storedObject2.id)
    }
    
    @Test
    fun `return sorted result by common createdAt field`() {
        // given
        val storedObject = createUser()
        val storedObject2 = createUser()
        val storedObject3 = createUser()
        
        val givenRequest = QueryV1Mother.of(
            filters = setOf(),
            sorting = SortingV1Mother.of(
                field = FilterFieldV1.CREATED_AT,
                dir = SortDirV1.ASC
            )
        )
        
        // when
        val result = searchAndGetItems(givenRequest, "users", UserV1::class.java).map { it.id }
        
        // then
        result shouldBe listOf(storedObject.id, storedObject2.id, storedObject3.id)
    }
    
    @Test
    fun `return count only`() {
        // given
        createUser()
        
        val givenRequest = QueryV1Mother.of(
            filters = setOf(),
            resultFields = setOf(ResultFieldV1.COUNT)
        )
        
        // when
        val result = searchAndGetResponse(givenRequest, "users", UserV1::class.java)
        
        // then
        result shouldBe SearchResultV1(
            totalCount = 1,
            items = null,
        )
    }
    
    @Test
    fun `return items only`() {
        // given
        val storedObject = createUser()
        
        val givenRequest = QueryV1Mother.of(
            filters = setOf(),
            resultFields = setOf(ResultFieldV1.ITEMS)
        )
        
        // when
        val result = searchAndGetResponse(givenRequest, "users", UserV1::class.java)
        
        // then
        result shouldBe SearchResultV1(
            totalCount = null,
            items = listOf(storedObject),
        )
    }
    
    private fun searchAndGetIds(request: QueryV1) = searchAndGetItems(request, "users", UserV1::class.java)
        .map { it.id }
        .toSet()
}