package dominiqn.webflux.demo

import dominiqn.webflux.demo.util.LoggerCompanion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class PeopleService(
    private val repository: PeopleRepository
) {
    companion object : LoggerCompanion()

    fun hello() {
        logger.info("hello")
    }

    fun createPerson(person: People) = repository.save(person)

    fun getPerson(personId: Long) = repository.findById(personId)

    fun listPerson(name: String?): Flux<People> {
        return (name?.let { repository.findByNameLike(name) } ?: repository.findAll())
    }
}