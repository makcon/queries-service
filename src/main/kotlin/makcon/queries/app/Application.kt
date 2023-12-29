package makcon.queries.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["makcon.queries"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}