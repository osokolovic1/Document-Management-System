package ba.unsa.etf.dms.bp.rest;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface DownloadDocumentInterface {
    @GET
    Call<ResponseBody> downloadDocument(@Url String fileUrl);
}
