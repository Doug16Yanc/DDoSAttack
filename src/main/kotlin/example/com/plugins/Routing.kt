package example.com.plugins

import example.com.models.Attack
import example.com.service.Service
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    service : Service
) {
    routing {
        get("/attacks") {
            val response = service.getAll().map { attack ->
                Attack(
                    id = attack.id,
                    sourceIp = attack.sourceIp,
                    targetIp = attack.targetIp,
                    timestamp = attack.timestamp,
                    typeAttack = attack.typeAttack
                )
            }
            call.respond(HttpStatusCode.OK, response)
        }
        get("/attacks/{id}") {
            val id = call.parameters["id"]?.toLong()

            val attack = id?.let { it1 -> service.getAttack(it1) }
            if (attack != null) {
                call.respond(attack)
            } else {
                call.respond(HttpStatusCode.NotFound, "Not identified.")
            }
        }
        post("/attacks") {
            val attackDraft = call.receive<Attack>()

            val attack = service.addAttack(attackDraft)
            call.respondText("Attack ${attack.id} saved successfully.", ContentType.Text.Plain)
        }
        put("/attacks/{id}") {
            val id = call.parameters["id"]?.toLong()

            val attackDraft = call.receive<Attack>()
            val updated = id?.let { it1 -> service.updateAttack(it1, attackDraft) }
            if (updated != null) {
                call.respond(HttpStatusCode.OK, "Attack updated")
            } else {
                call.respond(HttpStatusCode.NotFound, "Attack not found")
            }
        }
        delete("/attacks/{id}") {
            val id = call.parameters["id"]?.toLong()

            id?.let { it1 -> service.deleteAttack(it1) }
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
