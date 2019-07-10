package com.arthurbatista.register.retrofit;

import com.arthurbatista.register.service.RTService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {

    private static final String BASE_URL = "https://viacep.com.br/ws/";

    private final Retrofit retrofit;

    public RetrofitConfig() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public RTService getCEPService() { return this.retrofit.create(RTService.class); }
}
