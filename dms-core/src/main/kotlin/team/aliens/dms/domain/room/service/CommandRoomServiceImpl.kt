package team.aliens.dms.domain.room.service

import java.util.UUID
import org.springframework.stereotype.Service
import team.aliens.dms.domain.room.model.Room
import team.aliens.dms.domain.room.spi.CommandRoomPort
import team.aliens.dms.domain.room.spi.QueryRoomPort

@Service
class CommandRoomServiceImpl(
    private val commandRoomPort: CommandRoomPort,
    private val queryRoomPort: QueryRoomPort
) : CommandRoomService {

    override fun saveNotExistsRooms(roomNumbers: List<String>, schoolId: UUID): Map<String, Room> {
        val roomMap = queryRoomPort.queryRoomsByRoomNumbersIn(roomNumbers, schoolId)
            .associateBy { it.number }
            .toMutableMap()

        val notExistsRooms = roomNumbers.mapNotNull { roomNumber ->
            if (!roomMap.containsKey(roomNumber)) {
                Room(
                    number = roomNumber,
                    schoolId = schoolId
                ).apply {
                    roomMap[roomNumber] = this
                }
            } else { null }
        }

        commandRoomPort.saveRooms(notExistsRooms)
            .map { roomMap[it.number] = it }

        return roomMap
    }
}
