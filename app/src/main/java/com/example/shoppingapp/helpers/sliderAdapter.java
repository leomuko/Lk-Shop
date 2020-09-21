package com.example.shoppingapp.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shoppingapp.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class sliderAdapter extends SliderViewAdapter<sliderAdapter.SliderAdapterVH> {

    private Context mContext;
    private List<String> mImageUrls = new ArrayList<>();

    public sliderAdapter(Context context){
        this.mContext = context;
    }
    public void addItem(String sliderItem){
        this.mImageUrls.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mage_slider_layout, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        String imageUrl = mImageUrls.get(position);
        Picasso.get().load(imageUrl).into(viewHolder.productImage);
    }

    @Override
    public int getCount() {
        return mImageUrls.size();
    }

    public class SliderAdapterVH extends SliderViewAdapter.ViewHolder{

        ImageView  productImage;
        public SliderAdapterVH(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_imageView);
        }
    }
}
