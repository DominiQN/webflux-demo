package dominiqn.webflux.demo

import org.springframework.data.annotation.Id

data class People(
    /**
     * H2 Database를 사용했을 경우, Long = 0와 같이 사용할 수가 없었다.
     */
    @Id
    val id: Long?,
    val name: String
)