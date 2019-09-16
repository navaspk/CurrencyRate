package com.luxoft.navaspk.currencyrate.model.network;

import com.luxoft.navaspk.currencyrate.presenter.Response;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface CurrencyService {

    @GET
    Observable<Response> geCurrencyRate(@Url String url);
}
