package com.example.repositories

import com.example.data.model.Note
import com.example.data.table.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ktorm.database.Database
import org.ktorm.dsl.*

class NoteRepository(private val db: Database) {


    suspend fun insertNote(note: Note, userId: Int) = withContext(Dispatchers.IO) {
        val result = db.insert(NoteEntity) {
            set(it.title, note.title)
            set(it.subTitle, note.subTitle)
            set(it.note, note.note)
            set(it.dataTime, note.dataTime)
            set(it.color, note.color)
            set(it.userId, userId)
            set(it.imagePath, note.imagePath)
            set(it.webLink, note.webLink)
        }
        result
    }

    suspend fun updateNote(note: Note, userId: Int) = withContext(Dispatchers.IO) {
        val result = db.update(NoteEntity) {
            set(it.note, note.note)
            set(it.color, note.color)
            set(it.imagePath, note.imagePath)
            set(it.title, note.title)
            set(it.subTitle, note.subTitle)
            where {
                (it.id eq note.id!!) and (it.userId eq userId)
            }
        }
        result
    }

    suspend fun deleteNote(noteId: Int, userId: Int) = withContext(Dispatchers.IO) {
        val result = db.delete(NoteEntity) {
            (it.id eq noteId) and (it.userId eq userId)
        }
        result
    }

    suspend fun getAllNotes(userId: Int) = withContext(Dispatchers.IO) {
        val notes = db.from(NoteEntity).select()
            .where {
                NoteEntity.userId eq userId
            }.mapNotNull {
                rowToNote(it)
            }

        notes
    }

    private fun rowToNote(row: QueryRowSet?): Note? {
        return if (row == null) {
            null
        } else {
            Note(
                row[NoteEntity.id] ?: -1,
                row[NoteEntity.title] ?: "",
                row[NoteEntity.subTitle] ?: "",
                row[NoteEntity.dataTime] ?: "",
                row[NoteEntity.imagePath] ?:"",
                row[NoteEntity.note] ?: "",
                row[NoteEntity.color] ?:"",
                row[NoteEntity.webLink] ?:"",
                row[NoteEntity.userId] ?: -1
            )
        }
    }


}