package data.authentication.mapper

import data.authentication.model.UserDto
import logic.exceptions.UserTypeNotFoundException
import logic.model.User
import logic.model.UserType
import java.util.UUID

class MongoUserMapper {
    fun userToDto(user: User): UserDto {
        return UserDto(
            id = user.id.toString(),
            username = user.username,
            password = user.password,
            userType = user.userType.name
        )
    }
    fun dtoToUser(dto: UserDto): User {
        UserType.entries.find { it.name == dto.userType } ?: throw UserTypeNotFoundException()
        return User(
            id = UUID.fromString(dto.id),
            username = dto.username,
            password = dto.password,
            userType = UserType.valueOf(dto.userType)
        )
    }
}