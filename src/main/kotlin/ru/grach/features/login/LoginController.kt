package ru.grach.features.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.grach.database.tokens.TokenDTO
import ru.grach.database.tokens.Tokens
import ru.grach.database.users.Users
import java.util.*

class LoginController(private val call: ApplicationCall) {
    suspend fun performLogin() {
        val receive = call.receive<LoginReciveRemote>()
        val userDTO = Users.fetchUser(receive.login)
        println("receive -> $receive, dto -> $userDTO")
        if (userDTO == null) {
            call.respond(HttpStatusCode.BadRequest, "User NOT found")
        } else {
            if (userDTO.password == receive.password) {
                val token = UUID.randomUUID().toString()
                Tokens.insert(
                    TokenDTO(
                        rowId = UUID.randomUUID().toString(), login = receive.login,
                        token = token
                    )
                )
                call.respond(LoginResponseRemote(token = token))

            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid password")
            }
        }
    }
}