package ru.inversion.sbpapplication.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import ru.inversion.sbpapplication.utils.convertTimestampToTime
import java.io.Serializable

@Entity(tableName = "response_result")
data class ResponseResult(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var responseDate: Long,
    var keyName: String? = "",
    var KeyValue: String? = "",
) : Serializable {
    fun getFormatedTime(): String {
        return convertTimestampToTime(responseDate)
    }
}
