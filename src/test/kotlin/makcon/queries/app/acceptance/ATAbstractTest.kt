package makcon.queries.app.acceptance

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
        "spring.profiles.active=test",
    ]
)
@ExtendWith(PostgresqlTestContainer::class)
@Testcontainers
abstract class ATAbstractTest {
    
    @Autowired
    lateinit var template: NamedParameterJdbcTemplate
}