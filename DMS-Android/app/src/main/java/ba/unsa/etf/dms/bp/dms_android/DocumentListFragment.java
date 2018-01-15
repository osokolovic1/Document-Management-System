package ba.unsa.etf.dms.bp.dms_android;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import ba.unsa.etf.dms.bp.dms_android.Adapters.DocumentInfoAdapter;
import ba.unsa.etf.dms.bp.rest.DocumentInfo;
import ba.unsa.etf.dms.bp.rest.DocumentInfoInterface;
import ba.unsa.etf.dms.bp.rest.DownloadDocumentInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static android.content.ContentValues.TAG;


public class DocumentListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private List<DocumentInfo> documentInfos;
    private DocumentInfoAdapter documentInfoAdapter;
    private ListView documentListView;
    private static String RETROFIT_BASE_URL = "http://192.168.0.17:8181/";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DocumentListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DocumentListFragment newInstance(int columnCount) {
        DocumentListFragment fragment = new DocumentListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        View view = inflater.inflate(R.layout.fragment_documentlist, container, false);

        getDocumentList(bundle.getString("NavType"));
        documentListView = (ListView) view.findViewById(R.id.document_list);

        documentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer docId = documentInfos.get(position).getId();
                getDocument(docId,true);
            }
        });

        documentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Integer docId = documentInfos.get(position).getId();
                getDocument(docId,false);
                return true;
            }
        });


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private DocumentInfoInterface getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RETROFIT_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final DocumentInfoInterface mInterfaceService = retrofit.create(DocumentInfoInterface.class);
        return mInterfaceService;
    }

    private DownloadDocumentInterface getInterfaceDownloadService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RETROFIT_BASE_URL)
                .build();
        final DownloadDocumentInterface mInterfaceService = retrofit.create(DownloadDocumentInterface.class);
        return mInterfaceService;
    }

    private  void getDocument(final Integer docId, final boolean preview) {
        DownloadDocumentInterface mApiService = this.getInterfaceDownloadService();
        String url = RETROFIT_BASE_URL + "documents/view/" + docId.toString();
        Call<ResponseBody> call = mApiService.downloadDocument(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {

                    InputStream inputStream = null;
                    OutputStream outputStream = null;
                    DocumentInfo docInfo = null;

                    for(int i = 0; i < documentInfos.size(); i++)
                        if(documentInfos.get(i).getId() == docId) {
                            docInfo = documentInfos.get(i);
                            break;
                        }
                    File myFile = null;
          /*          if(preview)
                        myFile = new File(getActivity().getFilesDir()+File.separator+docInfo.getName());
                    else*/
                        myFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+File.separator + docInfo.getName());

                    Log.d("CREATING FILE: ",myFile.getAbsolutePath());
                    try {
                        long fileSize = response.body().contentLength();
                        if(fileSize > 4194304)
                            Toast.makeText(getActivity(), "File size exceeds 4MB", Toast.LENGTH_LONG).show();

                        byte[] fileReader = new byte[4194304];
                        inputStream = response.body().byteStream();
                        outputStream = new FileOutputStream(myFile);

                        while(true) {
                            int read = inputStream.read(fileReader);

                            if(read == -1)
                                break;

                            outputStream.write(fileReader,0,read);

                        }

                        outputStream.flush();

                        if(preview) {
                            File file = myFile;
                            Log.d("READING FILE: ",getActivity().getFilesDir() +File.separator+ docInfo.getName());
                            MimeTypeMap map = MimeTypeMap.getSingleton();
                            String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
                            String type = docInfo.getType();

                            if(type == null)
                                type = "*/*";

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            Uri data = Uri.fromFile(file);

                            intent.setDataAndType(data,type);

                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "Download completed", Toast.LENGTH_LONG).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error downloading file.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getDocumentList(String type) {
        DocumentInfoInterface mApiService = this.getInterfaceService();
        Call<List<DocumentInfo>> mService = null;

        if(type.equals("MyDocuments")) {
            mService = mApiService.getListOfMyDocuments();
        } else if(type.equals("SharedDocuments")) {
            mService = mApiService.getListaDokumenata();
        } else {
            Toast.makeText(getActivity(), "Internal error", Toast.LENGTH_LONG).show();
            return;
        }

        mService.enqueue(new Callback<List<DocumentInfo>>() {
            @Override
            public void onResponse(Call<List<DocumentInfo>> call, Response<List<DocumentInfo>> response) {
                if(response.isSuccessful()) {
                    documentInfos = response.body();

                    documentInfoAdapter = new DocumentInfoAdapter(getActivity(),R.layout.document_list, documentInfos);
                    documentListView.setAdapter(documentInfoAdapter);

                } else {
                    Toast.makeText(getActivity(), "Error while getting document list.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<DocumentInfo>> call, Throwable t) {
                call.cancel();
                Toast.makeText(getActivity(), "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();
                Log.v("FAIL",t.getMessage());
            }
        });
    }
}
