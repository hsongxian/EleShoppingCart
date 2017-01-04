package com.song.eleshoppingcart.fragment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.song.eleshoppingcart.R;
import com.song.eleshoppingcart.ShopActivity;
import com.song.eleshoppingcart.bean.TestData;
import com.song.eleshoppingcart.base.BaseFragment;
import com.song.eleshoppingcart.bean.Commodity;
import com.song.eleshoppingcart.bean.TypeName;
import com.song.eleshoppingcart.listener.OnShoppingItemClickListener;
import com.song.eleshoppingcart.view.PromptTextView;
import com.song.eleshoppingcart.view.ShoppingAddView;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.MultiItemTypeAdapter;
import com.zhy.adapter.abslistview.base.BaseTurboAdapter;
import com.zhy.adapter.abslistview.base.ViewHolder;
import com.zhy.adapter.abslistview.decoration.PinnedHeaderDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 商品fragment
 * Created by Administrator on 2016/10/5 0005.
 */
public class FragmentCommodity extends BaseFragment {


    private static final long SHOW_HIDE_ANIM_DURATION = 200;
    @Bind(R.id.recycler_view_menu)
    RecyclerView mRecyclerViewMenu;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private List<TypeName> mType;

    private int mSelectPosition;
    public List<Commodity> mCommodity;
    private ListAdapter mListAdapter;
    private CommonAdapter<TypeName> mMenuAdapter;

    private OnShoppingItemClickListener mOnShoppingItemClickListener;

    public void setOnShoppingItemClickListener(OnShoppingItemClickListener onShoppingItemClickListener) {
        mOnShoppingItemClickListener = onShoppingItemClickListener;

    }
    public boolean get(){
        String s= "";
        Map<String,String> map =new HashMap<>();
        s.isEmpty();
        return !s.isEmpty();
    }
    public static FragmentCommodity newInstance() {
        FragmentCommodity fragment = new FragmentCommodity();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_commodity;
    }

    @Override
    protected void initView() {

        initCommodityList();
        initTypemenu();

    }

