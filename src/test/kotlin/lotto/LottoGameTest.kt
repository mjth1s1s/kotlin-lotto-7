package lotto

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.assertj.core.api.Assertions.assertThat


class LottoGameTest {

    companion object {
        private const val ERROR_MESSAGE: String = "[ERROR]"
    }

    private val lottoGame = LottoGame()

    @Test
    fun `유효한 구입 금액을 입력하면 해당 금액을 반환한다`() {
        val game = LottoGame(readLine = { "3000" })
        val cost = game.inputCost()
        assertThat(cost).isEqualTo(3000)
    }

    @Test
    fun `금액이 1000원 미만이면 예외가 발생한다`() {
        val game = LottoGame(readLine = { "900" })
        assertThrows<IllegalArgumentException> {
            game.inputCost()
        }
    }

    @Test
    fun `금액이 1000 단위가 아니면 예외가 발생한다`() {
        val game = LottoGame(readLine = { "790" })
        assertThrows<IllegalArgumentException> {
            game.inputCost()
        }
    }

    @Test
    fun `유효한 당첨 번호를 입력하면 해당 로또를 반환한다`() {
        val game = LottoGame(readLine = { "1, 2, 3, 4, 5, 6" })
        val winningLotto = game.inputWinningLotto()
        assertThat(winningLotto).isEqualTo(listOf(1, 2, 3, 4, 5, 6))
    }

    @Test
    fun `당첨 번호가 유효하지 않으면 예외가 발생한다`() {
        val game = LottoGame(readLine = { "1, 2, 3, 4, 5" })
        assertThrows<IllegalArgumentException> {
            game.inputWinningLotto()
        }
    }

    @Test
    fun `유효한 보너스 번호를 입력하면 해당 번호를 반환한다`() {
        val game = LottoGame(readLine = { "8" })
        val bonusNumber = game.inputBonusNumber()
        assertThat(bonusNumber).isEqualTo(8)
    }

    @Test
    fun `보너스 번호가 유효하지 않으면 예외가 발생한다`() {
        val game = LottoGame(readLine = { "k" })
        assertThrows<IllegalArgumentException> {
            game.inputBonusNumber()
        }
    }

    @Test
    fun `구입 금액에 따른 로또 목록이 생성된다`() {
        val cost = 3000
        val game = LottoGame(readLine = { "3000" })
        val lottoList = game.buyLottos(cost)
        assertThat(lottoList.size).isEqualTo(cost / 1000)
        lottoList.forEach { lotto ->
            val numbers = lotto.getNumbers()
            assertThat(numbers.size).isEqualTo(6)
            assertThat(numbers.distinct().size).isEqualTo(6)
            assertThat(numbers.all { it in 1..45 }).isTrue()
        }
    }

    @Test
    fun `로또들과 당첨 로또를 비교하여 각 등수 개수 정확하게 계산`() {
        val winningLotto = Lotto(listOf(1, 2, 3, 4, 5, 6))
        val bonusNumber = 9

        val lottoList = listOf(
            Lotto(listOf(1, 2, 3, 4, 5, 6)),
            Lotto(listOf(1, 10, 20, 30, 40, 41)),
            Lotto(listOf(7, 8, 11, 19, 20, 23))
        )
        val result = lottoGame.calculateResult(lottoList,winningLotto, bonusNumber)

        assertThat(result.countRank(LottoRank.FIRST)).isEqualTo(1)
        assertThat(result.countRank(LottoRank.NONE)).isEqualTo(2)
    }
}