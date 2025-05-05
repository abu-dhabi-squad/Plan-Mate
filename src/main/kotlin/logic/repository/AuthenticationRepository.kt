package logic.repository
import squad.abudhabi.logic.model.User

interface AuthenticationRepository {
    fun loginUser(username: String, password: String): User?
    fun createUser(user: User)
    fun getUserByName(username:String):User?
    fun saveLoggedUser(user: User)
    fun getLoggedUser():User?
}