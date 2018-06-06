package com.benxiang.noodles.moudle.banner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.benxiang.noodles.MainActivity;
import com.benxiang.noodles.R;
import com.benxiang.noodles.base.BaseActivity;
import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.contants.MethodConstants;
import com.benxiang.noodles.model.Slider.SliderModel;
import com.benxiang.noodles.model.Slider.SliderParam;
import com.benxiang.noodles.model.Slider.SliderPresenter;
import com.benxiang.noodles.model.Slider.SliderView;
import com.benxiang.noodles.model.remote.CommonModel;
import com.benxiang.noodles.utils.JsonHelper;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

public class BannerActivity extends BaseActivity implements BannerView{
    @BindView(R.id.banner)
    Banner banner;
    private List<Integer> list=new ArrayList<>();
    BannerPresenter sliderPresenter;
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
//        initView();
        queryNoodles();
        getBanner();
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

    private void initView(List lists) {

        //banner本地图片数据（资源文件）
//        initData();
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        banner.setImages(lists)
                .setImageLoader(new GlideImageLoader())
                .setDelayTime(4000)
                .isAutoPlay(true)
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
       /* list.add(R.drawable.banner1);
        list.add(R.drawable.banner2);
        list.add(R.drawable.banner3);
        list.add(R.drawable.banner4);
        list.add(R.drawable.banner5);*/
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




    protected void getBanner() {

        //TODO 请求获取Banner图片信息  LINBIN
        //获取成本卡
        sliderPresenter = new BannerPresenter();
        sliderPresenter.attachView(this);
        SliderParam bannerReParameter = new SliderParam();
        bannerReParameter.Keys = MethodConstants.BANNER_KEYS;
        bannerReParameter.LID = MethodConstants.SHOPCODE;
        bannerReParameter.ContentType = MethodConstants.CONTENT_TYPE;
        sliderPresenter.getBanner(MethodConstants.BANNER, JsonHelper.getGson().toJson(bannerReParameter));

    }

    @Override
    public void getBannerSuccess(CommonModel<BannerModel> bannerModel) {
        String bannerDatas = JsonHelper.getGson().toJson(bannerModel.strMsg.AppBaseInfo.get(0).ADAddrs);
        List lists = new ArrayList();
        String[] strings ;
        //分割
        strings = bannerDatas.split(",");
        //去掉首尾的引号
        strings[0] = strings[0].substring(1);
        strings[strings.length-1] = strings[strings.length-1].substring(0,strings[strings.length-1].length()-1);
        //拼接并把其添加到list集合中
        for (int i = 0; i < strings.length; i++) {
            lists.add(Constants.BaseURL + strings[i].replaceAll(" +", ""));
            Log.d("listBanner"," : " + lists.get(i));
        }
        initView(lists);
    }
}
