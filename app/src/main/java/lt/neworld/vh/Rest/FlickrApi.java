package lt.neworld.vh.Rest;

import lt.neworld.vh.models.PhotoSetHolder;
import retrofit.http.GET;
import retrofit.http.Query;

public interface FlickrApi {
    @GET("/services/rest/?method=flickr.photos.search")
    public PhotoSetHolder getSearchResult(@Query("text") String searchTerm, @Query("page") int page);

    @GET("/services/rest/?method=flickr.photos.getRecent")
    public PhotoSetHolder getRecent(@Query("page") int page);
}
