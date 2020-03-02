package dominiqn.webflux.demo

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

@Configuration
@EnableWebFlux
class WebConfig(
    private val handler: PeopleHandler
) : WebFluxConfigurer {

    @Bean
    fun routes() = coRouter {
        GET("/alive", accept(MediaType.TEXT_PLAIN)) {
            ServerResponse.ok().bodyValueAndAwait("alive")
        }
    }.and(
        coRouter {
            /*
            // same as below
            POST("/people", accept(MediaType.APPLICATION_JSON), handler::createPerson)
            GET("/people", accept(MediaType.APPLICATION_JSON), handler::listPeople)
            GET("/people/{id}", accept(MediaType.APPLICATION_JSON) , handler::getPerson)
            */
            "/people".nest {
                accept(MediaType.APPLICATION_JSON).nest {
                    POST("", handler::createPerson)
                    GET("", handler::listPeople)
                    GET("/{id}", handler::getPerson)
                }
            }
        }
    )

}