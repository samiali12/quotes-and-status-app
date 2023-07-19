package android.example.quotes_and_status.QoutesModel;

public class CategoryImageMode {

    String imgName;
    int imgId;

    public CategoryImageMode(String imgName, int imgId) {
        this.imgName = imgName;
        this.imgId = imgId;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
