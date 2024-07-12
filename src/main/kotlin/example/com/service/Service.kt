package example.com.service

import example.com.models.Attack
import example.com.repository.AttackRepository
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class Service(private val database: Database) : AttackRepository {

    private object Attacks : Table() {
        val id = long("id")
        val sourceIp = varchar("sourceIP", 250)
        val targetIp = varchar("targetIp", 250)
        val timestamp = varchar("timestamp", 250)
        val typeAttack = varchar("typeAttack", 250)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Attacks)
        }
    }

    override suspend fun getAll(): List<Attack> = newSuspendedTransaction(Dispatchers.IO, database) {
        Attacks.selectAll().map { toAttack(it) }
    }

    override suspend fun getAttack(id: Long): Attack? = newSuspendedTransaction(Dispatchers.IO, database) {
        Attacks.select { Attacks.id eq id }
            .mapNotNull { toAttack(it) }
            .singleOrNull()
    }

    override suspend fun addAttack(attack: Attack): Attack = newSuspendedTransaction(Dispatchers.IO, database) {
        val id = Attacks.insert {
            it[sourceIp] = attack.sourceIp
            it[targetIp] = attack.targetIp
            it[timestamp] = attack.timestamp
            it[typeAttack] = attack.typeAttack
        }
        attack.copy(id = attack.id)
    }

    override suspend fun deleteAttack(id: Long): Unit = newSuspendedTransaction(Dispatchers.IO, database) {
        Attacks.deleteWhere { Attacks.id eq id }
    }

    override suspend fun updateAttack(id: Long, attack: Attack): Unit = newSuspendedTransaction{
        val updatedRows = Attacks.update({ Attacks.id eq id }) {
            it[sourceIp] = attack.sourceIp
            it[targetIp] = attack.targetIp
            it[timestamp] = attack.timestamp
            it[typeAttack] = attack.typeAttack
        }
    }

    private fun toAttack(row: ResultRow): Attack =
        Attack(
            id = row[Attacks.id],
            sourceIp = row[Attacks.sourceIp],
            targetIp = row[Attacks.targetIp],
            timestamp = row[Attacks.timestamp],
            typeAttack = row[Attacks.typeAttack]
        )
}
