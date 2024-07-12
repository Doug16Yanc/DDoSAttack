package example.com

import example.com.plugins.*
import example.com.service.Service
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.tomcat.*
import org.jetbrains.exposed.sql.Database


fun main() {
    embeddedServer(
        Tomcat,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    val database = Database.connect(
        url = "jdbc:h2:file:./database/db",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )
    val service = Service(database)
    configureRouting(service)
}
