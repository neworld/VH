package lt.neworld.vh.models;

public class Photo {
    public String id;
    public String owner;
    public String secret;
    public String server;
    public String farm;
    public String title;

    public String getUrl() {
        return String.format("https://farm%s.staticflickr.com/%s/%s_%s.jpg", farm, server, id, secret);
    }
}
