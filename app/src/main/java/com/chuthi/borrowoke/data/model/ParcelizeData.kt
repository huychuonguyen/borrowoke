package com.chuthi.borrowoke.data.model

import com.chuthi.borrowoke.base.BaseModel
import kotlinx.parcelize.Parcelize


sealed class ParcelizeData(open val value: Any?) : BaseModel() {

    @Parcelize
    data class ParcelizeInt(
        override val value: Int
    ) : ParcelizeData(value)

    @Parcelize
    data class ParcelizeString(
        override val value: String
    ) : ParcelizeData(value)

    @Parcelize
    data class ParcelizeFloat(
        override val value: Float
    ) : ParcelizeData(value)

    @Parcelize
    data class ParcelizeDouble(
        override val value: Double
    ) : ParcelizeData(value)

    @Parcelize
    data class ParcelizeNumber(
        override val value: Number
    ) : ParcelizeData(value)

    @Parcelize
    data class BooleanData(
        override val value: Boolean
    ) : ParcelizeData(value)


    // parsing to normal data type
    inline fun <reified T> getRawValue(): T? {
        return value as? T
    }
}
