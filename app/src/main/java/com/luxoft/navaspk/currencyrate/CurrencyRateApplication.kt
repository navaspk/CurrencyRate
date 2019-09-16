package com.luxoft.navaspk.currencyrate

import android.app.Application
import android.content.Context
import com.luxoft.navaspk.currencyrate.model.network.CurrencyService
import com.luxoft.navaspk.currencyrate.model.network.NetworkConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CurrencyRateApplication : Application() {

    internal var mCurrencyService: CurrencyService? = null

    override fun onCreate() {
        super.onCreate()
        getRetrofit()
    }

    fun getRetrofit(): CurrencyService? {
        if (mCurrencyService == null)
            mCurrencyService = ApiServiceFactory.initiateCurrencyService(this)
        return mCurrencyService
    }

    internal object ApiServiceFactory {

        private var okHttpClient: OkHttpClient? = null
        private val REQUEST_TIMEOUT = 60

        fun initiateCurrencyService(context: Context): CurrencyService {

            if (okHttpClient == null)
                initOkHttp()

            val retrofit = Retrofit.Builder()
                .baseUrl(NetworkConstants.BASE_URL)
                .client(okHttpClient!!)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            return retrofit.create(CurrencyService::class.java!!)
        }

        private fun initOkHttp() {
            val httpClient = OkHttpClient().newBuilder()
                .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            httpClient.addInterceptor(interceptor)

            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()

                val request = requestBuilder.build()
                chain.proceed(request)
            }

            okHttpClient = httpClient.build()
        }

    }
}