package makcon.queries.app.scheduler

import makcon.queries.app.event.fetcher.EventsFetcher
import mu.KotlinLogging
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
@ConditionalOnProperty(
    value = ["scheduler.fetch-events.enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class EventsFetchingScheduler(
    private val eventsFetcher: EventsFetcher,
) {
    
    private val log = KotlinLogging.logger { }
    
    @EventListener(ApplicationReadyEvent::class)
    @SchedulerLock(name = "fetchEvents")
    @Scheduled(
        fixedDelayString = "\${events.sync.period.min:\${EVENTS_SYNC_PERIOD_MIN:30}}",
        timeUnit = TimeUnit.MINUTES
    )
    fun run() {
        try {
            eventsFetcher.run()
        } catch (e: Exception) {
            log.error(e) { "Failed to sync events" }
        }
    }
}