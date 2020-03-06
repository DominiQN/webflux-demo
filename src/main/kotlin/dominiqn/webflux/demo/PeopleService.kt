package dominiqn.webflux.demo

import dominiqn.webflux.demo.util.LoggerCompanion
import org.springframework.stereotype.Service

@Service
class PeopleService {
    companion object : LoggerCompanion()

    fun hello() {
        logger.info("hello")
    }
}