package com.song.eleshoppingcart.fragment;


import com.song.eleshoppingcart.R;
import com.song.eleshoppingcart.base.BaseFragment;

/**
 * Created by Administrator on 2016/10/6 0006.
 */
public class FragmentMerchantInfo extends BaseFragment {

    public static FragmentMerchantInfo newInstance() {
        FragmentMerchantInfo fragment = new FragmentMerchantInfo();
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_merchantinfo;
    }
}
