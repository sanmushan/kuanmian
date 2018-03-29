package com.benxiang.noodles.moudle.banner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.benxiang.noodles.MainActivity;
import com.benxiang.noodles.R;
import com.benxiang.noodles.base.BaseActivity;
import com.benxiang.noodles.utils.JsonHelper;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

public class BannerActivity extends BaseActivity {
    @BindView(R.id.banner)
    Banner banner;
    private List<Integer> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        float xdpi = getResources().getDisplayMetrics().xdpi;
        float ydpi = getResources().getDisplayMetrics().ydpi;
        Log.e("分辨率", "onCreate: "+xdpi+"..........."+ydpi);
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_banner;
    }

    @Override
    protected void afterContentViewSet() {
        setTvCountdownVisible(false);
        initView();
        queryNoodles();
    }

    private void queryNoodles() {
      /*  ArrayList<RiceND> list = DBNoodleHelper.queryNoTypeNoolde(1);
        RiceND riceND = DBNoodleHelper.queryNooldeNum(1);
        DBNoodleHelper.upateNoodleNum(1,riceND.totalNum-1);*/
        Log.e("同号米粉库存", "queryNoodles: "+list.size() );
    }

    @Override
    protected void onStart() {
        super.onStart();
//        initView();
    }

    private void initView() {

        //banner本地图片数据（资源文件）
        initData();
        banner.setImages(list)
                .setImageLoader(new GlideImageLoader())
                .setDelayTime(4000)
                .start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

//                Toast.makeText(BannerActivity.this, "哈哈哈", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(BannerActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("riceOrderND",mRiceOrderND);
                Timber.e("获得的RiceOrderND:"+ JsonHelper.getGson().toJson(mRiceOrderND));
                it.putExtras(bundle);
//                Intent it = new Intent(BannerActivity.this, SeasoningPackageActivity.class);
                startActivity(it);
            }
        });
    }

    private void initData() {
        list.add(R.drawable.banner1);
        list.add(R.drawable.banner2);
        list.add(R.drawable.banner3);
        list.add(R.drawable.banner4);
        list.add(R.drawable.banner5);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (list != null) {
            list.clear();
            list=null;
        }
        if (banner != null) {
            banner=null;
        }
    }
}
