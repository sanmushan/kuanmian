package com.benxiang.noodles.base;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.benxiang.noodles.R;
import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.contants.DbTypeContants;
import com.benxiang.noodles.contants.MethodConstants;
import com.benxiang.noodles.data.DBNoodleHelper;
import com.benxiang.noodles.data.NoodleDB;
import com.benxiang.noodles.data.table.DropPackageDB;
import com.benxiang.noodles.model.CardDataReParameter;
import com.benxiang.noodles.model.CostCardDataModel;
import com.benxiang.noodles.model.backorder.BackOrderParam;
import com.benxiang.noodles.model.backorder.BackOrderPresenter;
import com.benxiang.noodles.model.backorder.BackOrderView;
import com.benxiang.noodles.model.clearStock.ClearStockModel;
import com.benxiang.noodles.model.clearStock.ClearStockParam;
import com.benxiang.noodles.model.clearStock.ClearStockPresenter;
import com.benxiang.noodles.model.clearStock.ClearStockView;
import com.benxiang.noodles.moudle.banner.BannerActivity;
import com.benxiang.noodles.moudle.costCard.CostCardPresenter;
import com.benxiang.noodles.moudle.costCard.CostCardView;
import com.benxiang.noodles.serialport.data.sp.FormulaPreferenceConfig;
import com.benxiang.noodles.serialport.data.sp.PreferenceConfig;
import com.benxiang.noodles.utils.JsonHelper;
import com.benxiang.noodles.utils.NoodleDataUtil;
import com.benxiang.noodles.utils.ParamObtainUtil;
import com.benxiang.noodles.utils.PreferenceUtil;
import com.benxiang.noodles.utils.SpUtils;

import java.util.ArrayList;

import butterknife.BindView;
import timber.log.Timber;

/**
 * Created by admin on 2018/1/5.
 */

public abstract class BaseMenageActivity extends BaseActivity implements CostCardView, ClearStockView, BackOrderView {

    @BindView(R.id.ed_setting_noodles)
    protected EditText noodelsNum;
    @BindView(R.id.ed_setting_noodles_plies)
    protected EditText noodelsNumPlies;
    @BindView(R.id.ed_setting_noodles_price)
    protected EditText noodelsPrice;
    @BindView(R.id.ed_setting_rice)
    protected EditText riceNum;
    @BindView(R.id.ed_setting_rice_plies)
    protected EditText riceNumPlies;
    @BindView(R.id.ed_setting_rice_price)
    protected EditText ricePrice;
    //todo-----------------------------------------------------------------------------------------------
    @BindView(R.id.edSetFreshNoodles)
    protected EditText freshNoodles;
    @BindView(R.id.edSetFreshNoodlesPlies)
    protected EditText freshNoodlesPlies;

    @BindView(R.id.ed_setting_chicken_leg)
    protected EditText ed_setting_chicken_leg;
    @BindView(R.id.ed_setting_leg_plies)
    protected EditText ed_setting_leg_plies;
    @BindView(R.id.ed_setting_halogen_eggs)
    protected EditText ed_setting_halogen_eggs;
    @BindView(R.id.ed_setting_eggs_plies)
    protected EditText ed_setting_eggs_plies;
    @BindView(R.id.ed_setting_spicy)
    protected EditText ed_setting_spicy;
    @BindView(R.id.ed_setting_spicy_plies)
    protected EditText ed_setting_spicy_plies;
    //新增品类信息
    @BindView(R.id.tv_one_category)
    protected TextView tvOneCategory;
    @BindView(R.id.ed_one_category_max)
    protected EditText edOneCategoryMax;

    @BindView(R.id.tv_two_category)
    protected TextView tvTwoCategory;
    @BindView(R.id.ed_two_category_max)
    protected EditText edTwoCategoryMax;

    @BindView(R.id.tv_three_category)
    protected TextView tvThreeCategory;
    @BindView(R.id.ed_three_category_max)
    protected EditText edThreeCategoryMax;

    @BindView(R.id.tv_four_category)
    protected TextView tvFourCategory;
    @BindView(R.id.ed_four_category_max)
    protected EditText edFourCategoryMax;

