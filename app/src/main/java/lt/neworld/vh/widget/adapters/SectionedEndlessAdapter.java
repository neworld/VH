package lt.neworld.vh.widget.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.comcast.freeflow.core.FreeFlowItem;
import com.comcast.freeflow.core.Section;
import com.comcast.freeflow.core.SectionedAdapter;

import java.util.Collection;

/**
 * This adapter do not load more data automatically if this list is empty
 * @param <T>
 */
public abstract class SectionedEndlessAdapter<T> implements SectionedAdapter {
    private final Section section;

    private boolean loading;
    private boolean finished;

    private int beforeItemsCount = 1;

    public SectionedEndlessAdapter() {
        section = new Section();
    }

    public T getItem(int position) {
        return (T) section.getDataAtIndex(position);
    }

    public int getCount() {
        return section.getDataCount();
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    public void addItem(T item) {
        section.addItem(item);
    }

    public void addAllItems(T... items) {
        for (T item : items)
            section.addItem(item);
    }

    public void addAllItems(Collection<T> items) {
        for (T item : items)
            section.addItem(item);
    }

    @Override
    public final View getItemView(int section, int position, View convertView, ViewGroup parent) {
        if (position > getCount() - beforeItemsCount - 1)
            loadMoreData();

        return getItemView(position, convertView, parent);
    }

    public abstract View getItemView(int position, View convertView, ViewGroup parent);

    @Override
    public View getHeaderViewForSection(int section, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public final int getNumberOfSections() {
        return 1;
    }

    @Override
    public final Section getSection(int index) {
        return section;
    }

    @Override
    public final Class[] getViewTypes() {
        return new Class[] {FrameLayout.class };
    }

    @Override
    public final Class getViewType(FreeFlowItem proxy) {
        return FrameLayout.class;
    }

    @Override
    public final boolean shouldDisplaySectionHeaders() {
        return false;
    }

    public void loadMoreData() {
        if (finished || loading)
            return;

        onLoadMoreData();
    }

    protected final void notifyFinishedUpdate(boolean hasMore) {
        loading = false;
        finished = !hasMore;
    }

    /**
     * @param beforeItemsCount how much item must be until end of list for start loading another portion of data
     */
    protected void setBeforeItemCount(int beforeItemsCount) {
        this.beforeItemsCount = beforeItemsCount;
    }

    /**
     * this method is called in UI thread. Do shouldn't block UI thread
     * you must call {@link #notifyFinishedUpdate(boolean)} after finish loading
     */
    protected abstract void onLoadMoreData();
}
