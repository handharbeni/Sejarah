package illiyin.mhandharbeni.sejarah;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import illiyin.mhandharbeni.sejarah.adapter.KategoriAdapter;
import illiyin.mhandharbeni.sejarah.model.KategoriModel;
import illiyin.mhandharbeni.sejarah.model.LokasiModel;
import illiyin.mhandharbeni.sejarah.tools.ClickListener;

public class MainActivity extends AppCompatActivity implements ClickListener {
    private static final String TAG = "MainActivity";
    private FirebaseFirestore db;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private EditText edtNama, edtAlamat, edtLatitude, edtLongitude, edtStar;
    private Button btnSimpan, btnDelete;

    private List<KategoriModel> kategoriModels;

    private KategoriAdapter kategoriAdapter;
    private RecyclerView listKategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermission();
    }

    private void fetch_component(){
        edtNama = (EditText)findViewById(R.id.edtNama);
        edtAlamat = (EditText) findViewById(R.id.edtAlamat);
        edtLatitude = (EditText) findViewById(R.id.edtLatitude);
        edtLongitude = (EditText) findViewById(R.id.edtLongitude);
        edtStar = (EditText) findViewById(R.id.edtStar);
        btnSimpan = (Button)findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LokasiModel lokasiModel = new LokasiModel();
                lokasiModel.setNama(edtNama.getText().toString());
                lokasiModel.setAlamat(edtAlamat.getText().toString());
                lokasiModel.setLatitude(edtLatitude.getText().toString());
                lokasiModel.setLongitude(edtLongitude.getText().toString());
                lokasiModel.setStar(edtStar.getText().toString());
                createDetailKategori("islam", lokasiModel);
            }
        });
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDataDetail("islam", edtNama.getText().toString());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetch_modules();
        fetch_recyclerview();
        fetch_adapter();
//        readKategori();
        registerListener();
    }

    private void fetch_recyclerview() {
        listKategori = (RecyclerView) findViewById(R.id.listKategori);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        listKategori.setLayoutManager(llm);
    }
    private void registerListener(){
        db.collection("kategorisejarah")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e!=null){
                            return;
                        }
                        kategoriModels.clear();
                        kategoriModels = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : documentSnapshots){
                            kategoriModels.add(new KategoriModel(documentSnapshot.getId().toString()));
                        }
                        update_adapter();
                    }
                });
    }
    private void readKategori(){
        kategoriModels.clear();
        kategoriModels = new ArrayList<>();
        db.collection("kategorisejarah")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                kategoriModels.add(new KategoriModel(documentSnapshot.getId().toString()));
                            }
                            update_adapter();
                        }
                    }
                });
    }
    private void fetch_adapter(){
        kategoriAdapter = new KategoriAdapter(this, kategoriModels, this);
        listKategori.setAdapter(kategoriAdapter);
    }

    private void update_adapter(){
        kategoriAdapter.updateData(kategoriModels);
    }

    public void fetch_modules(){
        kategoriModels = new ArrayList<>();
        init_firebase();
//        init_remoteconfig();
        init_cloudstore();
    }

    private void init_firebase(){
        FirebaseApp.initializeApp(this);
    }

    private void init_remoteconfig(){
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.fetch()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mFirebaseRemoteConfig.activateFetched();
                            getPaid();
                        }else{
                            Toast.makeText(MainActivity.this, "Fetch Failed", Toast.LENGTH_SHORT).show();
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
        Boolean paid = mFirebaseRemoteConfig.getBoolean("paid");
        if (paid){
//            Toast.makeText(MainActivity.this, "PAID", Toast.LENGTH_SHORT).show();
        }else{
//            Toast.makeText(MainActivity.this, "UNPAID", Toast.LENGTH_SHORT).show();
        }
    }

    private void init_cloudstore(){
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }


    private void readKategoriData(){
        db.collection("kategorisejarah")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                Log.d(TAG, "onComplete: Kategori"+documentSnapshot.getId());
                                readDataDetail(documentSnapshot.getId());
                            }
                        }
                    }
                });
    }

    private void onDataChange(){
        db.collection("kategorisejarah").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

            }
        });
    }
    private void readDataDetail(final String document){
        db.document("kategorisejarah/"+document)
                .collection("lokasi")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                Log.d(TAG, "onComplete: Detail Kategori "+document+" "+documentSnapshot.getId());
                            }
                        }
                    }
                });
    }
    private void createKategori(String namakategori){
        LokasiModel lokasiModel = new LokasiModel();
        db.collection("kategorisejarah")
                .document(namakategori)
                .set(lokasiModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Berhasil Menambahkan Kategori", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Gagal Menambahkan Kategori", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void createDetailKategori(String kategori, LokasiModel lokasiModel){
        db.collection("kategorisejarah")
                .document(kategori)
                .collection("lokasi")
                .document(lokasiModel.getNama())
                .set(lokasiModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Berhasil Menyimpan Detail Lokasi", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Gagal Menyimpan Detail Lokasi", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void deleteDataDetail(String kategori, String nama){
        db.collection("kategorisejarah")
                .document(kategori)
                .collection("lokasi")
                .document(nama)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Berhasil Menghapus Data", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Gagal Menghapus Data Detail", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void deleteData(final String nama){
        db.collection("sejarah")
                .document(nama)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DeleteSuccess: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "DeleteFaild: ");
                    }
                });
    }

    @Override
    public void onKategoriClick(KategoriModel kategoriModel) {
        Bundle bundle = new Bundle();
        bundle.putString("idKategori", kategoriModel.getNamakategori());

        Intent i = new Intent(this, DetailActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtras(bundle);

        startActivity(i);
        overridePendingTransition(R.anim.slide_up, R.anim.stay);
    }

    @Override
    public void onDetailClick(LokasiModel lokasiModel) {

    }

    private void initPermission(){
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_CHECKIN_PROPERTIES) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_WIFI_STATE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_CHECKIN_PROPERTIES,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        }, 1);
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
//                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 5);
//                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 6);
            }
        }
    }
}
