package lt.neworld.vh.widget.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lt.neworld.vh.R;
import lt.neworld.vh.models.Photo;

public abstract class PhotoSetAdapter extends SectionedEndlessAdapter<Photo> {

    public PhotoSetAdapter() {
        setBeforeItemCount(8);
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        final Photo photo = getItem(position);

        final PhotoHolder photoHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.photo_list_item, parent, false);
            photoHolder = new PhotoHolder(convertView);
            convertView.setTag(photoHolder);
        } else {
            photoHolder = (PhotoHolder) convertView.getTag();
        }

        Picasso.with(context).load(photo.getUrl()).into(photoHolder.image);

        return convertView;
    }

    static class PhotoHolder {
        @InjectView(R.id.photo_list_item_image)
        ImageView image;

        public PhotoHolder (View convertView) {
            ButterKnife.inject(this, convertView);
        }
    }
}
