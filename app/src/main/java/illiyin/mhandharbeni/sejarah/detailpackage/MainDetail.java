package illiyin.mhandharbeni.sejarah.detailpackage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import illiyin.mhandharbeni.sejarah.R;
import illiyin.mhandharbeni.sejarah.detailpackage.fragment.FragmentDescription;
import illiyin.mhandharbeni.sejarah.detailpackage.fragment.FragmentGallery;
import illiyin.mhandharbeni.sejarah.detailpackage.fragment.FragmentMaps;

/**
 * Created by root on 2/10/18.
 */

public class MainDetail extends AppCompatActivity {
    private static final String TAG = "MainDetail";
    private BottomNavigationView bottomNavigationView;
    private String nama, alamat, latitude, longitude, star, deskripsi, thumbnail;

    public static String NAMA = "nama";
    public static String ALAMAT = "alamat";
    public static String LATITUDE = "latitude";
    public static String LONGITUDE = "longitude";
    public static String STAR = "star";
    public static String DESKRIPSI = "deskripsi";
    public static String THUMBNAIL = "thumbnail";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_detail);
    }

    @Override
    protected void onStart() {
        super.onStart();
        init_bundle();
        fetch_component();
        init_fragment();
    }

    private void init_bundle(){
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null){
            nama = bundle.getString(NAMA);
            alamat = bundle.getString(ALAMAT);
            latitude = bundle.getString(LATITUDE);
            longitude = bundle.getString(LONGITUDE);
            star = bundle.getString(STAR);
            deskripsi = bundle.getString(DESKRIPSI);
            thumbnail = bundle.getString(THUMBNAIL);

            getSupportActionBar().setTitle(nama.toUpperCase());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 16908332:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    private void fetch_component(){
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Bundle bundle = new Bundle();
                        Fragment fragment;
                        switch (item.getItemId()) {
                            case R.id.actionmaps:
                                bundle.putDouble(FragmentMaps.LATITUDE, Double.valueOf(latitude));
                                bundle.putDouble(FragmentMaps.LONGITUDE, Double.valueOf(longitude));
                                fragment = new FragmentMaps();
                                fragment.setArguments(bundle);
                                changeFragment(fragment);
                                break;
//                            case R.id.actiongallery:
//                                fragment = new FragmentGallery();
//                                fragment.setArguments(bundle);
//                                changeFragment(fragment);
//                                break;
                            case R.id.actiondescription:
                                bundle.putString(FragmentDescription.DESK, deskripsi);
                                fragment = new FragmentDescription();
                                fragment.setArguments(bundle);
                                changeFragment(fragment);
                                break;
                        }
                        return true;
                    }
                });
    }

    private void init_fragment(){
        Bundle bundle = new Bundle();
        bundle.putDouble(FragmentMaps.LATITUDE, Double.valueOf(latitude));
        bundle.putDouble(FragmentMaps.LONGITUDE, Double.valueOf(longitude));
        Fragment fragment = new FragmentMaps();
        fragment.setArguments(bundle);
        changeFragment(fragment);
    }

    private void changeFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame,fragment);
        ft.commit();
    }
}
