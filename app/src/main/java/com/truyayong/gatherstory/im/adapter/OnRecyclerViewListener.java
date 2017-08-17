package com.truyayong.gatherstory.im.adapter;

/**
 * Created by alley_qiu on 2017/3/12.
 * 为RecyclerView添加点击事件
 */

public interface OnRecyclerViewListener {
    void onItemClick(int position);
    boolean onItemLongClick(int position);
}
