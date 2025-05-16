package data.utils.hashing

import logic.utils.hashing.HashingService
import java.security.MessageDigest

class Md5Hashing : HashingService {
    override fun hash(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val hashBytes = md.digest(input.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}