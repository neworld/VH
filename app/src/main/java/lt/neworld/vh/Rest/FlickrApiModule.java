package lt.neworld.vh.Rest;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

@Module(
        library = true
)
public class FlickrApiModule {
    private static final String API_KEY = "7f79b92270a76e590e4bfded764a5b76";

    @Provides @Singleton FlickrApi providerFlickrApi() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.flickr.com")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addQueryParam("api_key", API_KEY);
                        request.addQueryParam("format", "json");
                        request.addQueryParam("nojsoncallback", "1");
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();

        return restAdapter.create(FlickrApi.class);
    }
}
