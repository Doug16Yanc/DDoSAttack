package example.com.models

import kotlinx.serialization.Serializable
import org.h2.api.H2Type.UUID

@Serializable
data class Attack(
    val id: Long,
    val sourceIp: String,
    val targetIp: String,
    val timestamp: String,
    val typeAttack: String
)