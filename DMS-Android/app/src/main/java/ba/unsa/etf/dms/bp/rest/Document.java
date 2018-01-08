package ba.unsa.etf.dms.bp.rest;

/**
 * Created by Komp on 25.12.2017..
 */

public class Document {
    private String documentName;

    public Document(String name) {
        this.documentName = name;
    }
    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
}
