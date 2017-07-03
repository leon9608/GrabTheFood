package com.example.leon.grabthefood.menusRelated;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.leon.grabthefood.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by leon on 5/1/17.
 */

public class menuAdapter extends RecyclerView.Adapter<menuAdapter.ViewHolder> {

    private ArrayList<String> menuesPath;
    private Context context;

    public menuAdapter(ArrayList<String> menuePaths, Context context) {
        this.menuesPath = menuePaths;
        this.context = context;
    }

    @Override
    public menuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        final View allmenueItem = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(allmenueItem);
    }

    @Override
    public void onBindViewHolder(final menuAdapter.ViewHolder holder, final int position) {
        String menuePath = menuesPath.get(position);
        Picasso.with(context).load(menuePath).into(holder.menue);
    }

    @Override
    public int getItemCount() {
        return menuesPath.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView menue;

        ViewHolder(View itemView) {
            super(itemView);
            menue = (ImageView) itemView.findViewById(R.id.menuePic);
        }
    }
}
