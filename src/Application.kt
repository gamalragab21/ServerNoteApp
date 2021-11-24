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
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
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
                    val email = payload.getClaim("email").asString()
                    if (email.isNotEmpty()) {
                        val user = userRepository.findUserByEmail(email)
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

    }.start(wait = true)
}

data class MySession(val count: Int = 0)

