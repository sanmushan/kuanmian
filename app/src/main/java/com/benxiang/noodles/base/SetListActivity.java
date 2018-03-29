package com.benxiang.noodles.base;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.benxiang.noodles.AppApplication;
import com.benxiang.noodles.R;
import com.benxiang.noodles.model.ListModle;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadmoreWrapper;

import butterknife.BindView;

/**
 * Created by 刘圣如 on 2017/9/4.
 */

public abstract class SetListActivity extends BaseActivity {
    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;
    private boolean isSetline=false;
    private CommonAdapter<ListModle> myAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private LoadmoreWrapper mLoadMoreWrapper;
    @Override
    public int getContentViewID() {
        return 0;
    }

    @Override
    protected void afterContentViewSet() {
        setRecycleView();
    }

    private void setRecycleView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(AppApplication.getAppContext()));
        if (isSetline) {
            mRecyclerView.addItemDecoration(new DividerItemDecoration(AppApplication.getAppContext(), DividerItemDecoration.VERTICAL_LIST));
        }
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(myAdapter);
        mLoadMoreWrapper = new LoadmoreWrapper(mHeaderAndFooterWrapper);
//        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setAdapter(mLoadMoreWrapper);
    }

    protected void setAdapter(CommonAdapter<ListModle> adapter,boolean isline) {
        myAdapter=adapter;
        isSetline=isline;
    }
    protected void freshen(int position){
        mLoadMoreWrapper.notifyItemRemoved(position);
//        mLoadMoreWrapper.notifyDataSetChanged();
    }
    protected void refreshAll(){
        mLoadMoreWrapper.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadMoreWrapper != null) {
            mLoadMoreWrapper = null;
        }
        if (mHeaderAndFooterWrapper != null) {
            mHeaderAndFooterWrapper=null;
        }
        if (myAdapter != null) {
            myAdapter=null;
        }
        if (mRecyclerView != null) {
            mRecyclerView=null;
        }
    }
}
