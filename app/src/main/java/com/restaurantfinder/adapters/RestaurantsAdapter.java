package com.restaurantfinder.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.restaurantfinder.R;
import com.restaurantfinder.manager.LocationManager;
import com.restaurantfinder.model.Restaurant;
import com.restaurantfinder.model.RestaurantList;
import com.restaurantfinder.startup.RestaurantApplication;
import com.restaurantfinder.utils.Utility;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Aditya on 13-Apr-16.
 */
public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> {

    private RestaurantList mRestaurantList;
    OnItemClickListener mItemClickListener;

    public class RestaurantItemHolder extends ViewHolder implements View.OnClickListener{
        @Bind(R.id.restaurantName)TextView name;
        @Bind(R.id.address)TextView address;
        @Bind(R.id.distance)TextView distance;
        @Bind(R.id.imageView)ImageView imageView;
        @Bind(R.id.layout)RelativeLayout layout;

        public RestaurantItemHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public void setContent(Restaurant item) {
            layout.setOnClickListener(this);

            name.setText(item.name);
            address.setText(item.address);
            if (!item.logo.equals("")) {
                Picasso.with(RestaurantApplication.getContext()).load(item.logo).
                        placeholder(R.drawable.placeholder).
                        into(imageView);
            } else {
                imageView.setImageResource(R.drawable.placeholder);
            }

            double distanced = Utility.getDistance(LocationManager.getInstance().getLatitude(), LocationManager.getInstance().getLongitude(),
                    item.latitude, item.longitude);

            distance.setText(String.format("%.2f", distanced) + " km(s)");
        }

        @Override
        public void onClick(View v) {
            Log.i("hello","clicked");
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(mRestaurantList.restaurantList.get(getAdapterPosition()));
            }
        }
    }

    public RestaurantsAdapter(RestaurantList list,OnItemClickListener listener){
        mItemClickListener = listener;
        mRestaurantList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurantitem_layout, parent, false);
        return new RestaurantItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RestaurantItemHolder mholder = (RestaurantItemHolder) holder;
        mholder.setContent(getItem(position));

    }

    @Override
    public int getItemCount() {
        if (mRestaurantList != null) {
            return mRestaurantList.restaurantList.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    private Restaurant getItem(int position){
        if(mRestaurantList!=null){
            return mRestaurantList.restaurantList.get(position);
        }
        return null;
    }

    public interface OnItemClickListener {
        public void onItemClick(Restaurant item);
    }
}
