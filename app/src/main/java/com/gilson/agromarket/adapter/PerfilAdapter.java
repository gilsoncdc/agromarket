package com.gilson.agromarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gilson.agromarket.R;
import com.gilson.agromarket.model.Produto;

import java.util.List;

public class PerfilAdapter extends BaseAdapter {

    private Context context;
    private String listDescricao[];
    private int listIcon[];
    LayoutInflater inflater;

    public PerfilAdapter(Context ctx, String[] listDescricao, int[] listIcon) {
        this.context = ctx;
        this.listDescricao = listDescricao;
        this.listIcon = listIcon;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return listDescricao.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.item_perfil_list, null);

        TextView textDescricao = (TextView) view.findViewById(R.id.descricao);
        ImageView iconImg = (ImageView) view.findViewById(R.id.imgIcon);

        textDescricao.setText(listDescricao[i]);
        iconImg.setImageResource(listIcon[i]);

        return view;

    }
}
