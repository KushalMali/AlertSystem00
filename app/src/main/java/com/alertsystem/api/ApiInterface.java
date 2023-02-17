package com.alertsystem.api;

import com.alertsystem.models.BlockResponse;
import com.alertsystem.models.CommonResponse;
import com.alertsystem.models.CrimeResponse;
import com.alertsystem.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("register.php")
    Call<CommonResponse> registerUser(@Field("name") String name,
                                      @Field("mobile") String mobile,
                                      @Field("email") String email,
                                      @Field("address") String address,
                                      @Field("password") String password,
                                      @Field("usertype") String usertype);

    @FormUrlEncoded
    @POST("register.php")
    Call<CommonResponse> registerPolice(@Field("name") String name,
                                        @Field("mobile") String mobile,
                                        @Field("email") String email,
                                        @Field("address") String address,
                                        @Field("password") String password,
                                        @Field("usertype") String usertype,
                                        @Field("stname") String stationName,
                                        @Field("stmobile") String stationMobile,
                                        @Field("staddress") String stationAddress);

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> loginUser(@Field("email") String email,
                                  @Field("password") String password,
                                  @Field("usertype") String usertype);

    @FormUrlEncoded
    @POST("addCrime.php")
    Call<CommonResponse> addCrime(@Field("uid") String uid,
                                  @Field("area") String area,
                                  @Field("ctype") String ctype,
                                  @Field("ccount") String ccount,
                                  @Field("des") String des);

    @GET("getCrimeData.php")
    Call<CrimeResponse> getCrimeData(@Query("area") String area);

    @GET("getBlockData.php")
    Call<BlockResponse> getBlockData(@Query("area") String area,
                                     @Query("uid") String uid);

    @GET("forgetPassword.php")
    Call<LoginResponse> forgetPassword(@Query("mobile") String mobile,
                                       @Query("email") String email,
                                       @Query("usertype") String usertype);

}