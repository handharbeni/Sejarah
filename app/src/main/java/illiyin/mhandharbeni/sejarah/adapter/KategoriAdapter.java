package illiyin.mhandharbeni.sejarah.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import illiyin.mhandharbeni.sejarah.DetailActivity;
import illiyin.mhandharbeni.sejarah.R;
import illiyin.mhandharbeni.sejarah.model.KategoriModel;
import illiyin.mhandharbeni.sejarah.tools.ClickListener;

/**
 * Created by root on 2/10/18.
 */

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.ViewHolder>{
    private Context context;
    private List<KategoriModel> kategoriModel;
    private ClickListener clickListener;

    public KategoriAdapter(Context context, List<KategoriModel> kategoriModel, ClickListener clickListener) {
        this.context = context;
        this.kategoriModel = kategoriModel;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kategori, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final KategoriModel kModel = kategoriModel.get(position);
        holder.idKategori.setText(kModel.getNamakategori().toUpperCase());
        holder.mainitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onKategoriClick(kModel);
            }
        });
        holder.mainitem.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//                contextMenu.setHeaderTitle("MENU");
//                contextMenu.add
//                contextMenu.add(0, view.getId(), kModel.getNamakategori(), "EDIT");
//                contextMenu.add(0, view.getId(), kModel.getNamakategori(), "DELETE");
            }
        });
    }

    @Override
    public int getItemCount() {
        return kategoriModel.size();
    }

    public void updateData(List<KategoriModel> kategoriModel){
        this.kategoriModel.clear();
        this.kategoriModel.addAll(kategoriModel);
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView mainitem;
        TextView idKategori;
        public ViewHolder(View itemView) {
            super(itemView);
            mainitem = itemView.findViewById(R.id.mainitem);
            idKategori = itemView.findViewById(R.id.idKategori);
        }
    }
}
