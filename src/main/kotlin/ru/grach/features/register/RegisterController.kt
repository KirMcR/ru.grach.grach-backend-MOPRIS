package ru.grach.features.register

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.grach.database.tokens.TokenDTO
import ru.grach.database.tokens.Tokens
import ru.grach.database.users.UserDTO
import ru.grach.database.users.Users
import ru.grach.utils.isValidEmail
import java.util.*

class RegisterController(private val call: ApplicationCall) {
    suspend fun registerNewUser() {

        val registerReciveRemote = call.receive<RegisterReciveRemote>()
        if (!registerReciveRemote.email.isValidEmail()) {
            call.respond(HttpStatusCode.BadRequest, "Email is NOT valid")
        }

        val userDTO = Users.fetchUser(registerReciveRemote.login)
        val usernameCheck = Users.fetchUserByUserName(registerReciveRemote.username)
        println("userDTO is $userDTO")
        println("username check is $usernameCheck")
        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "Login already exist")
        }
        else if(usernameCheck!=null){
            call.respond(HttpStatusCode.Conflict, "Username already exist")
        }
       else {
            val token = UUID.randomUUID().toString()
            try {

                Users.insert(
                    UserDTO(
                        login = registerReciveRemote.login,
                        password = registerReciveRemote.password,
                        email = registerReciveRemote.email,
                        username = registerReciveRemote.username
                    )
                )
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.Conflict, "User already exist")
            }
            Tokens.insert(
                TokenDTO(
                    rowId = UUID.randomUUID().toString(), login = registerReciveRemote.login,
                    token = token
                )
            )
            call.respond(RegisterResponseRemote(token = token))
        }
    }
}