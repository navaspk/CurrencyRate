package com.luxoft.navaspk.currencyrate.presenter

import android.content.Context
import android.view.View
import com.luxoft.navaspk.currencyrate.CurrencyRateActivity
import com.luxoft.navaspk.currencyrate.CurrencyRateApplication
import com.luxoft.navaspk.currencyrate.model.network.CurrencyService
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CurrencyRatePresenter(var mUpdate : UpdateView) {

    private var mCurrencyService: CurrencyService? = null

    fun makeNetworkCallForResponse(context : Context) {
        if ((context as CurrencyRateActivity).isNetworkConnected()) {
            mCurrencyService = (context?.getApplication() as CurrencyRateApplication).getRetrofit()

            val url = "https://api.exchangeratesapi.io/latest"
            val observable = mCurrencyService!!.geCurrencyRate(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
            observable.subscribe(object : Observer<Response> {
                override fun onSubscribe(d: Disposable) {}

                override fun onNext(response: Response) {
                    if (response != null) {
                        mUpdate.showUpdatedContent(response)
                        mUpdate.showProgress(View.GONE)
                    }
                }

                override fun onError(e: Throwable) {
                    // in case any kind error we are showing snack bar
                    mUpdate?.showProgress(View.GONE)
                    mUpdate?.sowSnackBarOnIssue(false, true)
                }

                override fun onComplete() {}
            })
        } else {
            mUpdate?.sowSnackBarOnIssue(true, true)
        }
    }


}

interface UpdateView {
    fun showProgress(visibility: Int)
    fun showUpdatedContent(response: Response)
    fun sowSnackBarOnIssue(noNetwork : Boolean, noResponse: Boolean)
}
