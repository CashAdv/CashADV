package app.cashadvisor.customView.multipleCircleProgressBar.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.ColorRes

data class Progress(
    val id: Int,
    var value: Int,
    val maxValue: Int,
    @ColorRes val mainColor: Int,
    @ColorRes val backgroundColor: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(value)
        parcel.writeInt(maxValue)
        parcel.writeInt(mainColor)
        parcel.writeInt(backgroundColor)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Progress> {
        override fun createFromParcel(parcel: Parcel): Progress {
            return Progress(parcel)
        }

        override fun newArray(size: Int): Array<Progress?> {
            return arrayOfNulls(size)
        }
    }
}
