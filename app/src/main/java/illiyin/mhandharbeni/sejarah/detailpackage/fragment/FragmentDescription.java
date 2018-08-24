package illiyin.mhandharbeni.sejarah.detailpackage.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import illiyin.mhandharbeni.sejarah.R;
import illiyin.mhandharbeni.sejarah.model.LokasiModel;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 2/11/18.
 */

public class FragmentDescription extends Fragment {
    public static String DESK = "deskripsi";
    private String deskripsi;
    private View v;
    private TextView idDeskripsi;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_deskripsi, container, false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        fetch_argument();
        fetch_component();
        fetch_data();
    }

    public void fetch_argument(){
        Bundle argument = getArguments();
        deskripsi = argument.getString(FragmentDescription.DESK);
    }

    public void fetch_component(){
        idDeskripsi = v.findViewById(R.id.idDeskripsi);
    }

    public void fetch_data(){
        idDeskripsi.setText(deskripsi);
    }
}
