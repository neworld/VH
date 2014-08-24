package lt.neworld.vh.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lt.neworld.vh.R;
import lt.neworld.vh.models.Photo;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoPreviewFragment extends Fragment{
    private static final String ARG_PHOTO = "photo";

    @InjectView(R.id.fr_photo_preview_image)
    PhotoView photoView;

    private PhotoViewAttacher photoViewAttacher;

    public static PhotoPreviewFragment createInstance(Photo photo) {
        Bundle args = new Bundle(1);
        args.putParcelable(ARG_PHOTO, photo);

        PhotoPreviewFragment fragment = new PhotoPreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_photo_preview, container, false);

        ButterKnife.inject(this, view);

        final Photo photo = getArguments().getParcelable(ARG_PHOTO);

        Picasso.with(getActivity()).load(photo.getUrl()).into(photoView);

        photoViewAttacher = new PhotoViewAttacher(photoView);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.reset(this);
    }
}
