package logic.repository
import logic.model.User

interface AuthenticationRepository {
    suspend fun loginUser(username: String, password: String): User?
    suspend fun createUser(user: User)
    suspend fun getUserByName(username:String):User?
    fun saveLoggedUser(user: User)
    fun getLoggedUser():User?
}