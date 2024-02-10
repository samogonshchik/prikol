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

    @Query("SELECT * from terms_table WHERE id = :id")
    fun getTermStream(id: Int): Flow<Term>

    @Query("SELECT * from terms_table WHERE name = :name")
    fun getTermStream(name: String): Flow<Term>

    @Query("SELECT * from terms_table ORDER BY name ASC")
    fun getAllTermsStream(): Flow<List<Term>>

    @Query("DELETE from terms_table")
    fun deleteAll()

//    @Query("SELECT  from terms_table")
//    fun getAllIdentifyingNames(): Flow<List<String>>
}