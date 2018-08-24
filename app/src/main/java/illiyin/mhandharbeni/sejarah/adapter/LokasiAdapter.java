package illiyin.mhandharbeni.sejarah.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import illiyin.mhandharbeni.sejarah.R;
import illiyin.mhandharbeni.sejarah.detailpackage.MainDetail;
import illiyin.mhandharbeni.sejarah.model.LokasiModel;
import illiyin.mhandharbeni.sejarah.tools.ClickListener;

/**
 * Created by root on 2/10/18.
 */

public class LokasiAdapter extends RecyclerView.Adapter<LokasiAdapter.ViewHolder> {
    private Context context;
    private ArrayList<LokasiModel> listLokasi;
    private ClickListener clickListener;

    public LokasiAdapter(Context context, ArrayList<LokasiModel> listLokasi, ClickListener clickListener) {
        this.context = context;
        this.listLokasi = listLokasi;
        this.clickListener = clickListener;
    }

    @Override
    public LokasiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lokasi, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LokasiAdapter.ViewHolder holder, int position) {
        final LokasiModel lokasiModel = listLokasi.get(position);
        holder.idNama.setText(lokasiModel.getNama());
        holder.idAlamat.setText(lokasiModel.getAlamat());
        Glide.with(context).load(lokasiModel.getThumbnail()).into(holder.imageView);
        holder.mainitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onDetailClick(lokasiModel);
            }
        });
    }

    public void update(ArrayList<LokasiModel> listLokasi){
        this.listLokasi.clear();
        this.listLokasi.addAll(listLokasi);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listLokasi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView mainitem;
        TextView idNama, idAlamat;
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            mainitem = itemView.findViewById(R.id.mainitem);
            idNama = itemView.findViewById(R.id.idNama);
            idAlamat = itemView.findViewById(R.id.idAlamat);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
