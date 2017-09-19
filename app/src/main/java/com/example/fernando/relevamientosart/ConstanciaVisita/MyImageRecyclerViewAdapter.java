package com.example.fernando.relevamientosart.ConstanciaVisita;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fernando.relevamientosart.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Modelo.Image;


public class MyImageRecyclerViewAdapter extends RecyclerView.Adapter<MyImageRecyclerViewAdapter.ViewHolder> {

    private final List<Image> mValues;
    private final ImageFragment.OnImageListFragmentInteractionListener mListener;

    public MyImageRecyclerViewAdapter(Collection<Image> items, ImageFragment.OnImageListFragmentInteractionListener listener) {
        mValues = new ArrayList<>(items);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        Uri uri = Uri.parse(mValues.get(position).URLImage);


        holder.mImageView.setImageURI(uri);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onImagenPressed(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public Image mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = view.findViewById(R.id.ivImagen);
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
}
