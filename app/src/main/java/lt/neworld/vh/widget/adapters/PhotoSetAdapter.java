package lt.neworld.vh.widget.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
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

        photoHolder.title.setText(photo.title);
        photoHolder.author.setText(String.format("by %s", photo.ownerName));

        if (position % 4 == 0) {
            photoHolder.avatar.setVisibility(View.VISIBLE);
            photoHolder.title.setTextAppearance(context, R.style.TextPhotoTitle_Big);
            photoHolder.author.setTextAppearance(context, R.style.TextPhotoAuthor_Big);

            Picasso.with(context).load(photo.getAvatarUrl()).into(photoHolder.avatar);
        } else {
            photoHolder.avatar.setVisibility(View.INVISIBLE);
            photoHolder.title.setTextAppearance(context, R.style.TextPhotoTitle_Small);
            photoHolder.author.setTextAppearance(context, R.style.TextPhotoAuthor_Small);
        }

        return convertView;
    }

    static class PhotoHolder {
        @InjectView(R.id.photo_list_item_image)
        ImageView image;

        @InjectView(R.id.photo_list_item_avatar)
        RoundedImageView avatar;

        @InjectView(R.id.photo_list_item_title)
        TextView title;

        @InjectView(R.id.photo_list_item_author)
        TextView author;

        public PhotoHolder (View convertView) {
            ButterKnife.inject(this, convertView);
        }
    }
}
