package dominiqn.webflux.demo

import org.springframework.stereotype.Repository
import java.util.*

@Repository
class PeopleRepository {
    private val database: MutableMap<UUID, Person> = mutableMapOf(
        Person("Adam").let { it.id to it }
    )

    fun save(person: Person): Person {
        database[person.id] = person
        return person
    }

    fun findById(id: UUID) = database[id]

    fun findAll() = database.values.toList()
}
