package com.chuthi.borrowoke.data.model.response

import android.os.Parcel
import android.os.Parcelable
import com.chuthi.borrowoke.base.BaseModel
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
) : BaseModel()

data class Article(
    var id: Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
) : BaseModel()

data class Source(
    val id: Any,
    val name: String
)

class ExternalClass(val value: Int)

object ExternalClassParceler: Parceler<ExternalClass> {
    override fun create(parcel: Parcel): ExternalClass {
        return ExternalClass(parcel.readInt())
    }

    override fun ExternalClass.write(parcel: Parcel, flags: Int) {
        parcel.writeInt(value)
    }

}

@Parcelize
@TypeParceler<ExternalClass,ExternalClassParceler>
class MyClass(val externalClass: ExternalClass) : Parcelable