package team.aliens.dms.point.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.point.exception.PointHistoryCanNotCancelException
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointType
import java.time.LocalDateTime
import java.util.UUID

class PointHistoryTests {

    @Test
    fun `취소 내역 반환`() {
        // given
        val pointHistory = PointHistory(
            studentName = "김은빈",
            studentGcn = "2106",
            bonusTotal = 3,
            minusTotal = 0,
            isCancel = false,
            pointName = "분리수거",
            pointScore = 3,
            pointType = PointType.BONUS,
            createdAt = LocalDateTime.of(2023, 3, 5, 12, 0),
            schoolId = UUID.randomUUID()
        )

        val pointTotal = Pair(3, 0)

        // when
        val canceledHistory = pointHistory.cancelHistory(pointTotal)

        // then
        assertAll(
            { assertEquals(canceledHistory.bonusTotal, 0) },
            { assertEquals(canceledHistory.minusTotal, 0) },
            { assertEquals(canceledHistory.isCancel, true) }
        )
    }

    @Test
    fun `취소할 수 없는 내역`() {
        // given
        val canceledPointHistory = PointHistory(
            studentName = "김은빈",
            studentGcn = "2106",
            bonusTotal = 3,
            minusTotal = 0,
            isCancel = true,
            pointName = "분리수거",
            pointScore = 3,
            pointType = PointType.BONUS,
            createdAt = LocalDateTime.of(2023, 3, 5, 12, 0),
            schoolId = UUID.randomUUID()
        )

        val pointTotal = Pair(3, 0)

        // when & then
        assertThrows<PointHistoryCanNotCancelException> {
            canceledPointHistory.cancelHistory(pointTotal)
        }
    }
}
