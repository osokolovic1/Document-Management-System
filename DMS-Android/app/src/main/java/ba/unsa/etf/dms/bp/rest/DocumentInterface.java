package ba.unsa.etf.dms.bp.rest;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Komp on 25.12.2017..
 */

public interface DocumentInterface {
    @GET("documents/shared/")
    Call<ListaDokumenata> getListaDokumenata();
}
