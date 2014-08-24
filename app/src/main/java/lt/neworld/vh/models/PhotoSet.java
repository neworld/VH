package lt.neworld.vh.models;

import com.google.gson.annotations.SerializedName;

public class PhotoSet {
    public int page;
    public int pages;

    @SerializedName("perpage")
    public int perPage;

    public int total;

    public Photo[] photo;
}
