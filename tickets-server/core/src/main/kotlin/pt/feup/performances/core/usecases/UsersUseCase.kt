package pt.feup.performances.core.usecases

import pt.feup.performances.core.User
import pt.feup.performances.core.UserNotFoundException
import pt.feup.performances.core.UserRepositoryService

interface UsersUseCase {
    fun registerUser(user: User): String
    fun getUser(userId: String): User
}

class UsersUseCaseImpl(
    private val usersRepository: UserRepositoryService
) : UsersUseCase {
    override fun registerUser(user: User): String {
        return usersRepository.save(user)
    }
    override fun getUser(userId: String): User {
        return usersRepository.findById(userId) ?: throw UserNotFoundException()
    }

}
