package com.chuthi.borrowoke.base

import androidx.recyclerview.widget.DiffUtil

/**************************************
- Created by Chuong Nguyen
- Email : chuong.nguyen@gumiviet.com
- Date : 10/17/2022
- Project : Chibijob
 **************************************/

class BaseDiffAdapter<T : BaseModel> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T) =
        oldItem.hashCode() == newItem.hashCode()
    //areSameItem.invoke(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}
