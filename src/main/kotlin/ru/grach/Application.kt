package ru.grach

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import ru.grach.features.login.configureLoginRouting
import ru.grach.features.register.configureRegisterRouting
import ru.grach.plugins.*

fun main() {
    Database.connect("jdbc:postgresql://localhost:5432/Grach1.0", driver = "org.postgresql.Driver",
        user ="postgres", password="пароль в pgAdmin" )
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureLoginRouting()
        configureRegisterRouting()
        configureSerialization()
    }.start(wait = true)
}
