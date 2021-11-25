package com.example.routes

import com.example.data.model.LoginRequest
import com.example.data.model.RegisterRequest
import com.example.data.model.User
import com.example.repositories.UserRepository
import com.example.utils.MyResponse
import com.example.utils.TokenManager
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

const val API_VERSION = "/v1"
const val USERS = "$API_VERSION/users"
const val REGISTER_REQUEST = "$USERS/register"
const val LOGIN_REQUEST = "$USERS/login"
const val ME_REQUEST = "$USERS/me"

//@Location(REGISTER_REQUEST)
//class UserRegisterRoute
//
//@Location(LOGIN_REQUEST)
//class UserLoginRoute

fun Route.usersRoute(userRepository: UserRepository, tokenManager: TokenManager) {

    //base_url/v1/users/register
    post(REGISTER_REQUEST) {
        // check body request if  missing some fields
        val registerRequest = try {
            call.receive<RegisterRequest>()
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.OK,
                MyResponse(
                    success = false,
                    message = "Missing Some Fields",
                    data = null
                )
            )
            return@post
        }

        // check if operation connected db successfully
        try {
            val user = User(
                username = registerRequest.username,
                email = registerRequest.email, image = registerRequest.image, password = registerRequest.password
            )
            // check if email exist or note
            if (userRepository.findUserByEmail(user.email) == null) // means not found
            {
                val result = userRepository.register(user)
                // if result >0 it's success else is failed
                if (result > 0) {
                    call.respond(
                        HttpStatusCode.OK,
                        MyResponse(
                            success = true,
                            message = "Registration Successfully",
                            data = tokenManager.generateJWTToken(user)
                        )
                    )
                    return@post
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        MyResponse(
                            success = false,
                            message = "Failed Registration",
                            data = null
                        )
                    )
                    return@post
                }
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    MyResponse(
                        success = false,
                        message = "User already registration before.",
                        data = null
                    )
                )
                return@post
            }

        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.OK,
                MyResponse(
                    success = false,
                    message = e.message ?: "Failed Registration",
                    data = null
                )
            )
            return@post
        }

    }

    //base_url/v1/users/login
    post(LOGIN_REQUEST) {
        // check body request if  missing some fields
        val loginRequest = try {
            call.receive<LoginRequest>()
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.OK,
                MyResponse(
                    success = false,
                    message = "Missing Some Fields",
                    data = null
                )
            )
            return@post
        }

        // check if operation connected db successfully
        try {
            val user = userRepository.findUserByEmail(loginRequest.email)

            println("User Find ${user.toString()}")
            // check if user exist or not
            if (user != null) {
                // check password after hash pasword
                if (user.matchPassword(loginRequest.password)) {
                    call.respond(
                        HttpStatusCode.OK,
                        MyResponse(
                            success = true,
                            message = "You are logged in successfully",
                            data = tokenManager.generateJWTToken(user)
                        )
                    )
                    return@post
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        MyResponse(
                            success = false,
                            message = "Password Incorrect",
                            data = null
                        )
                    )
                    return@post
                }
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    MyResponse(
                        success = false,
                        message = "Email is wrong",
                        data = null
                    )
                )
                return@post
            }

        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.OK,
                MyResponse(
                    success = false,
                    message = e.message ?: "Failed Login",
                    data = null
                )
            )
            return@post
        }


    }

    authenticate("jwt") {
        get(ME_REQUEST) {
            // get user info from jwt

            val user =  try{
                call.principal<User>()!!

            }catch (e:Exception){
                call.respond(
                    HttpStatusCode.OK,
                    MyResponse(
                        success = false,
                        message = e.message ?: "Failed ",
                        data = null
                    )
                )
                return@get
            }

            call.respond(
                HttpStatusCode.OK,
                MyResponse(
                    success = true,
                    message = "Success",
                    data = user
                )
            )
            return@get


        }
    }

}