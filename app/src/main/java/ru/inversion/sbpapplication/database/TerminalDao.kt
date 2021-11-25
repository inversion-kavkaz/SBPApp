package ru.inversion.sbpapplication.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Single
import ru.inversion.sbpapplication.pojo.Terminal

@Dao
interface TerminalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTerminal(terminal: Terminal) : Single<Long>

    @Query("SELECT * FROM terminal")
    fun getTerminalList(): LiveData<List<Terminal>>

    @Query("SELECT * FROM terminal where id = :id")
    fun getTerminalByID(id: Long): LiveData<Terminal>

    @Query("SELECT count(*) FROM terminal")
    fun getTerminalCount(): Single<Long>

    @Query("delete from terminal")
    fun clearTerminalTable()






}