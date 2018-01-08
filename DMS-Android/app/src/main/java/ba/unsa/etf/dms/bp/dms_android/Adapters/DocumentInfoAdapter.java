package ba.unsa.etf.dms.bp.dms_android.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import ba.unsa.etf.dms.bp.dms_android.R;
import ba.unsa.etf.dms.bp.rest.DocumentInfo;

public class DocumentInfoAdapter extends ArrayAdapter<DocumentInfo> {

    private Context context;
    private int resource;
    private View view;
    private List<DocumentInfo> documentInfos;

    public DocumentInfoAdapter(Context context, int resource, List<DocumentInfo> documentInfos) {
        super(context,resource, documentInfos);
        this.context = context;
        this.resource = resource;
        this.documentInfos = documentInfos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(resource, parent, false);

        DocumentInfo documentInfo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.document_list, parent, false);
        }

        TextView textViewDocumentName = (TextView) convertView.findViewById(R.id.document_name);
        TextView textViewId = (TextView) convertView.findViewById(R.id.document_id);

        if(textViewDocumentName != null)
            textViewDocumentName.setText(documentInfo.getName().toString());
        if(textViewId != null)
            textViewId.setText(documentInfo.getId().toString());

        return convertView;
    }
}
