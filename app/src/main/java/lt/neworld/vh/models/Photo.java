package lt.neworld.vh.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {
    public String id;
    public String owner;
    public String secret;
    public String server;
    public String farm;
    public String title;

    public Photo() {

    }

    public Photo(Parcel source) {
        id = source.readString();
        owner = source.readString();
        secret = source.readString();
        server = source.readString();
        farm = source.readString();
        title = source.readString();
    }

    public String getUrl() {
        return String.format("https://farm%s.staticflickr.com/%s/%s_%s.jpg", farm, server, id, secret);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(owner);
        dest.writeString(secret);
        dest.writeString(server);
        dest.writeString(farm);
        dest.writeString(title);
    }

    public static Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
