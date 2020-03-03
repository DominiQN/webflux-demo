package dominiqn.webflux.demo

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface PeopleRepository : ReactiveCrudRepository<People, Long> {

    @Query("select * from people p where name like concat('%', :name, '%')")
    fun findByNameLike(name: String): Flux<People>

}