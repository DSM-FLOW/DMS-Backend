package team.aliens.dms.domain.volunteer

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import team.aliens.dms.domain.volunteer.exception.VolunteerApplicationAlreadyAssigned
import team.aliens.dms.domain.volunteer.stub.createVolunteerApplicationStub

class VolunteerTest : DescribeSpec({

    describe("approveVolunteer") {
        val volunteerApplication = createVolunteerApplicationStub(
            approved = false
        )
        context("봉사 승인이 되어있지 않다면") {
            it("봉사 승인이 가능하다") {
                shouldNotThrowAny {
                    volunteerApplication.checkIsNotApproved()
                }
            }
        }
        context("봉사 승인이 이미 되어있다면") {
            val volunteerApplication2 = createVolunteerApplicationStub(
                approved = true
            )
            it("봉사 승인 불가능 하다") {
                shouldThrow<VolunteerApplicationAlreadyAssigned> {
                    volunteerApplication2.checkIsNotApproved()
                }
            }
        }
    }
})
