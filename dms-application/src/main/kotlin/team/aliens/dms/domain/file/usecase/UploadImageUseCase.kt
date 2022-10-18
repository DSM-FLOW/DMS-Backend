package team.aliens.dms.domain.file.usecase

import team.aliens.dms.domain.file.exception.FileInvalidExtensionException
import team.aliens.dms.domain.file.spi.UploadFilePort
import team.aliens.dms.global.annotation.UseCase
import java.io.File

@UseCase
class UploadImageUseCase(
    private val uploadFilePort: UploadFilePort
) {

    fun execute(file: File): String {
        if (!isCorrectExtension(file)) {
            file.delete()
            throw FileInvalidExtensionException
        }

        return uploadFilePort.upload(file)
    }

    private fun isCorrectExtension(file: File) = when (file.extension.lowercase()) {
        "jpg", "jpeg", "png", "heic" -> true
        else -> false
    }

}