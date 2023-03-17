package com.chuthi.borrowoke.ext

import com.chuthi.borrowoke.base.BaseModel
import com.chuthi.borrowoke.data.model.ParcelizeData

fun Any.toParcelable(): BaseModel? {
    return when (this) {
        is BaseModel -> this
        is String -> ParcelizeData.ParcelizeString(this)
        is Int -> ParcelizeData.ParcelizeInt(this)
        is Float -> ParcelizeData.ParcelizeFloat(this)
        is Double -> ParcelizeData.ParcelizeDouble(this)
        is Number -> ParcelizeData.ParcelizeNumber(this)
        else -> null
    }
}