package com.udacity.asteroidradar.Room

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Entity()
data class DatabaseAsteroid(
    @PrimaryKey
    val id: Long,
    val codename: String, val closeApproachDate: String,
    val absoluteMagnitude: Double, val estimatedDiameter: Double,
    val relativeVelocity: Double, val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroids(vararg asteroids: DatabaseAsteroid)

    @Query("select * from DatabaseAsteroid order by closeApproachDate Desc")
    fun refreshAsteroidList(): LiveData<List<DatabaseAsteroid>>


}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {

    abstract val Dao: AsteroidDao

}


private lateinit var Instance: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {

    synchronized(AsteroidDatabase::class) {

        if (!::Instance.isInitialized) {
            Instance = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "Asteroid-Database"
            ).fallbackToDestructiveMigration().build()
        }
        return Instance

    }
}
