package ru.inversion.sbpapplication.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.inversion.sbpapplication.utils.convertTimestampToTime
import java.io.Serializable

@Entity(tableName = "trn_list")
data class Transaction(
    @PrimaryKey
    @SerializedName("id")       @Expose val id: Long,
    @SerializedName("rnum")     @Expose val rnum: Long,
    @SerializedName("purpose")  @Expose val purpose: String? = "",
    @SerializedName("dat")      @Expose val dat: Long? = null,
    @SerializedName("mtrnsum")  @Expose val mtrnsum : Double? = 0.0,
    @SerializedName("qr_id")  @Expose val qrId : String? = "",
    @SerializedName("bank")  @Expose val bank : String? = "",
    @SerializedName("cur")  @Expose val cur : String? = "",
    @SerializedName("order_num")  @Expose val orderNum : String? = ""
) : Serializable
{
    fun getFormatedTime(): String {
        return convertTimestampToTime(dat)
    }
}
