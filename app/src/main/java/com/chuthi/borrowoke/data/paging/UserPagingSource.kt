package com.chuthi.borrowoke.data.paging

import com.chuthi.borrowoke.base.paging.BasePagingSource
import com.chuthi.borrowoke.data.model.UserModel

class UserPagingSource(
    private val queryAction: suspend (page: Int, size: Int, UserModel?)
    -> List<UserModel>?
) : BasePagingSource<UserModel>() {


    override fun onError(): (String) -> Unit = {

    }

    override fun query() = queryAction
}