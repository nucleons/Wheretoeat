package com.restaurantfinder.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.restaurantfinder.R;
import com.restaurantfinder.adapters.RestaurantsAdapter;
import com.restaurantfinder.adapters.RestaurantsAdapter.OnItemClickListener;
import com.restaurantfinder.animation.ToolbarHidingOnScrollListener;
import com.restaurantfinder.manager.DownloadManager;
import com.restaurantfinder.model.Restaurant;
import com.restaurantfinder.model.RestaurantList;
import com.restaurantfinder.utils.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Aditya on 13-Apr-16.
 */
public class BreakfastFragment extends Fragment {

    @Bind(R.id.recyclerView)RecyclerView mRecyclerView;
    @Bind(R.id.progressbar)ProgressBar mProgressbar;
    private RestaurantsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    RestaurantItemClickListener mListener;

    public BreakfastFragment(){

    }

    public BreakfastFragment(RestaurantItemClickListener listener){
        mListener = listener;
    }

    public interface RestaurantItemClickListener{
        public void itemClicked(Restaurant item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.breakfast_layout, container, false);
        ButterKnife.bind(this, view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        setRecyclerView();
        return view;
    }

    private void setRecyclerView(){
        View tabBar = getActivity().findViewById(R.id.tabs);

        View toolbarContainer = getActivity().findViewById(R.id.appBarLayout);
        View toolbar = getActivity().findViewById(R.id.toolbar);

        //mRecyclerView.addOnScrollListener(new ToolbarHidingOnScrollListener(toolbarContainer, toolbar, tabBar,null));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    private void loadData(){
        mProgressbar.setVisibility(View.VISIBLE);
        DownloadManager manager = new DownloadManager();
        manager.getContent(Constants.URL, new DownloadManager.ContentDownloadListener() {
            @Override
            public void onContentDownloadSuccess(Object response) {
                RestaurantList list = (RestaurantList)response;
                mAdapter = new RestaurantsAdapter(list, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Restaurant item) {
                        //Log.i("hello","item clicked at "+position);
                        if(mListener!=null){
                            mListener.itemClicked(item);
                        }
                    }
                });
                mRecyclerView.setAdapter(mAdapter);
                mProgressbar.setVisibility(View.GONE);
            }

            @Override
            public void onContentDownloadFailure() {
                mProgressbar.setVisibility(View.GONE);
            }
        });
    }
}
