package makcon.queries.app.acceptance

import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.TestcontainersExtension

class PostgresqlTestContainer : TestcontainersExtension() {
    
    companion object {
        
        private val container = PostgreSQLContainer("postgres:16.0")
            .withDatabaseName("queries")
            .withUsername("sa")
            .withPassword("sa")
    }
    
    init {
        if (!container.isRunning) {
            container.start()
            System.setProperty("spring.datasource.url", container.jdbcUrl)
            System.setProperty("spring.datasource.username", container.username)
            System.setProperty("spring.datasource.password", container.password)
        }
    }
}