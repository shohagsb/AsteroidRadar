package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM AsteroidTable WHERE closeApproachDate >= date(:today) ORDER BY date(closeApproachDate) ASC")
    fun getWeeksAsteroids(today: String): Flow<List<Asteroid>>

    @Query("SELECT * FROM AsteroidTable WHERE closeApproachDate = date(:today) ORDER BY date(closeApproachDate) ASC")
    fun getTodayAsteroids(today: String): Flow<List<Asteroid>>

    @Query("SELECT * FROM AsteroidTable ORDER BY date(closeApproachDate) ASC")
    fun getSavedAsteroids(): Flow<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids: Asteroid)

    @Query("DELETE FROM AsteroidTable WHERE closeApproachDate < date(:today)")
    suspend fun deletePreviousAsteroids(today: String)
}

@Database(entities = [Asteroid::class], version = 1)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidsDatabase::class.java,
                "videos"
            ).build()
        }
    }
    return INSTANCE
}