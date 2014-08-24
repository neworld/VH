package lt.neworld.vh.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.comcast.freeflow.core.AbsLayoutContainer;
import com.comcast.freeflow.core.FreeFlowContainer;
import com.comcast.freeflow.core.FreeFlowItem;
import com.comcast.freeflow.layouts.VLayout;

import java.lang.ref.SoftReference;

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

public class PhotoSetList extends Fragment implements AbsLayoutContainer.OnItemClickListener, SearchView.OnQueryTextListener {
    @InjectView(R.id.fr_photo_set_list_container)
    FreeFlowContainer container;

    @Inject
    FlickrApi flickrApi;

    private SoftReference<RecentPhotoSetAdapter> recentPhotoSetAdapterCache = new SoftReference<RecentPhotoSetAdapter>(null);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ObjectGraph.create(ProxyFlickrApiModule.class).inject(this);

        setHasOptionsMenu(true);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fr_photo_set, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_photo_set_search).getActionView();

        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setRecentPhotoAdapter();
                return true;
            }
        });
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
        if (recentPhotoSetAdapterCache.get() == null) {
            RecentPhotoSetAdapter adapter = new RecentPhotoSetAdapter();
            adapter.onLoadMoreData();

            recentPhotoSetAdapterCache = new SoftReference<RecentPhotoSetAdapter>(adapter);
        }

        container.setAdapter(recentPhotoSetAdapterCache.get());
    }

    private void setSearchPhotoAdapter(String term) {
        SearchPhotoSetAdapter adapter = new SearchPhotoSetAdapter(term);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.isEmpty(query)) {
            setSearchPhotoAdapter(query);
            return true;
        } else
            return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            setRecentPhotoAdapter();
            return true;
        } else
            return false;
    }

    private class SearchPhotoSetAdapter extends PhotoSetAdapter {
        private final String term;

        private int page;

        private SearchPhotoSetAdapter(String term) {
            this.term = term;
        }

        @Override
        protected void onLoadMoreData() {
            new AsyncTask<Integer, Void, PhotoSet>() {
                @Override
                protected PhotoSet doInBackground(Integer... params) {
                    return flickrApi.getSearchResult(term, params[0]).photos;
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
