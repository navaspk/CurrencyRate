package com.luxoft.navaspk.currencyrate

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.luxoft.navaspk.currencyrate.presenter.Rates

class CurrencyRateAdapter(var mContext: Context,
                          var mSampleRate: Rates?) : RecyclerView.Adapter<BaseViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): BaseViewHolder {
        val layoutView = LayoutInflater.from(viewGroup.context).inflate(R.layout.each_item, null)
        return BaseViewHolder(layoutView)
    }

    override fun onBindViewHolder(baseViewHolder: BaseViewHolder, pos: Int) {

        var rate = mContext.getString(R.string.updating)
        if (pos == 0) {
            baseViewHolder.getTitle()?.text = mContext.getString(R.string.pln)
            if (mSampleRate != null) {
                rate = ""+mSampleRate?.pln
            }
            baseViewHolder.getSummary()?.text = "${mContext.getString(R.string.rate)} : $rate"
        } else {
            baseViewHolder.getTitle()?.text = mContext.getString(R.string.usd)
            if (mSampleRate != null) {
                rate = ""+mSampleRate?.usd
            }
            baseViewHolder.getSummary()?.text = "${mContext.getString(R.string.rate)} : $rate"
        }
    }

    override fun getItemCount(): Int {
        //since we are displaying only two content in recycler view, count is hard coded as 2
        return 2
    }

    fun updateContent(rate : Rates) {
        mSampleRate = rate
    }
}