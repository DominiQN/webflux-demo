package dominiqn.webflux.demo

import java.util.*

data class Person(
    val name: String
) {
    val id: UUID = UUID.randomUUID()
}