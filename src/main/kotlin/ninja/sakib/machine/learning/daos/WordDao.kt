package ninja.sakib.machine.learning.daos

import ninja.sakib.machine.learning.models.Word
import ninja.sakib.pultusorm.core.PultusORM
import kotlin.properties.Delegates

/**
 * @author Sakib Sami
 * Created on 2/13/17.
 */

object WordDao {
    val dbName = "words.db"
    val dbPath = System.getProperty("user.home")

    var connection: PultusORM by Delegates.notNull<PultusORM>()

    init {
        connection = PultusORM(dbName, dbPath)
    }

    fun save(word: Word) {
        connection.save(word)
    }

    fun get(): MutableList<Any> {
        return connection.find(Word())
    }
}
