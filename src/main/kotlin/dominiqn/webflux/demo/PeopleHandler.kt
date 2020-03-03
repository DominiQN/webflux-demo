package dominiqn.webflux.demo

import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
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
        val person = request.awaitBody<People>()
        validate(person)
        val saved = repository.save(person).asFlow()
        return ok().contentType(APPLICATION_JSON).bodyAndAwait(saved)
    }

    suspend fun getPerson(request: ServerRequest): ServerResponse {
        val personId = request.pathVariable("id").toLong()
        return repository.findById(personId).awaitFirstOrNull()
            ?.let { ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it) }
            ?: ServerResponse.notFound().buildAndAwait()
    }

    suspend fun listPeople(request: ServerRequest): ServerResponse {
        return request.queryParam("name")
            .map { name -> repository.findByNameLike(name) }
            .orElseGet { repository.findAll() }
            .asFlow()
            .let {
                ok().contentType(APPLICATION_JSON).bodyAndAwait(it)
            }
    }

    private fun validate(person: People) {
        val errors: Errors = BeanPropertyBindingResult(person, "person")
        if (errors.hasErrors()) {
            throw ServerWebInputException(errors.toString())
        }
    }
}