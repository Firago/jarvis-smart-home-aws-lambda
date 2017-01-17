package com.dfirago.jsh.aws.web.rest.service;

import com.dfirago.jsh.aws.web.rest.model.ActionRequest;
import com.dfirago.jsh.aws.web.rest.model.ActionResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by dmfi on 17/01/2017.
 */
public interface JarvisHubService {

    @POST("api/aws/action")
    Call<ActionResponse> postActionRequest(@Body ActionRequest request);
}
