package android.example.quotes_and_status.QoutesModel;

import java.util.ArrayList;

public class QuotesModel {

    public String dataId;
    public String data;
    public boolean isLiked;

    private ArrayList<QuotesModel> list;

    public QuotesModel(String dataId, String data, boolean isLiked) {
        this.dataId = dataId;
        this.data = data;
        this.isLiked = isLiked;
    }


    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getData() {
        return data;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public void setData(String data) {
        this.data = data;
    }

}