    private void initTypemenu() {
        mRecyclerViewMenu.setLayoutManager(new LinearLayoutManager(mContext));
//        mType = new TypeName();
        mType = new ArrayList<TypeName>();

        for (Commodity elementsBean : mCommodity) {
            if ("1".equals(elementsBean.getType())) {
                TypeName e = new TypeName();
                e.setName(elementsBean.getName());
                mType.add(e);
            }
        }
        mMenuAdapter = new CommonAdapter<TypeName>(
                mContext, R.layout.item_commodity_type_name, mType) {

            @Override
            public void convert(ViewHolder holder, TypeName elementsBean, int position) {
                boolean select = mSelectPosition == position;
                holder.setText(R.id.tv_name, elementsBean.getName());
                holder.itemView.setBackgroundResource(select ? R.color.white : R.color.appback);
                holder.setVisible(R.id.item_menu_view_select, select);
                ((TextView) holder.getView(R.id.tv_name)).getPaint().setFakeBoldText(select);
                PromptTextView promptTextView = holder.getView(R.id.tv_num);
                if (0 == elementsBean.getNumber())
                    promptTextView.setVisibility(View.GONE);
                else {
                    promptTextView.setText(String.valueOf(elementsBean.getNumber()));
                    promptTextView.setVisibility(View.VISIBLE);
                }
            }
        };

        mRecyclerViewMenu.setAdapter(mMenuAdapter);

        mMenuAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                mSelectPosition = position;
                mMenuAdapter.notifyDataSetChanged();
                int pos = mListAdapter.getItemPosition(mType.get(position).getName());
                if (pos != -1) {
                    mRecyclerView.scrollToPosition(pos);
                    LinearLayoutManager mLayoutManager =
                            (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(pos, 0);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initCommodityList() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1);
        mRecyclerView.addItemDecoration(decoration);

        mCommodity = new ArrayList<>();
        mCommodity = new ArrayList<>();
        for (int i = 0; i < TestData.ListMenu_STYLE.length; i++) {
            Commodity bean = new Commodity();
            bean.setType("1");
            bean.setName(TestData.ListMenu_STYLE[i]);
            bean.setPosition(i);
            bean.setContent("内容：" + TestData.ListMenu_STYLE[i]);
            mCommodity.add(bean);
            for (int j = 0; j < 6; j++) {
                String name = TestData.ListMenu_STYLE[i];
                Commodity elementsBean = new Commodity();
                elementsBean.setName(name + j);
                elementsBean.setPosition(i);
                elementsBean.setIcon(R.mipmap.ic_launcher);
                elementsBean.setContent(name + j);
                elementsBean.setSum("15" + j);
                elementsBean.setSales("月售19" + j + "  好评率8" + j + "%");
                mCommodity.add(elementsBean);
            }
        }
        ((ShopActivity)mContext).createBottomSheetDialog();
        mListAdapter = new ListAdapter(mContext);
        mRecyclerView.setAdapter(mListAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                Commodity elementsBean = mCommodity.get(firstPosition);
                int position = elementsBean.getPosition();
                if (firstPosition != -1 && mSelectPosition != position) {
                    LinearLayoutManager menuLayoutManager = (LinearLayoutManager) mRecyclerViewMenu.getLayoutManager();
                    if (position < menuLayoutManager.findFirstVisibleItemPosition()) {
                        menuLayoutManager.scrollToPosition(position);
                    } else if (position > menuLayoutManager.findLastVisibleItemPosition())
                        mRecyclerViewMenu.scrollToPosition(position);
                    mSelectPosition = position;
                    mMenuAdapter.notifyDataSetChanged();
                }
            }
        });

    }


    public class ListAdapter extends BaseTurboAdapter<Commodity, ViewHolder> {

        private int TYPE_TITLE = 1;
        private int TYPE_CONTENT = 2;
        private final LayoutInflater mInflater;

        public ListAdapter(Context context) {
            super(context, mCommodity);
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        protected int getDefItemViewType(int position) {
            boolean equals = "1".equals(mData.get(position).getType());
            return equals ? TYPE_TITLE : TYPE_CONTENT;
        }

        @Override
        protected ViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
            if (TYPE_TITLE == viewType)
                return new TitleVH(mInflater.inflate(R.layout.item_commodity_title, parent, false));
            else {
                ContentVH contentVH = new ContentVH(mInflater.inflate(R.layout.item_commodity_list, parent, false));
                if (mOnShoppingItemClickListener != null)
                    setListener(parent, contentVH);
                return contentVH;
            }
        }

        private void setListener(ViewGroup parent, final ContentVH viewHolder) {
            viewHolder.mRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = viewHolder.getAdapterPosition();
                    mOnShoppingItemClickListener.onItemClick(view, viewHolder, position);
                }
            });
            viewHolder.mShoppingAddView.setOnShoppingClickListener(new ShoppingAddView.OnShoppingClickListener() {
                @Override
                public void onAddClick(View view, int num) {
                    int position = viewHolder.getAdapterPosition();
                    mOnShoppingItemClickListener.onAddClick(view, num, Integer.valueOf(mData.get(viewHolder.getAdapterPosition()).getSum()));
                    updataMenuItem(num, position);

                }

                @Override
                public void onMinusClick(View view, int num) {
                    mOnShoppingItemClickListener.onMinusClick(view, num, Integer.valueOf(mData.get(viewHolder.getAdapterPosition()).getSum()));
                    updataMenuItem(num, viewHolder.getAdapterPosition());
                }
            });
        }

        @Override
        protected void convert(ViewHolder holder, Commodity item, int position) {
            boolean isTittle = holder instanceof TitleVH;
            if (isTittle) {
                TitleVH titleVH = (TitleVH) holder;
                titleVH.mTitle.setText(item.getName());
                titleVH.mTvContent.setText(item.getContent());
            } else {
                ContentVH contentVH = (ContentVH) holder;
                contentVH.mTv_Name.setText(item.getName());
                contentVH.mTvContent.setText(item.getContent());
                contentVH.mTvSales.setText(item.getSales());
                contentVH.mTvSum.setText(item.getSum());
            }
        }

        public int getItemPosition(String name) {
            for (int i = 0; i < mData.size(); i++) {
                Commodity elementsBean = mData.get(i);
                if (elementsBean.getName().equals(name))
                    return i;
            }
            return -1;
        }

    }

    private void updataMenuItem(int num, int position) {
        try {
            Commodity elementsBean = mCommodity.get(position);
            int dataPosition = elementsBean.getPosition();
            mMenuAdapter.getData().get(dataPosition).setNumber(num);
            mMenuAdapter.notifyItemChanged(dataPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class TitleVH extends ViewHolder {

        @Bind(R.id.title)
        TextView mTitle;
        @Bind(R.id.tv_content)
        TextView mTvContent;


        public TitleVH(View itemView) {
            super(itemView.getContext(), itemView);
            ButterKnife.bind(this, itemView);

        }
    }


    class ContentVH extends ViewHolder {
        @Bind(R.id.tv_name)
        TextView mTv_Name;
        @Bind(R.id.tv_content)
        TextView mTvContent;
        @Bind(R.id.tv_sales)
        TextView mTvSales;
        @Bind(R.id.tv_sum)
        TextView mTvSum;
        @Bind(R.id.shopping_view)
        ShoppingAddView mShoppingAddView;
        @Bind(R.id.item_root)
        View mRootView;

        public ContentVH(View itemView) {
            super(itemView.getContext(), itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
