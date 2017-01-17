package com.dfirago.jsh.aws.web.rest.service;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by dmfi on 17/01/2017.
 */
public class RestServiceFactory {

    private static final String API_BASE_URL = "http://www.jsh-hub1.duckdns.org";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private RestServiceFactory() {

    }

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}