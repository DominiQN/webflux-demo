package dominiqn.webflux.demo.util

import java.util.*

fun <T> Optional<T>.orElseNull(): T? {
    if (this.isPresent) {
        return this.get()
    }
    return null
}