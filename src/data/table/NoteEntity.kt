package com.example.data.table

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object NoteEntity:Table<Nothing>("Note") {

    val id =  int("id").primaryKey()
    val title = varchar("title")
    val subTitle = varchar("subTitle")
    val dataTime = varchar("dataTime")
    val imagePath = varchar("imagePath")
    val note = varchar("note")
    val color = varchar("color")
    val webLink = varchar("webLink")
    val userId = int("userId")
}