    @BindView(R.id.ed_four_category_sum)
    protected EditText edFourCategorySum;
    @BindView(R.id.ed_four_category_plies)
    protected EditText edFourCategoryPlies;

    @BindView(R.id.ed_brine)
    EditText edBrine;

    @BindView(R.id.sw)
    protected Switch sw;


    private CostCardPresenter mCostCardPresenter;
    private ClearStockPresenter mClearStockPresenter;
    protected BackOrderParam backOrderParam;
    protected BackOrderPresenter mBackOrderPresenter;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();
        getCostCard();
        //显示当前弹簧最大数
        edOneCategoryMax.setText(FormulaPreferenceConfig.getOnePliesMax() + "");
        edTwoCategoryMax.setText(FormulaPreferenceConfig.getTwoPliesMax() + "");
        edThreeCategoryMax.setText(FormulaPreferenceConfig.getThreePliesMax() + "");
        edFourCategoryMax.setText(FormulaPreferenceConfig.getFourPliesMax() + "");

        //LIN 设置是否可以抽奖  1：可以，0：不可以
        if (SpUtils.loadValue("lucky").equals("1")) {
            sw.setChecked(true);
        } else {
            sw.setChecked(false);
        }

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SpUtils.saveValue("lucky", "1");
                } else {
                    SpUtils.saveValue("lucky", "0");
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCostCardPresenter != null) {
            mCostCardPresenter.detavh();
            mCostCardPresenter = null;
        }
        if (mClearStockPresenter != null) {
            mClearStockPresenter.detavh();
            mClearStockPresenter = null;
        }
        if (mBackOrderPresenter != null) {
            mBackOrderPresenter.detavh();
            mBackOrderPresenter = null;
        }
    }

    protected void getCostCard() {
        //TODO 请求获取成本卡信息  LINBIN
        //获取成本卡
        mCostCardPresenter = new CostCardPresenter();
        mCostCardPresenter.attachView(this);
        CardDataReParameter cardDataReParameter = new CardDataReParameter();
        cardDataReParameter.LID = MethodConstants.SHOPCODE;
        mCostCardPresenter.getCostCard(MethodConstants.COST_CARD_DATA, JsonHelper.getGson().toJson(cardDataReParameter));
    }

    //物品库存清零
    @SuppressLint("BinaryOperationInTimber")
    protected void clearStock() {
        mClearStockPresenter = new ClearStockPresenter();
        mClearStockPresenter.attachView(this);
        ClearStockParam clearStockParam = ParamObtainUtil.getClearStockParam();
        Timber.e("物品清零参数:" + JsonHelper.getGson().toJson(clearStockParam));
        mClearStockPresenter.clearStock(MethodConstants.CLEARSTOCK, JsonHelper.getGson().toJson(clearStockParam));
    }

    //补货
    protected void backOrder() {
        Timber.e("开始补货");
        mBackOrderPresenter = new BackOrderPresenter();
        mBackOrderPresenter.attachView(this);
//        if (backOrderParam!=null){
        mBackOrderPresenter.getBackOrderInfo(MethodConstants.BACK_ORDER, JsonHelper.getGson().toJson(backOrderParam));
//        }
    }

    //TODO LIN 补库存
    @SuppressLint("BinaryOperationInTimber")
    protected void dataToDB() {
        String noodleNo = "0";//面的数量
        String riceNo = "0";//粉的数量

        String freshNo = "0";//新鲜面的数量

        String noodlePlies = "0";//面的层数
        String ricePlies = "0";//粉的层数

        String freshPlies = "0";//新鲜面的层数

        String spicyNo = "0";//酸辣包的数量
        String legNo = "0";//鸡腿的数量
        String eggNo = "0";//卤蛋的数量
        String fourCategoryNo = "0";//=========>第四品类的数量
        String spicyPiles = "0";//酸辣包的层数
        String legPlies = "0";//鸡腿的层数
        String eggPlies = "0";//鸡蛋的层数
        String fourCategoryPlies = "0";//=========>第四品类的层数
        String brineCapacity = "0";//卤水的容量
        int brineNo = 0;
        if (edNullHint()) {
            noodleNo = noodelsNum.getText().toString().trim();
            riceNo = riceNum.getText().toString().trim();
            freshNo = freshNoodles.getText().toString().trim();
            if (!noodelsNumPlies.getText().toString().trim().equals("")) {
                noodlePlies = noodelsNumPlies.getText().toString().trim();
            }
            if (!riceNumPlies.getText().toString().trim().equals("")) {
                ricePlies = riceNumPlies.getText().toString().trim();
            }
            if (!freshNoodlesPlies.getText().toString().trim().equals("")) {
                freshPlies = freshNoodlesPlies.getText().toString().trim();
            }
            if (!ed_setting_spicy.getText().toString().trim().equals("")) {
                spicyNo = ed_setting_spicy.getText().toString().trim();
            }
            if (!ed_setting_chicken_leg.getText().toString().trim().equals("")) {
                eggNo = ed_setting_chicken_leg.getText().toString().trim();
            }
            if (!ed_setting_halogen_eggs.getText().toString().trim().equals("")) {
                legNo = ed_setting_halogen_eggs.getText().toString().trim();
            }
            if (!edFourCategorySum.getText().toString().trim().equals("")) {
                fourCategoryNo = edFourCategorySum.getText().toString().trim();
            }
            if (!ed_setting_spicy_plies.getText().toString().trim().equals("")) {
                spicyPiles = ed_setting_spicy_plies.getText().toString().trim();
            }
            if (!ed_setting_leg_plies.getText().toString().trim().equals("")) {
                eggPlies = ed_setting_leg_plies.getText().toString().trim();
            }
            if (!ed_setting_eggs_plies.getText().toString().trim().equals("")) {
                legPlies = ed_setting_eggs_plies.getText().toString().trim();
            }
            if (!edFourCategoryPlies.getText().toString().trim().equals("")) {
                fourCategoryPlies = edFourCategoryPlies.getText().toString().trim();
            }
            if (!edBrine.getText().toString().trim().equals("")) {
                brineCapacity = edBrine.getText().toString().trim();
            }

            //酸辣包  鸡腿  卤蛋  榨菜
            //面粉的数量
            if (Integer.parseInt(noodleNo) > DbTypeContants.RICE_Piles_MAX * Integer.parseInt(noodlePlies) ||
                    Integer.parseInt(riceNo) > DbTypeContants.RICE_Piles_MAX * Integer.parseInt(ricePlies) ||

                    //添加宽面判断
                    Integer.parseInt(freshNo) > DbTypeContants.RICE_Piles_MAX * Integer.parseInt(freshPlies) ||
                    //掉料包的数量
                    Integer.parseInt(spicyNo) > FormulaPreferenceConfig.getOnePliesMax() * Integer.parseInt(spicyPiles) ||
                    Integer.parseInt(eggNo) > FormulaPreferenceConfig.getTwoPliesMax() * Integer.parseInt(eggPlies) ||
                    Integer.parseInt(legNo) > FormulaPreferenceConfig.getThreePliesMax() * Integer.parseInt(legPlies) ||
                    Integer.parseInt(fourCategoryNo) > FormulaPreferenceConfig.getFourPliesMax() * Integer.parseInt(fourCategoryPlies) ||
                    //层数的数量
                    Integer.parseInt(noodlePlies) + Integer.parseInt(ricePlies) + Integer.parseInt(freshPlies) > DbTypeContants.RICE_PLIES_MAX ||
                    Integer.parseInt(spicyPiles) + Integer.parseInt(legPlies) + Integer.parseInt(eggPlies) + Integer.parseInt(fourCategoryPlies) > DbTypeContants.CHARGE_PLIES_MAX
                    ) {
                showError("请正确填写物料层数");
            }else if (Integer.parseInt(brineCapacity) > 2500){
                showError("卤水最大值为2500ML");
            }else {
                Timber.e("面条层数" + noodlePlies);
                Timber.e("米粉层数" + ricePlies);
                Timber.e("新鲜面层数" + freshPlies);
                showLoadingDialog();
                int riceStartNo = (Integer.parseInt(noodlePlies) + DbTypeContants.LACK_PILES) * 3 + 1;

                int freshStartNo = (Integer.parseInt(ricePlies)) * 3 + 1;
                //TODO LIN 设置掉料通道
                //一品类（酸辣包）
                int spicyStartNo = 51;
                //二品类(卤蛋)
                int eggStartNo = 51 + Integer.parseInt(spicyPiles);
                //三品类(鸡腿)
                int legStartNo = 51 + Integer.parseInt(spicyPiles) + Integer.parseInt(eggPlies);
                //四品类(抽奖)
                int fourCategorytStartNo = 51 + Integer.parseInt(spicyPiles) + Integer.parseInt(eggPlies) + Integer.parseInt(legPlies);

                brineNo = Integer.parseInt(brineCapacity) / NoodleDataUtil.getMaxCapacityBrine();

                Timber.e("鸡蛋层数" + eggPlies);
                Timber.e("鸡蛋开始" + eggStartNo);

                //TODO LIN 设置面食类的最大数量
                PreferenceConfig.setTypeAMax((Integer.parseInt(noodlePlies) + DbTypeContants.LACK_PILES) * 3);
//                PreferenceConfig.setTypeAMax((Integer.parseInt(noodlePlies) + 1) * 3);

                PreferenceConfig.setTypeBMin(riceStartNo);
                PreferenceConfig.setTypeBMax((riceStartNo + Integer.parseInt(ricePlies)) * 3);

                PreferenceConfig.setTypeCMin(freshStartNo);
                PreferenceConfig.setTypeCMax((freshStartNo + Integer.parseInt(freshPlies)) * 3);


                //面
                NoodleDB.initNoodle(Integer.parseInt(noodleNo), DbTypeContants.NOODLE_TYPE, true, DbTypeContants.NOODLE_START_NO, Integer.parseInt(noodlePlies));
                //粉
                NoodleDB.initNoodle(Integer.parseInt(riceNo), DbTypeContants.RICE_TYPE, false, riceStartNo, Integer.parseInt(ricePlies));
                //新鲜面
                NoodleDB.initNoodle(Integer.parseInt(freshNo), DbTypeContants.FRESH_TYPE, false, freshStartNo, Integer.parseInt(freshPlies));

                //设置掉料包
                NoodleDB.initNoodle(Integer.parseInt(spicyNo), FormulaPreferenceConfig.getOnePliesMax(), DbTypeContants.SUANLABAO_TYPE, false,
                        DbTypeContants.SUANLABAO_TYPE_NO, Integer.parseInt(spicyPiles), spicyStartNo);

                //todo 修改后 3.22
                NoodleDB.initNoodle(Integer.parseInt(eggNo), FormulaPreferenceConfig.getTwoPliesMax(), DbTypeContants.LUJIDANG_TYPE, false,
                        DbTypeContants.LUJIDANG_TYPE_NO, Integer.parseInt(eggPlies), eggStartNo);

                NoodleDB.initNoodle(Integer.parseInt(legNo), FormulaPreferenceConfig.getThreePliesMax(), DbTypeContants.SUANLAJITUI_TYPE, false,
                        DbTypeContants.SUANLAJITUI_TYPE_NO, Integer.parseInt(legPlies), legStartNo);

                NoodleDB.initNoodle(Integer.parseInt(fourCategoryNo), FormulaPreferenceConfig.getFourPliesMax(), DbTypeContants.FOUR_CATEGORY_TYPE, false,
                        DbTypeContants.FOUR_CATEGORY_TYPE_NO,
                        Integer.parseInt(fourCategoryPlies), fourCategorytStartNo);

                NoodleDB.initNoodle(brineNo, false, fourCategorytStartNo + Integer.parseInt(fourCategoryPlies));
                runToNextPager();
                //TODO LIN 设置后台库存上传参数
                backOrderParam = ParamObtainUtil.getBackOrderParam(Integer.parseInt(noodleNo), Integer.parseInt(riceNo), Integer.parseInt(spicyNo), Integer.parseInt(legNo), Integer.parseInt(eggNo), Integer.parseInt(fourCategoryNo));
//                backOrderParam = ParamObtainUtil.getBackOrderParam(Integer.parseInt(noodleNo), Integer.parseInt(riceNo), Integer.parseInt(freshNo), Integer.parseInt(spicyNo), Integer.parseInt(legNo), Integer.parseInt(eggNo), Integer.parseInt(fourCategoryNo));
                //清库存后补货
                clearStock();
            }
        }
    }

    protected void runToNextPager() {
        getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLoadingDialog();
                startNext(BaseMenageActivity.this, BannerActivity.class);
            }
        }, 3000);
    }

    //面或米粉的数量不为空就返回True
    private boolean edNullHint() {
        String noodleNo = noodelsNum.getText().toString().trim();
        String riceNo = riceNum.getText().toString().trim();
        //新鲜面
        String freshNo = freshNoodles.getText().toString().trim();

        String noodleprice = noodelsPrice.getText().toString().trim();
        String riceprice = ricePrice.getText().toString().trim();
        //新鲜面
        String freshPrice = freshNoodlesPlies.getText().toString().trim();

        String oneCategoryMax = edOneCategoryMax.getText().toString().trim();
        String twoCategoryMax = edTwoCategoryMax.getText().toString().trim();
        String threeCategoryMax = edThreeCategoryMax.getText().toString().trim();
        String fourCategoryMax = edFourCategoryMax.getText().toString().trim();
//        if (TextUtils.isEmpty(noodleNo) || TextUtils.isEmpty(riceNo) || TextUtils.isEmpty(freshNo)) {
//            showError("不能为空");
//            return false;
//        }
        if (TextUtils.isEmpty(noodleNo)) {
            showError("请添加面条的数量");
            return false;
        }
        if (TextUtils.isEmpty(riceNo)) {
            showError("请添加米粉的数量");
            return false;
        }
        if (TextUtils.isEmpty(freshNo)) {
            showError("请添加新鲜面的数量");
            return false;
        }
        if (!TextUtils.isEmpty(noodleprice)) {
            PreferenceUtil.config().setStringValue(Constants.NOODLE_PRICE, noodleprice);
        }
        if (!TextUtils.isEmpty(riceprice)) {
            PreferenceUtil.config().setStringValue(Constants.RICE_PRICE, riceprice);
        }
        //新鲜面
        if (!TextUtils.isEmpty(freshPrice)) {
            PreferenceUtil.config().setStringValue(Constants.FRESH_PRICE, freshPrice);
        }
        PreferenceUtil.config().setStringValue(Constants.NOODLE_NO, noodleNo);
        PreferenceUtil.config().setStringValue(Constants.RICE_NO, riceNo);
        PreferenceUtil.config().setStringValue(Constants.FRESH_NO, freshNo);

        if (!TextUtils.isEmpty(oneCategoryMax)) {
            FormulaPreferenceConfig.setOnePliesMax(Integer.parseInt(oneCategoryMax));
        }
        if (!TextUtils.isEmpty(twoCategoryMax)) {
            FormulaPreferenceConfig.setTwoPliesMax(Integer.parseInt(twoCategoryMax));
        }
        if (!TextUtils.isEmpty(threeCategoryMax)) {
            FormulaPreferenceConfig.setThreePliesMax(Integer.parseInt(threeCategoryMax));
        }
        if (!TextUtils.isEmpty(fourCategoryMax)) {
            FormulaPreferenceConfig.setFourPliesMax(Integer.parseInt(fourCategoryMax));
        }
        return true;
    }

    @Override
    public void getCostCardSuccess(CostCardDataModel cardDataModel) {
        NoodleDataUtil.setCategory(cardDataModel);

        ArrayList<DropPackageDB> dropPackageDBs = DBNoodleHelper.queryByProductStatus("");
        for (int i = 0; i < dropPackageDBs.size(); i++) {
            switch (i) {
                //TODO 3.8 LIN ----------------------------------------------------------------------------------------
                //卤蛋
                case 0:
                    tvTwoCategory.setText(dropPackageDBs.get(0).menuItemCName);
                    break;
                //鸡腿
                case 1:
                    tvThreeCategory.setText(dropPackageDBs.get(2).menuItemCName);
                    break;
                //抽奖
                case 2:
                    tvFourCategory.setText("抽奖：" + dropPackageDBs.get(1).menuItemCName);
                    break;
                default:
                    Timber.e("数据库的数量超过3种");
                    break;
            }
        }
    }

    @Override
    public void clearStockSuccess(ClearStockModel strMsg) {
        backOrder();

    }

    @Override
    public void BackOrderSuccess() {
    }

    @Override
    public void showNetError(String error) {
//        showError(error);
    }
}
