package com.arthurbatista.register.service;

import com.arthurbatista.register.model.CEP;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RTService {

    @GET("{cep}/json")
    Call<CEP> findCEP(@Path("cep") String cep);
}
