package illiyin.mhandharbeni.sejarah;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;

import illiyin.mhandharbeni.sejarah.adapter.LokasiAdapter;
import illiyin.mhandharbeni.sejarah.detailpackage.MainDetail;
import illiyin.mhandharbeni.sejarah.model.KategoriModel;
import illiyin.mhandharbeni.sejarah.model.LokasiModel;
import illiyin.mhandharbeni.sejarah.tools.ClickListener;

/**
 * Created by root on 2/10/18.
 */

public class DetailActivity extends AppCompatActivity implements ClickListener {

    private static final String TAG = "DetailActivity";
    private String idKategori;
    private FirebaseFirestore db;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private RecyclerView listDetail;
    private ArrayList<LokasiModel> listLokasi;
    private LokasiAdapter lokasiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetch_bundle();
        fetch_modul();
        fetch_component();
    }

    private void fetch_bundle(){
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null)
            idKategori = bundle.getString("idKategori");

        getSupportActionBar().setTitle("Sejarah "+idKategori);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    private void fetch_modul(){
        init_firebase();
        init_firestore();
//        init_remoteconfig();
    }

    private void fetch_component(){
        listDetail = (RecyclerView) findViewById(R.id.listDetail);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        GridLayoutManager glm = new GridLayoutManager(getApplicationContext(), 2);
        listDetail.setLayoutManager(glm);
        fetch_adapter();
        readData();
    }

    private void readData(){
        listLokasi = new ArrayList<>();
        readDataDetail(idKategori);
        registerListener(idKategori);
    }

    private void init_firebase(){
        FirebaseApp.initializeApp(this);
    }
    private void init_firestore(){
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }
    private void init_remoteconfig(){
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.fetch()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
//                            mFirebaseRemoteConfig.activateFetched();
//                            getPaid();
                        }else{
                            Toast.makeText(DetailActivity.this, "Fetch Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    private void getPaid(){
        String paid = mFirebaseRemoteConfig.getString("paid");
        if (paid.equalsIgnoreCase("true")){
            Toast.makeText(DetailActivity.this, "PAID", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(DetailActivity.this, "UNPAID", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerListener(final String document){
        db.document("kategorisejarah/"+document)
                .collection("lokasi")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e!=null){
                            return;
                        }
                        for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.MODIFIED) {
//                                Log.d(TAG, "New city: " + dc.getDocument().getData());
                                updateDataDetail(document);
                            }
                        }

                    }
                });
    }

    private void updateDataDetail(String document){
        listLokasi.clear();
        listLokasi = new ArrayList<>();
        db.document("kategorisejarah/"+document)
                .collection("lokasi")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                LokasiModel model = new LokasiModel();
                                model.setNama(documentSnapshot.get("nama").toString());
                                model.setAlamat(documentSnapshot.get("alamat").toString());
                                model.setLatitude(documentSnapshot.get("latitude").toString());
                                model.setLongitude(documentSnapshot.get("longitude").toString());
                                model.setStar(documentSnapshot.get("star").toString());
                                model.setDeskripsi(documentSnapshot.get("deskripsi").toString());
                                model.setThumbnail(documentSnapshot.get("thumbnail").toString());
                                listLokasi.add(model);
                            }
                            update_adapter();
                        }
                    }
                });
    }
    private void readDataDetail(final String document){
        listLokasi.clear();
        listLokasi = new ArrayList<>();
        db.document("kategorisejarah/"+document)
                .collection("lokasi")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                LokasiModel model = new LokasiModel();
                                model.setNama(documentSnapshot.get("nama").toString());
                                model.setAlamat(documentSnapshot.get("alamat").toString());
                                model.setLatitude(documentSnapshot.get("latitude").toString());
                                model.setLongitude(documentSnapshot.get("longitude").toString());
                                model.setStar(documentSnapshot.get("star").toString());
                                model.setDeskripsi(documentSnapshot.get("deskripsi").toString());
                                model.setThumbnail(documentSnapshot.get("thumbnail").toString());
                                listLokasi.add(model);
                            }
                            update_adapter();
                        }
                    }
                });
    }
    private void fetch_adapter(){
        listLokasi = new ArrayList<>();
        lokasiAdapter = new LokasiAdapter(this, listLokasi, this);
        listDetail.setAdapter(lokasiAdapter);
    }
    private void update_adapter(){
        lokasiAdapter.update(listLokasi);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    @Override
    public void onKategoriClick(KategoriModel kategoriModel) {

    }

    @Override
    public void onDetailClick(LokasiModel lokasiModel) {
        Bundle bundle = new Bundle();
        bundle.putString(MainDetail.NAMA, lokasiModel.getNama());
        bundle.putString(MainDetail.ALAMAT, lokasiModel.getAlamat());
        bundle.putString(MainDetail.LATITUDE, lokasiModel.getLatitude());
        bundle.putString(MainDetail.LONGITUDE, lokasiModel.getLongitude());
        bundle.putString(MainDetail.STAR, lokasiModel.getStar());
        bundle.putString(MainDetail.DESKRIPSI, lokasiModel.getDeskripsi());
        bundle.putString(MainDetail.THUMBNAIL, lokasiModel.getThumbnail());

        Intent i = new Intent(this, MainDetail.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtras(bundle);
        startActivity(i);
        overridePendingTransition(R.anim.slide_up, R.anim.stay);
    }
}
