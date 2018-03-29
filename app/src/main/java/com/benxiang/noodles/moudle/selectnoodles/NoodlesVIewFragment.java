package com.benxiang.noodles.moudle.selectnoodles;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.benxiang.noodles.AppApplication;
import com.benxiang.noodles.R;
import com.benxiang.noodles.base.BaseFragment;
import com.benxiang.noodles.model.ListModle;
import com.benxiang.noodles.utils.PreferenceUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadmoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 刘圣如 on 2017/9/2.
 */

public class NoodlesVIewFragment extends BaseFragment {
    private static final String TAG = "NoodlesVIewFragment";
    private static final String PORT_TYPE = "noodle_type";
    private static final String EXCHANGE = "no_goods";
    @BindView(R.id.rv_goods_stail)
    RecyclerView recyclerView;
    private CommonAdapter<ListModle> mAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private LoadmoreWrapper mLoadMoreWrapper;
    private List<ListModle> goodsNewsBeanList = new ArrayList<>();
    SelectNoodlesActivity selectNoodlesActivity;


    @Override
    protected int contentViewID() {
        return R.layout.fragment_noodles_view;
    }

    //执行顺序:newInstance ==> onCreate() ==> onCreateView() ==> onViewCreated
    public static NoodlesVIewFragment newInstance(int portType, boolean exchange, List<ListModle> listModles) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("listModles", (ArrayList<? extends Parcelable>) listModles);
        NoodlesVIewFragment fragment = new NoodlesVIewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        goodsNewsBeanList = arguments.getParcelableArrayList("listModles");

        Log.e(TAG, "onCreate: " + goodsNewsBeanList.size());
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectNoodlesActivity = (SelectNoodlesActivity) getActivity();
        initView();
    }
    private void initView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        mAdapter = new CommonAdapter<ListModle>(getActivity(), R.layout.item_recycle_goods_big, goodsNewsBeanList) {
            @Override
            protected void convert(final ViewHolder holder, ListModle goodsNewsBean, final int position) {
                final ListModle goodsNewsBeans = goodsNewsBeanList.get(holder.getAdapterPosition());
                Log.e(TAG, "convert是否有库存: " + goodsNewsBeans.stock + holder.getAdapterPosition());
                holder.setText(R.id.tv_noodles_name, goodsNewsBeans.goods_name);
                holder.setText(R.id.tv_noodles_price, getString(R.string.money_desc, goodsNewsBeans.goods_prive));
                Log.e(TAG, "convert() returned: " +goodsNewsBean.ig_url);
                if (goodsNewsBean.stock){
                    holder.setImageUrl(AppApplication.getAppContext(),R.id.igbtn_noodles,goodsNewsBean.ImagePath, R.drawable.load_fail_large);
                }else {
                    holder.setBackgroundRes(R.id.igbtn_noodles, goodsNewsBean.ig_url);
                }
                PreferenceUtil.config().setBooleanValue(goodsNewsBeans.goods_no + "号", true);
                if (goodsNewsBeanList.get(holder.getAdapterPosition()).stock) {
                    holder.getView(R.id.igbtn_noodles).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e(".....", "onClick: " + holder.getAdapterPosition());
                            Log.e(".....", "onClick: " + position);
                            Log.e(".....", "onClick: " + goodsNewsBeanList.get(holder.getAdapterPosition()).stock);
                            if (goodsNewsBeanList.get(holder.getAdapterPosition()).stock && PreferenceUtil.config().getBooleanValue(goodsNewsBeans.goods_no+"号")) {
                                selectNoodlesActivity.refreshShop(goodsNewsBeanList.get(holder.getAdapterPosition()), holder.getAdapterPosition());
                                //点击一次后设置不可点击
                                PreferenceUtil.config().setBooleanValue(goodsNewsBeans.goods_no + "号", false);
                            }
                        }
                    });

                } else {
                    holder.setBackgroundRes(R.id.igbtn_noodles, goodsNewsBean.ig_url);
                }
            }
        };
//        initHeaderAndFooter();
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        mLoadMoreWrapper = new LoadmoreWrapper(mHeaderAndFooterWrapper);
        recyclerView.setAdapter(mLoadMoreWrapper);
    }

}
