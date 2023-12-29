package makcon.queries.app.config

import net.javacrumbs.shedlock.core.LockProvider
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock
import net.javacrumbs.shedlock.support.KeepAliveLockProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import javax.sql.DataSource

@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "30s")
class ScheduleConfig {
    
    @Bean
    fun lockProvider(dataSource: DataSource, shedLockScheduler: ScheduledExecutorService): LockProvider =
        KeepAliveLockProvider(
            JdbcTemplateLockProvider(
                JdbcTemplateLockProvider.Configuration.builder()
                    .withJdbcTemplate(JdbcTemplate(dataSource))
                    .usingDbTime()
                    .withTableName("shedlock")
                    .build()
            ),
            shedLockScheduler
        )
    
    @Bean
    fun shedLockScheduler(
        @Value("\${shedlock.executor.pool.size:\${SHEDLOCK_EXECUTOR_POOL_SIZE_SIZE:4}}") poolSize: Int,
    ): ScheduledExecutorService =
        Executors.newScheduledThreadPool(poolSize) {
            val thread = Thread()
            thread.name = "shedlock-"
            thread
        }
}