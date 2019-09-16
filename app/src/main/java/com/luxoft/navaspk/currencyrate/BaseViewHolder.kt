package com.luxoft.navaspk.currencyrate

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.each_item.view.*

class BaseViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    private var mTitle = itemView.heading
    private var mSummary = itemView.summary


    fun getTitle(): TextView? {
        return mTitle
    }

    fun getSummary(): TextView? {
        return mSummary
    }
}