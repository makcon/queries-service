package makcon.queries.external.event.dto

data class EventsResultV1(
    val totalCount: Long,
    val pageNumber: Int,
    val pageSize: Int,
    val items: List<EventV1> = listOf(),
)