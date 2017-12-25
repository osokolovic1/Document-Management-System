package ba.unsa.etf.dms.bp.rest;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Komp on 24.12.2017..
 */

public interface LoginAndRegistration {
    @FormUrlEncoded
    @POST("/user/login/")
    Call<String> authenticate(@Field("username") String username, @Field("password") String password);

}
