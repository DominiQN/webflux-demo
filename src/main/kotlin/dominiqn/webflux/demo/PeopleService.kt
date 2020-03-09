package dominiqn.webflux.demo

import dominiqn.webflux.demo.util.LoggerCompanion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.stereotype.Service

@Service
class PeopleService(
    private val repository: PeopleRepository
) {
    companion object : LoggerCompanion()

    fun hello() {
        logger.info("hello")
    }

    fun createPerson(person: People) = repository.save(person).asFlow()

    fun getPerson(personId: Long) = repository.findById(personId)

    fun listPerson(name: String?): Flow<People> {
        return (name?.let { repository.findByNameLike(name) } ?: repository.findAll()).asFlow()
    }
}