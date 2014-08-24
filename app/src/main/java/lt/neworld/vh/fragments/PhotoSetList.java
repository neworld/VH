package lt.neworld.vh.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comcast.freeflow.core.AbsLayoutContainer;
import com.comcast.freeflow.core.FreeFlowContainer;
import com.comcast.freeflow.core.FreeFlowItem;
import com.comcast.freeflow.layouts.VLayout;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.Module;
import dagger.ObjectGraph;
import lt.neworld.vh.R;
import lt.neworld.vh.Rest.FlickrApi;
import lt.neworld.vh.Rest.FlickrApiModule;
import lt.neworld.vh.models.Photo;
import lt.neworld.vh.models.PhotoSet;
import lt.neworld.vh.widget.adapters.PhotoSetAdapter;
import lt.neworld.vh.widget.layouts.VHLayout;

public class PhotoSetList extends Fragment implements AbsLayoutContainer.OnItemClickListener {
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
        this.container.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.reset(this);
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

    @Override
    public void onItemClick(AbsLayoutContainer parent, FreeFlowItem proxy) {
        getFragmentManager().beginTransaction()
                .addToBackStack("PhotoPreview")
                .add(R.id.root, PhotoPreviewFragment.createInstance((Photo) proxy.data))
                .commit();
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
