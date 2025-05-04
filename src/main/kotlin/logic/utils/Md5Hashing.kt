package logic.utils
import com.soywiz.krypto.md5

class Md5Hashing : HashingService {
    @OptIn(ExperimentalStdlibApi::class)
    override fun hash(input: String): String {
        val hashBytes = input.encodeToByteArray().md5().bytes
        return hashBytes.toHexString()
    }
}