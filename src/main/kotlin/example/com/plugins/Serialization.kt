package example.com.plugins

import example.com.models.Attack
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.collections.*

val attacks = ConcurrentMap<Long, Attack>()

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
