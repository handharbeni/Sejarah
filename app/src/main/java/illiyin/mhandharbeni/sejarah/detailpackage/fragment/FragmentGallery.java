package illiyin.mhandharbeni.sejarah.detailpackage.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import illiyin.mhandharbeni.sejarah.R;

/**
 * Created by root on 2/10/18.
 */

public class FragmentGallery extends Fragment {
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_gallery, container, false);
        return v;
    }
}
