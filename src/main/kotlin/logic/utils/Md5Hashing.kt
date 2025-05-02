package squad.abudhabi.logic.utils

import java.security.MessageDigest

class Md5Hashing: HashingService {
    @OptIn(ExperimentalStdlibApi::class)
    override fun hash(input: String): String {
        val bytes = MessageDigest.getInstance("MD5").digest(input.toByteArray())
        return bytes.toHexString()
    }
}