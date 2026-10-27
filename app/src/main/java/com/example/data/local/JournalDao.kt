package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    // --- Journal Entries ---
    @Query("SELECT * FROM journal_entries ORDER BY dateEpochMillis DESC, id DESC")
    fun getAllEntries(): Flow<List<JournalEntry>>

    @Query("SELECT * FROM journal_entries WHERE id = :id LIMIT 1")
    suspend fun getEntryById(id: Long): JournalEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: JournalEntry): Long

    @Update
    suspend fun updateEntry(entry: JournalEntry)

    @Delete
    suspend fun deleteEntry(entry: JournalEntry)

    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun deleteEntryById(id: Long)

    @Query("DELETE FROM journal_entries WHERE isDemoEntry = 1")
    suspend fun deleteDemoJournals()

    @Query("SELECT COUNT(*) FROM journal_entries")
    fun getJournalCount(): Flow<Int>

    // --- Teacher Profiles ---
    @Query("SELECT * FROM teacher_profile WHERE isLoggedIn = 1 LIMIT 1")
    fun getProfileFlow(): Flow<TeacherProfile?>

    @Query("SELECT * FROM teacher_profile WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getProfileSync(): TeacherProfile?

    @Query("SELECT * FROM teacher_profile WHERE nip = :identifier OR email = :identifier LIMIT 1")
    suspend fun findProfileByIdentifier(identifier: String): TeacherProfile?

    @Query("SELECT * FROM teacher_profile WHERE isDemo = 1 LIMIT 1")
    fun getDemoProfileFlow(): Flow<TeacherProfile?>

    @Query("SELECT * FROM teacher_profile WHERE isDemo = 1 LIMIT 1")
    suspend fun getDemoProfileSync(): TeacherProfile?

    @Query("SELECT * FROM teacher_profile ORDER BY id DESC")
    fun getAllProfilesFlow(): Flow<List<TeacherProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: TeacherProfile): Long

    @Update
    suspend fun updateProfile(profile: TeacherProfile)

    @Query("UPDATE teacher_profile SET isLoggedIn = 0")
    suspend fun logoutAllProfiles()

    @Query("DELETE FROM teacher_profile WHERE isDemo = 1")
    suspend fun deleteDemoProfile()

    @Query("DELETE FROM teacher_profile WHERE id = :id")
    suspend fun deleteProfileById(id: Long)

    // --- Teaching Schedules ---
    @Query("SELECT * FROM teaching_schedules WHERE hari = :hari ORDER BY id ASC")
    fun getSchedulesForDay(hari: String): Flow<List<TeachingSchedule>>

    @Query("SELECT * FROM teaching_schedules ORDER BY id ASC")
    fun getAllSchedules(): Flow<List<TeachingSchedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<TeachingSchedule>)
}
