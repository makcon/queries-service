package makcon.queries.adapters.repository.mapper

import java.sql.Timestamp
import java.time.Instant

fun Instant.toTimestamp(): Timestamp = Timestamp.from(this)