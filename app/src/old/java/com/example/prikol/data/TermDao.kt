package com.example.prikol.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TermDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(term: Term)

    @Update
    suspend fun update(term: Term)

    @Delete
    suspend fun delete(term: Term)

    @Query("DELETE FROM terms_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM terms_table WHERE id = :id")
    fun getTermStream(id: Int): Flow<Term>

    @Query("SELECT * FROM terms_table WHERE name = :name")
    fun getTermStream(name: String): Flow<Term>

    @Query("SELECT * FROM terms_table ORDER BY id ASC")
    fun getAllTermsStream(): Flow<List<Term>>

    @Query("SELECT name FROM terms_table")
    fun getAllNamesStream(): Flow<List<String>>

    @Query("SELECT * FROM terms_table WHERE id = :id")
    suspend fun getTerm(id: Int): Term

    @Query("SELECT * FROM terms_table WHERE name = :name")
    suspend fun getTerm(name: String): Term

    @Query("SELECT MIN(id) FROM terms_table WHERE id > :id")
    suspend fun getNextId(id: Int): Int?

    @Query("SELECT MAX(id) FROM terms_table WHERE id < :id")
    suspend fun getPrevId(id: Int): Int?
}