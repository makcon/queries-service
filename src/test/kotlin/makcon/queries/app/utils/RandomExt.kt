package makcon.queries.app.utils

import java.time.Instant
import java.time.temporal.ChronoUnit.MILLIS
import java.util.*
import kotlin.random.Random

object Rand {
    
    private val chars: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    
    fun string(size: Int = 15): String = List(size) { chars.random() }.joinToString("")
    
    fun uuidStr() = UUID.randomUUID().toString()
    
    fun instantNow(): Instant = Instant.now().truncatedTo(MILLIS)
    
    fun instantPast(): Instant = instantNow().minusMillis(long(10000))
    
    fun double(until: Double = Double.MAX_VALUE) = Random.nextDouble(until)
    
    fun int(until: Int = Int.MAX_VALUE) = Random.nextInt(until)
    
    fun long(until: Long = Long.MAX_VALUE) = Random.nextLong(until)
    
    fun bool() = Random.nextBoolean()
}