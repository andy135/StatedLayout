package com.andiag.statedlayoutsample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by andyq on 30/12/2016.
 */

public class AdapterContent extends RecyclerView.Adapter<AdapterContent.Holder> {

    private List<ItemContent> contentList;
    private Context context;

    public AdapterContent(Context context, List<ItemContent> contentList) {
        this.contentList = contentList;
        this.context = context;
    }

    public void updateList(List<ItemContent> items) {
        this.contentList = items;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ItemContent item = contentList.get(position);

        holder.textView.setText(item.getText());
        holder.imageView.setImageResource(item.getImage());

    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        Holder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageItem);
            this.textView = (TextView) itemView.findViewById(R.id.textItem);
        }
    }
}
