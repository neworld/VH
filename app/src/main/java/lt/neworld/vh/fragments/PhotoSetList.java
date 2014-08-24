package lt.neworld.vh.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comcast.freeflow.core.FreeFlowContainer;
import com.comcast.freeflow.layouts.VLayout;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.Module;
import dagger.ObjectGraph;
import lt.neworld.vh.R;
import lt.neworld.vh.Rest.FlickrApi;
import lt.neworld.vh.Rest.FlickrApiModule;
import lt.neworld.vh.models.PhotoSet;
import lt.neworld.vh.widget.adapters.PhotoSetAdapter;
import lt.neworld.vh.widget.layouts.VHLayout;

public class PhotoSetList extends Fragment {
    @InjectView(R.id.fr_photo_set_list_container)
    FreeFlowContainer container;

    @Inject
    FlickrApi flickrApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ObjectGraph.create(ProxyFlickrApiModule.class).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_set_list, container, false);

        ButterKnife.inject(this, view);

        this.container.setLayout(new VHLayout(getActivity()));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRecentPhotoAdapter();
    }

    private void setRecentPhotoAdapter() {
        RecentPhotoSetAdapter adapter = new RecentPhotoSetAdapter();
        adapter.onLoadMoreData();

        container.setAdapter(adapter);
    }

    private class RecentPhotoSetAdapter extends PhotoSetAdapter {
        private int page;

        @Override
        protected void onLoadMoreData() {
            new AsyncTask<Integer, Void, PhotoSet>() {
                @Override
                protected PhotoSet doInBackground(Integer... params) {
                    return flickrApi.getRecent(params[0]).photos;
                }

                @Override
                protected void onPostExecute(PhotoSet photoSet) {
                    addAllItems(photoSet.photo);
                    notifyFinishedUpdate(photoSet.perPage == photoSet.photo.length);
                    container.dataInvalidated(false);
                }
            }.execute(++page);
        }
    }

    @Module(
            injects = PhotoSetList.class,
            includes = FlickrApiModule.class
    )
    final static class ProxyFlickrApiModule {

    }
}
