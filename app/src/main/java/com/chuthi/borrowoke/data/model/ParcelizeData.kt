package com.chuthi.borrowoke.data.model

import com.chuthi.borrowoke.base.BaseModel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ParcelizeData<T>(val value: @RawValue T?) : BaseModel()