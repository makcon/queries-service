package makcon.queries.app.acceptance.search

import io.kotest.matchers.shouldBe
import makcon.queries.adapters.repository.table.userTable
import makcon.queries.app.mother.QueryFilterV1Mother
import makcon.queries.app.mother.QueryV1Mother
import makcon.queries.dto.UserV1
import makcon.queries.dto.constant.FilterFieldV1
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mybatis.dynamic.sql.util.kotlin.spring.deleteFrom

class ATUsersSearchShould : ATAbstractSearchTest<UserV1>() {
    
    @BeforeEach
    internal fun setUp() {
        createUser()
    }
    
    @AfterEach
    internal fun tearDown() {
        template.deleteFrom(userTable) {}
    }
    
    @ParameterizedTest
    @CsvSource(
        value = [
            "${FilterFieldV1.ID},id",
            "${FilterFieldV1.FIRST_NAME},firstName",
            "${FilterFieldV1.LAST_NAME},lastName",
            "${FilterFieldV1.EMAIL},email",
        ]
    )
    fun `find 1 item by user fields`(filterField: String, objectField: String) {
        // given
        val storedUser = createUser()
        
        val givenRequest = QueryV1Mother.of(
            filters = setOf(
                QueryFilterV1Mother.ofEQ(
                    field = filterField,
                    value = storedUser.getValue(objectField),
                )
            )
        )
        
        // when
        val result = searchAndGetItems(givenRequest, "users", UserV1::class.java)
        
        // then
        result.map { it.id } shouldBe setOf(storedUser.id)
    }
}