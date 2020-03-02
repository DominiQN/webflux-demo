package dominqn.webflux.demo

import kotlinx.coroutines.flow.asFlow
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.server.ServerWebInputException
import java.util.*

@Component
class PeopleHandler(
    private val repository: PeopleRepository
) {

    suspend fun createPerson(request: ServerRequest): ServerResponse {
        val person = request.awaitBody<Person>()
        validate(person)
        val saved = repository.save(person)
        return ok().bodyValueAndAwait(saved)
    }

    suspend fun getPerson(request: ServerRequest): ServerResponse {
        val personId = request.pathVariable("id").toUUID()
        return repository.findById(personId)?.let { ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it) }
            ?: ServerResponse.notFound().buildAndAwait()
    }

    suspend fun listPeople(request: ServerRequest): ServerResponse {
        val people = repository.findAll().asFlow()
        return ok().contentType(APPLICATION_JSON).bodyAndAwait(people)
    }

    private fun validate(person: Person) {
        val errors: Errors = BeanPropertyBindingResult(person, "person")
        if (errors.hasErrors()) {
            throw ServerWebInputException(errors.toString())
        }
    }
}

fun String.toUUID() = UUID.fromString(this)