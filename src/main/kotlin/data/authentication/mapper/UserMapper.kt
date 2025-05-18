package data.authentication.mapper

import data.authentication.model.UserDto
import logic.exceptions.UserTypeNotFoundException
import logic.model.User
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserMapper {
    fun userToDto(user: User): UserDto {
        return UserDto(
            _id = user.userId.toString(),
            username = user.username,
            password = user.password,
            userType = user.userType.name
        )
    }

    fun dtoToUser(dto: UserDto): User {
        User.UserType.entries.find { it.name == dto.userType } ?: throw UserTypeNotFoundException()
        return User(
            userId = Uuid.parse(dto._id),
            username = dto.username,
            password = dto.password,
            userType = User.UserType.valueOf(dto.userType)
        )
    }
}