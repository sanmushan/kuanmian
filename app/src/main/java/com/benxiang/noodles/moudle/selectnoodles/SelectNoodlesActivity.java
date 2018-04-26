package com.benxiang.noodles.moudle.selectnoodles;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.benxiang.noodles.AppApplication;
import com.benxiang.noodles.BuildConfig;
import com.benxiang.noodles.R;
import com.benxiang.noodles.base.SetListActivity;
import com.benxiang.noodles.contants.DbTypeContants;
import com.benxiang.noodles.data.DBNoodleHelper;
import com.benxiang.noodles.data.table.RiceND;
import com.benxiang.noodles.model.ListModle;
import com.benxiang.noodles.model.NoodleTradeModel;
import com.benxiang.noodles.moudle.config.MenageActivity;
import com.benxiang.noodles.moudle.pay.PaymentActivity;
import com.benxiang.noodles.utils.PreferenceUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

/**
 *
 * @author 刘圣如
 * @date 2017/8/29
 */

public class SelectNoodlesActivity extends SetListActivity implements View.OnClickListener {

    private static final String TAG = "SelectNoodlesActivity";
    @BindView(R.id.btn_noodle)
    RadioButton btnnoodles;
    @BindView(R.id.btn_rice)
    RadioButton btnrice;
    @BindView(R.id.btn_kuanMian)
    RadioButton btnKuanMian;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.testBtn)
    Button testBtn;
    /**
     * 左边物品面的Fragment
     */
    private NoodlesVIewFragment noodle_Fragment;
    private NoodleTradeModel mNoodleTradeModel;
    /**
     * 左边物品米粉的Fragment
     */
    private NoodlesVIewFragment rice_Fragment;
    /**
     * 新鲜面
     */
    private NoodlesVIewFragment fresh_Fragment;
    /**
     * 面和粉两个Fragment的数组
     */
    private Fragment[] listFragment;
    /**
     * 判断点击的RadioButton是否是同一个
     */
    private int mIndex;
    /**
     * 保存从MainActivity传过来的粉的数据
     */
    private List<ListModle> listModlesRice = new ArrayList<>();
    /**
     * 保存从MainActivity传过来的面的数据
     */
    private List<ListModle> listModlesNoodle = new ArrayList<>();

    private List<ListModle> listModlesFresh = new ArrayList<>();
    private FragmentTransaction ft;

    private CommonAdapter<ListModle> mAdapter;
    /**
     * 保存goodsNewsBeanArrayMap中的value值，用于recycleview的展示
     */
    private List<ListModle> goodsNewsBeanList = new ArrayList<>();

    /**
     * 米粉和面条的数量
     */
    private int riceNum = 0;
    private int noodleNum = 0;
    private int freshNum = 0;
    /**
     * 统计鸡蛋/鸡腿/香辣包的数量
     */
    private int spicyNum = 0;
    private int legNum = 0;
    private int eggNum = 0;
    private int brineNum = 0;

    @Override
    public int getContentViewID() {
        return R.layout.activity_select_noodles;
    }

    @Override
    protected void afterContentViewSet() {
        setEnableCountdown(true);
        initDatas();
        initView();
        registerMainHandler();
        //执行顺序:setAdapter==>setRecyclerView
        super.afterContentViewSet();

        if (BuildConfig.DEBUG){
            testBtn.setVisibility(View.VISIBLE);
            testBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SelectNoodlesActivity.this, MenageActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

    private void initView() {
        initFragment();
    }

    private void initDatas() {
        //获取MainActivity.class中的数据
        NoodleTradeModel modelNoodle = getIntent().getParcelableExtra("noodle");
        NoodleTradeModel modelRice = getIntent().getParcelableExtra("rice");

        //todo lin---------------------------------------------------------------------------------3.26
        NoodleTradeModel modelFresh = getIntent().getParcelableExtra("fresh");

        if (modelRice == null || modelNoodle == null || modelFresh == null){
            return;
        }
        listModlesFresh = modelFresh.listModles;

        listModlesRice = modelRice.listModles;
        listModlesNoodle = modelNoodle.listModles;
        mNoodleTradeModel = new NoodleTradeModel();
        //初始化Adapter
        mAdapter = new CommonAdapter<ListModle>(this, R.layout.item_recycke_shop_cart, goodsNewsBeanList) {

            @Override
            protected void convert(final ViewHolder holder, final ListModle goodsNewsBean, final int position) {
                final int[] goods_num = {goodsNewsBean.goods_num};
                holder.setText(R.id.tv_noodles_shop_name, goodsNewsBean.goods_name);
                holder.setText(R.id.tv_noodles_shop_price, getString(R.string.money_desc, goodsNewsBean.goods_prive));
                holder.setText(R.id.tv_show_num, "" + goods_num[0]);
                holder.setImageUrl(AppApplication.getAppContext(),R.id.igbtn_noodles,goodsNewsBean.SmallImagePath,R.drawable.load_fail_small);
                holder.setOnClickListener(R.id.btn_num_dec, new View.OnClickListener() {
                    @SuppressLint("BinaryOperationInTimber")
                    @Override
                    public void onClick(View v) {
                        goodsNewsBean.goods_num--;
                        if (goodsNewsBean.goods_num >= 0) {
                            if (goodsNewsBean.goods_num == 0) {
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Timber.e("物品编号" + goodsNewsBean.goods_no);
                                //设置左边物品列表可点击添加
                                /*NoodlesVIewFragment noodlesVIewFragments= (NoodlesVIewFragment) listFragment[FragmentId];
                                noodlesVIewFragments.setOnclikEnable(true);*/
                                PreferenceUtil.config().setBooleanValue(goodsNewsBean.goods_no + "号", true);
                                goodsNewsBeanList.remove(holder.getAdapterPosition());

                            }
                            refreshAll();
                            onChange();
                        }

                    }
                });
                holder.setOnClickListener(R.id.btn_num_inc, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goodsNewsBean.goods_num++;
                        refreshAll();
                        onChange();
                    }
                });
            }
        };
        setAdapter(mAdapter, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        goodsNewsBeanList = null;
    }

    private void initFragment() {
        fresh_Fragment = NoodlesVIewFragment.newInstance(3, false, listModlesFresh);
        rice_Fragment = NoodlesVIewFragment.newInstance(2, false, listModlesRice);
        noodle_Fragment = NoodlesVIewFragment.newInstance(1, false, listModlesNoodle);

        listFragment = new Fragment[]{noodle_Fragment, rice_Fragment , fresh_Fragment};
        btnnoodles.setOnClickListener(this);
        btnrice.setOnClickListener(this);
        btnKuanMian.setOnClickListener(this);
        //开启事务
        ft = getSupportFragmentManager().beginTransaction();

        //添加首页
        ft.add(R.id.fl_goods, noodle_Fragment).commit();

        //默认设置为第0个
        setIndexSelected(0);
    }





    //添加订单
    public void refreshShop(ListModle shopGoodsList, int adapterPosition) {

        ListModle goodsNewsBean = shopGoodsList;
        goodsNewsBean.goods_num = 1;
        goodsNewsBeanList.add(shopGoodsList);
        Log.e(TAG, "shopGoodsList: " + shopGoodsList.goods_num);
        refreshAll();
        onChange();

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_noodle:
                setIndexSelected(0);
                break;
            case R.id.btn_rice:
                setIndexSelected(1);
                break;

            case R.id.btn_kuanMian:
                setIndexSelected(2);
                break;

                default:
        }
    }

    private void setIndexSelected(int index) {

        if (mIndex == index) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        //隐藏
        ft.hide(listFragment[mIndex]);
        //判断是否添加
        if (!listFragment[index].isAdded()) {
            ft.add(R.id.fl_goods, listFragment[index]).show(listFragment[index]);
        } else {
            ft.show(listFragment[index]);
        }

        ft.commit();
        //再次赋值
        mIndex = index;

    }

    /**
     * 商品数量和金额改变处理
     */
    public void onChange() {
        int sum = 0;
        float total = 0;
        for (int i = 0; i < goodsNewsBeanList.size(); i++) {
            ListModle goodsNewsBean = goodsNewsBeanList.get(i);
            sum += goodsNewsBean.goods_num;
            total += goodsNewsBean.goods_prive * (float) goodsNewsBean.goods_num;
            Timber.e("总金额"+total);
        }
        mNoodleTradeModel.listModles = goodsNewsBeanList;

        mNoodleTradeModel.total_price = total;
        mNoodleTradeModel.total_num = sum;
        tvTotal.setText(getString(R.string.payment_total, sum, (float) total));

    }

    public void confirmPay(View view) {
        resetNum();
        if (mNoodleTradeModel.total_num == 0) {
            showDialog("请选择您需要购买的商品");
        } else if (lowStockHint()) {
            Intent intent = new Intent(SelectNoodlesActivity.this, PaymentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("noodle", mNoodleTradeModel);
            bundle.putParcelable("riceOrderND",mRiceOrderND);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void resetNum() {
        riceNum = 0;
        noodleNum = 0;
        freshNum = 0;
        spicyNum = 0;
        eggNum = 0;
        legNum = 0;
        brineNum = 0;
    }

    //数据库操作
    private boolean lowStockHint() {
        for (int i = 0; i < goodsNewsBeanList.size(); i++) {
            Log.d(TAG,"linbin , i = " + goodsNewsBeanList.get(i).goods_name);
            Log.d(TAG,"linbin , i = " + goodsNewsBeanList.get(i).goods_num);
            ListModle listModle = goodsNewsBeanList.get(i);
            Log.d(TAG,"linbin , noodle_type = " + listModle.riceType);
            if (listModle.riceType == DbTypeContants.NOODLE_TYPE_NO) {
                noodleNum += listModle.goods_num;
            } else if (listModle.riceType == DbTypeContants.RICE_TYPE_NO) {
                riceNum += listModle.goods_num;
            }else if (listModle.riceType == DbTypeContants.FRESH_TYPE_NO) {
                freshNum += listModle.goods_num;
            }
            for (int j = 0; j < listModle.goods_num; j++) {
                getDbRecipeNum(listModle.rice_recipe_Type);
            }
        }
        Timber.e("米粉点了%d份", riceNum);
        Timber.e("面条点了%d份", noodleNum);
        Timber.e("新鲜面点了%d份", freshNum);
            if (getDbNum(DbTypeContants.SUANLABAO) < spicyNum) {
            showWarningDialog("确定", getString(R.string.noodle_stock
                    ,"酸辣包",getDbNum(DbTypeContants.SUANLABAO)),true,false);
            return false;
        }
        if (getDbNum(DbTypeContants.LUJIDANG) < eggNum) {
            showWarningDialog("确定", getString(R.string.noodle_stock
                    ,"鸡蛋",getDbNum(DbTypeContants.LUJIDANG)),true,false);
            return false;
        }
        if (getDbNum(DbTypeContants.SUANLAJITUI) < legNum) {
            showWarningDialog("确定", getString(R.string.noodle_stock
                    ,"鸡腿",getDbNum(DbTypeContants.SUANLAJITUI)),true,false);

            return false;
        }
        if (getDbNum(DbTypeContants.MIFEN) < riceNum) {
                showWarningDialog("确定", getString(R.string.noodle_stock
                ,"米粉",getDbNum(DbTypeContants.MIFEN)),true,false);
            return false;
        }
        if (getDbNum(DbTypeContants.MIANTIAO) < noodleNum) {
            showWarningDialog("确定", getString(R.string.noodle_stock
                    ,"面条",getDbNum(DbTypeContants.MIANTIAO)),true,false);
            return false;
        }
        //新鲜面
        if (getDbNum(DbTypeContants.FRESH_NOODLES) < freshNum) {
                showWarningDialog("确定", getString(R.string.noodle_stock
                    ,"新鲜面",getDbNum(DbTypeContants.FRESH_NOODLES)),true,false);
            return false;
        }
        if (getDbNum(DbTypeContants.BRINE_STATUS) < brineNum) {
            showWarningDialog("确定", getString(R.string.noodle_stock
                    ,"卤水",getDbNum(DbTypeContants.BRINE_STATUS)),true,false);
            return false;
        }
        mNoodleTradeModel.rice_No = riceNum;
        mNoodleTradeModel.noodle_No = noodleNum;
        mNoodleTradeModel.fresh_No = freshNum;
        Log.e(TAG ,"linbin , riceNum = " + riceNum);
        Log.e(TAG ,"linbin , noodleNum = "+ noodleNum);
        Log.e(TAG ,"linbin , freshNum = "+ freshNum);
        return true;
    }

    /**
     * 查询数据库各类物品的数量
     * @param num
     * @return
     */
    private int getDbNum(int num) {
        int sum = 0;
        ArrayList<RiceND> list = DBNoodleHelper.querynoodleStatusNoolde(num);
        for (int i = 0; i < list.size(); i++) {
            sum += list.get(i).totalNum;
        }
        Timber.e("库存的量=%d" + sum);
        return sum;
    }

    /**
     * 计算鸡蛋/鸡腿/香辣包的数量
     * @param riceRecipe
     */
    private void getDbRecipeNum(int riceRecipe) {
        brineNum++;
        switch (riceRecipe) {
            case 1:
                break;
            case 2:
                spicyNum++;
                break;
            case 3:
                spicyNum++;
                eggNum++;
                break;
            case 4:
                spicyNum++;
//                eggNum++;
                legNum++;
                break;
                default:
        }
    }

}
