package com.jay.patientmonitoringapp

import android.os.Parcel
import android.os.Parcelable

data class Patient_Data_DB(
    var patientnum: String? = null,
    var firstname: String? = null,
    var lastname: String? = null,
    var gender: String? = null,
    var age: String? = null,
    var weight: String? = null,
    var height: String? = null,
    var imageUrl: String? = null,
    var uid: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        // Read values from parcel
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        // Write values to parcel
        parcel.writeString(patientnum)
        parcel.writeString(firstname)
        parcel.writeString(lastname)
        parcel.writeString(gender)
        parcel.writeString(age)
        parcel.writeString(weight)
        parcel.writeString(height)
        parcel.writeString(imageUrl)
        parcel.writeString(uid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Patient_Data_DB> {
        override fun createFromParcel(parcel: Parcel): Patient_Data_DB {
            return Patient_Data_DB(parcel)
        }

        override fun newArray(size: Int): Array<Patient_Data_DB?> {
            return arrayOfNulls(size)
        }
    }
}
