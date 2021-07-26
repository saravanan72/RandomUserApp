package com.ztask.sarvan.randomuser.data.localdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tbl_users")
data class  RandomUserEntities(

    @PrimaryKey
    val id:Int,

    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String?,

    @SerializedName("first")
    @ColumnInfo(name = "firstname")
    val first: String?,

    @SerializedName("last")
    @ColumnInfo(name = "lastname")
    val last: String?,

    @SerializedName("gender")
    @ColumnInfo(name = "gender")
    val gender: String?,

    @SerializedName("userimg")
    @ColumnInfo(name = "userimg")
    val userimg: String?,

    @SerializedName("mobile")
    @ColumnInfo(name = "mobile")
    val mobile: String?,

    @SerializedName("emailid")
    @ColumnInfo(name = "emailid")
    val emailid: String?,

    @SerializedName("dob")
    @ColumnInfo(name = "dob")
    val dob: String?,

    @SerializedName("age")
    @ColumnInfo(name = "age")
    val age: String?,

    @SerializedName("streetNo")
    @ColumnInfo(name = "streetNo")
    val streetNo: String?,

    @SerializedName("streetname")
    @ColumnInfo(name = "streetname")
    val streetname: String?,

    @SerializedName("city")
    @ColumnInfo(name = "city")
    val city: String?,

    @SerializedName("state")
    @ColumnInfo(name = "state")
    val state: String?,

    @SerializedName("country")
    @ColumnInfo(name = "country")
    val country: String?,

    @SerializedName("postalcode")
    @ColumnInfo(name = "postalcode")
    val postalcode: String?

)
