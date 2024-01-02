The project demonstrates one of the solutions to query distributed system data. The idea is to have a centralised
service that stores data from different services. It assumes that all related services
use [event sourcing pattern](https://microservices.io/patterns/data/event-sourcing.html) (they permanently store events
in a database)

To make it working we should:

### Subscribe to events

- Each service sends previously stored events using a messaging system, e.q. Kafka
- The service subscribes to those events
- It fetches the data and transforms it into a representation suitable to query it efficiently. E.q. in the case of an
  SQL database we store all searchable fields as columns and the rest of the data as a JSON object
- Also, the service handles the order of events

### Implement events fetching

- There are cases when we need to request events from original services
    - In order to initialize the service's database
    - In case we need to change a structure
- Each service provides
  an [interface](https://github.com/makcon/queries-service/blob/master/src/main/kotlin/makcon/queries/external/event/client/EventsClient.kt)
  to request events. For this example there is fake implementation that just emulates the fetching process and provides
  random events
- The service periodically checks if a certain topic is sync
- Also it possible to implement a logic to manually trigger the synchronisation

### Create querying processing

- For most cases queries are primitive (let's say we need to request orders by product data and return sorted results),
  so we can implement a general query translator from API request to a database query
- For most complex scenarios we implement custom queries. Also, we can precalculate data using multiple events  