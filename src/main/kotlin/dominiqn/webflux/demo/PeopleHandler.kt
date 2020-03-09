package dominiqn.webflux.demo

import dominiqn.webflux.demo.util.orElseNull
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.server.ServerWebInputException

@Component
class PeopleHandler(
    private val service: PeopleService
) {

    suspend fun createPerson(request: ServerRequest): ServerResponse {
        return request.awaitBody<People>()
            .validate()
            .let { service.createPerson(it) }
            .asFlow()
            .let { ok().contentType(APPLICATION_JSON).bodyAndAwait(it) }
    }

    suspend fun getPerson(request: ServerRequest): ServerResponse {
        return request.pathVariable("id")
            .let { service.getPerson(it.toLong()) }
            .awaitFirstOrNull()
            ?.let { ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it) }
            ?: ServerResponse.notFound().buildAndAwait()
    }

    suspend fun listPeople(request: ServerRequest): ServerResponse {
        return request.queryParam("name")
            .orElseNull()
            .let { service.listPerson(it) }
            .asFlow()
            .let { ok().contentType(APPLICATION_JSON).bodyAndAwait(it) }
    }

    private fun People.validate(): People {
        val errors: Errors = BeanPropertyBindingResult(this, "people")
        if (errors.hasErrors()) {
            throw ServerWebInputException(errors.toString())
        }
        return this
    }
}