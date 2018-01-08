package ba.unsa.etf.dms.bp.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Komp on 25.12.2017..
 */

public interface DocumentInfoInterface {
    @GET("/documents/shared/")
    Call<List<DocumentInfo>> getListaDokumenata();

    @GET("/documents/my-documents/")
    Call<List<DocumentInfo>> getListOfMyDocuments();
}
