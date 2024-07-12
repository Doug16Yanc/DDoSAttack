package example.com.repository

import example.com.models.Attack

interface AttackRepository {
    suspend fun getAll() : List<Attack>
    suspend fun getAttack(id : Long) : Attack?
    suspend fun addAttack(attack: Attack) : Attack
    suspend fun deleteAttack(id : Long)
    suspend fun updateAttack(id: Long, attack: Attack)
}