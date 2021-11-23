package com.example.data.table

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object UserEntity:Table<Nothing>("User") {
    val userId =  int("userId").primaryKey()
    val username = varchar("username")
    val image = varchar("image")
    val email = varchar("email")
    val haspassord = varchar("haspassord")
}