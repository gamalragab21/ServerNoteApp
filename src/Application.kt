package com.example

import com.example.helpers.ConnectionDataBase
import com.example.repositories.NoteRepository
import com.example.repositories.UserRepository
import com.example.routes.noteRoute
import com.example.routes.usersRoute
import com.example.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.sessions.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.config.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.locations.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val db = ConnectionDataBase.database
    val userRepository = UserRepository(db)
    val noteRepository = NoteRepository(db)
    val tokenManager = TokenManager(config)

    // after validate send user entity from db
    install(Authentication) {
        jwt("jwt") {
            verifier(tokenManager.verifyJWTToken())
            realm = config.property("realm").getString()
            validate { jwtCredential ->
                val payload = jwtCredential.payload
                val userId = payload.getClaim("userId").asInt()
                if (userId.toString().isNotEmpty()) {
                    val user = userRepository.findUserById(userId)
                    user
                } else {
                    null
                }
            }
        }
    }


    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
    install(Locations)

    install(ContentNegotiation) {
        gson {}
    }




    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        usersRoute(userRepository, tokenManager)
        noteRoute(noteRepository, tokenManager)
    }


}

data class MySession(val count: Int = 0)

