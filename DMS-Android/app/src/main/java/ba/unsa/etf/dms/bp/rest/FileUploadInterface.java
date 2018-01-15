package ba.unsa.etf.dms.bp.rest;

import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileUploadInterface {
    @Multipart
    @POST("/documents/add")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part file, @Part("description") RequestBody description);
}
