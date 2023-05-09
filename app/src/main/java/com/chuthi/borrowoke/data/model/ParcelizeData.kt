package com.chuthi.borrowoke.data.model

import com.chuthi.borrowoke.base.BaseModel
import kotlinx.parcelize.Parcelize


sealed class ParcelizeData(open val value: Any?) : BaseModel() {

    @Parcelize
    data class IntData(
        override val value: Int
    ) : ParcelizeData(value)

    @Parcelize
    data class StringData(
        override val value: String
    ) : ParcelizeData(value)

    @Parcelize
    data class FloatData(
        override val value: Float
    ) : ParcelizeData(value)

    @Parcelize
    data class DoubleData(
        override val value: Double
    ) : ParcelizeData(value)

    @Parcelize
    data class NumberData(
        override val value: Number
    ) : ParcelizeData(value)

    @Parcelize
    data class BooleanData(
        override val value: Boolean
    ) : ParcelizeData(value)


    /**
     *  Parsing from [ParcelizeData] to normal data type.
     */
    inline fun <reified T> getRawValue() = value as? T
}
