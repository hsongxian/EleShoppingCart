package com.song.eleshoppingcart.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2016/10/7 0007.
 */
public interface OnShoppingItemClickListener {

    void onItemClick(View view, RecyclerView.ViewHolder holder, int position);
    void onAddClick(View view, int num, int position);
    void onMinusClick(View view, int num, int position);
}
