package com.example.routes

import com.example.data.model.Note
import com.example.data.model.User
import com.example.repositories.NoteRepository
import com.example.utils.MyResponse
import com.example.utils.TokenManager
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

const val NOTES = "$API_VERSION/notes"
const val CREATE_NOTES = "$NOTES/create"
const val UPDATE_NOTES = "$NOTES/update"
const val DELETE_NOTES = "$NOTES/delete"
const val SELECT_NOTE = "$NOTES/{id}"

//@Location(CREATE_NOTES)
//class NoteCreateRoute
//
//@Location(NOTES)
//class NoteGetRoute
//
//@Location(UPDATE_NOTES)
//class NoteUpdateRoute
//
//@Location(DELETE_NOTES)
//class NoteDeleteRoute


fun Route.noteRoute(noteRepository: NoteRepository, tokenManager: TokenManager) {

    authenticate("jwt") {

        // base_url/v1/notes
        get(NOTES) {
            try {
                // get user info from jwt
                val user = call.principal<User>()!!

                print("User ${user.toString()}")
                // get  notes of this user
                val notes = noteRepository.getAllNotes(user.id!!)

                call.respond(
                    HttpStatusCode.OK,
                    MyResponse(
                        success = true,
                        message = "Success ",
                        data = notes
                    )
                )

            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.OK,
                    MyResponse(
                        success = false,
                        message = e.message ?: "Conflict during get note",
                        data = null
                    )
                )
            }

        }
        // base_url/v1/notes/create
        post(CREATE_NOTES) {
            val note:Note = try {
                call.receive<Note>()
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.OK, MyResponse(
                        false,
                        "Missing Fields", data = null
                    )
                )
                return@post
            }
            try {
                // get user info from jwt
                val user = call.principal<User>()!!

                // insert note
                val result = noteRepository.insertNote(note, user.id!!)
                // check successfully or note
                if (result > 0) {
                    call.respond(
                        HttpStatusCode.OK,
                        MyResponse(
                            success = true,
                            message = "Insert Note  Successfully",
                            data = null
                        )
                    )
                    return@post
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        MyResponse(
                            success = false,
                            message = "Failed insert note.",
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
                        message = e.message ?: "Conflict during insert note",
                        data = null
                    )
                )
                return@post
            }

        }

        // base_url/v1/notes/update
        put(UPDATE_NOTES) {
            val note = try {
                call.receive<Note>()
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.OK, MyResponse(
                        false,
                        "Missing Fields", data = null
                    )
                )
                return@put
            }
            try {
                if (note.id != null) {
                    // get user info from jwt
                    val user = call.principal<User>()!!

                    // insert note
                    val result = noteRepository.updateNote(note, user.id!!)
                    // check successfully or note
                    if (result > 0) {
                        call.respond(
                            HttpStatusCode.OK,
                            MyResponse(
                                success = true,
                                message = "update Note  Successfully",
                                data = note
                            )
                        )
                        return@put
                    } else {
                        call.respond(
                            HttpStatusCode.OK,
                            MyResponse(
                                success = false,
                                message = "Failed update note.",
                                data = null
                            )
                        )
                        return@put
                    }
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        MyResponse(
                            success = false,
                            message = "Missing Id of note",
                            data = null
                        )
                    )
                    return@put
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.OK,
                    MyResponse(
                        success = false,
                        message = e.message ?: "Conflict during update note",
                        data = null
                    )
                )
                return@put
            }

        }


        // base_url/v1/notes/delete
        delete(DELETE_NOTES) {
            val id = try {
                call.request.queryParameters["id"]!!.toInt()
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.OK, MyResponse(
                        false,
                        "Missing  Id Field", data = null
                    )
                )
                return@delete
            }
            try {
                // get user info from jwt
                val user = call.principal<User>()!!

                // insert note
                val result = noteRepository.deleteNote(id, user.id!!)
                // check successfully or note
                if (result > 0) {
                    call.respond(
                        HttpStatusCode.OK,
                        MyResponse(
                            success = true,
                            message = "Delete Note  Successfully",
                            data = id
                        )
                    )
                    return@delete
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        MyResponse(
                            success = false,
                            message = "Failed delete  note.",
                            data = null
                        )
                    )
                    return@delete
                }

            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.OK,
                    MyResponse(
                        success = false,
                        message = e.message ?: "Conflict during delete note",
                        data = null
                    )
                )
                return@delete
            }

        }

        get(SELECT_NOTE) {
            val id = try {
                call.parameters["id"]?.toInt() ?: -1
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.OK, MyResponse(
                        false,
                        "Missing  Id Field", data = null
                    )
                )
                return@get
            }
            try {
                val user = call.principal<User>()!!

                val note = noteRepository.findNoteById(id, user.id!!)
                if (note!=null) {
                    call.respond(
                        HttpStatusCode.OK, MyResponse(
                            true,
                            "Success get note ", data = note
                        )
                    )
                    return@get
                }else{
                    call.respond(
                        HttpStatusCode.OK, MyResponse(
                            true,
                            "Not found this note"
                            , data = null
                        )
                    )
                    return@get
                }

            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.OK, MyResponse(
                        false,
                        "Conflict during get note", data = null
                    )
                )
                return@get
            }
        }

    }


}