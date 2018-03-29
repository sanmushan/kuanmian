package com.benxiang.noodles;

import com.benxiang.noodles.base.BaseActivity;
import com.benxiang.noodles.contants.MethodConstants;
import com.benxiang.noodles.model.Slider.SliderPresenter;
import com.benxiang.noodles.model.Slider.SliderView;
import com.benxiang.noodles.model.backorder.BackOrderParam;
import com.benxiang.noodles.model.backorder.BackOrderPresenter;
import com.benxiang.noodles.model.backorder.BackOrderView;
import com.benxiang.noodles.model.clearStock.ClearStockModel;
import com.benxiang.noodles.model.clearStock.ClearStockParam;
import com.benxiang.noodles.model.clearStock.ClearStockPresenter;
import com.benxiang.noodles.model.clearStock.ClearStockView;
import com.benxiang.noodles.model.information.InformationModle;
import com.benxiang.noodles.model.information.InformationPresenter;
import com.benxiang.noodles.model.information.InformationView;
import com.benxiang.noodles.model.placeorder.PlaceOrderModel;
import com.benxiang.noodles.model.placeorder.PlaceOrderParam;
import com.benxiang.noodles.model.placeorder.PlaceOrderPresenter;
import com.benxiang.noodles.model.placeorder.PlaceOrderView;
import com.benxiang.noodles.model.remote.CommonModel;
import com.benxiang.noodles.model.retreatfood.RetreatFoodParam;
import com.benxiang.noodles.model.selloff.SellOffPresenter;
import com.benxiang.noodles.model.selloff.SellOffView;
import com.benxiang.noodles.moudle.makenoodle.UplaodExView;
import com.benxiang.noodles.moudle.makenoodle.UploadExPresenter;
import com.benxiang.noodles.utils.DataEncrypt;
import com.benxiang.noodles.utils.JsonHelper;
import com.benxiang.noodles.utils.ParamObtainUtil;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by 刘圣如 on 2017/9/30.
 */

public class GuqingTestActivity extends BaseActivity implements SellOffView ,UplaodExView ,InformationView,PlaceOrderView,SliderView,BackOrderView
,ClearStockView{
    private SellOffPresenter mSellOffPresenter;
    private UploadExPresenter mUploadExPresenter;
    private InformationPresenter mInformationPresenter;
    private PlaceOrderPresenter placeOrderPresenter;
    private SliderPresenter mSliderPresenter;
    private BackOrderPresenter backOrderPresenter;
//    private  String orderNo= DeviceUtils.getMacAddress().replace(":", "").toUpperCase() + DataEncrypt.dataFormatString();
    private String orderTimne = DataEncrypt.dataFormat();
    private ClearStockPresenter mClearStockPresenter;
    @Override
    public int getContentViewID() {
        return R.layout.testcolor;
    }

    @Override
    protected void afterContentViewSet() {
        registerMainHandler();
        showError("哈哈哈哈");
    /*    mSellOffPresenter = new SellOffPresenter();
        mSellOffPresenter.attachView(this);
        SellOffParam sellOffParam = new SellOffParam();
        sellOffParam.LID = MethodConstants.SHOPCODE;
        sellOffParam.MenuItemCode = "0102";
        mSellOffPresenter.getSellOffList(MethodConstants.GUQING, JsonHelper.getGson().toJson(sellOffParam));
        mSellOffPresenter.sellOff(MethodConstants.GETGUQING, JsonHelper.getGson().toJson(sellOffParam));

        //上传异常
        mUploadExPresenter = new UploadExPresenter();
        mUploadExPresenter.attachView(this);
        ExceptionParam exceptionParam = new ExceptionParam();
        exceptionParam.mechanical_num = DeviceUtils.getMacAddress().replace(":", "").toUpperCase();
        Timber.e("机器编码："+ exceptionParam.mechanical_num);
        exceptionParam.abnormal_level = "1";
        exceptionParam.abnormal_type = "机器异常";
        exceptionParam.abnormal_detail = "机器不正常，电源没打开";
        exceptionParam.time = DataEncrypt.dataFormatString();
        exceptionParam.merchant_id = "1";
        exceptionParam.remark = "请赶紧解决";
        Timber.e(exceptionParam.toString());
        mUploadExPresenter.uploadException(MethodConstants.UPDATEMCHNO, JsonHelper.getGson().toJson(exceptionParam));

        //获取物品信息
        mInformationPresenter = new InformationPresenter();
        mInformationPresenter.attachView(this);
        InformationParam informationParam = new InformationParam();
        informationParam.shopCode = "000005";
        mInformationPresenter.getInformation(MethodConstants.GETMENU,JsonHelper.getGson().toJson(informationParam));


        //下单
       placeOrderPresenter = new PlaceOrderPresenter();
        placeOrderPresenter.attachView(this);*/


       /* mSliderPresenter = new SliderPresenter();
        mSliderPresenter.attachView(this);
        SliderParam sliderParam = new SliderParam();
        sliderParam.Keys = "kdt123456789";
        mSliderPresenter.slideShow("5",JsonHelper.getGson().toJson(sliderParam));*/

        //补货
//        backOrderPresenter = new BackOrderPresenter();
//        backOrderPresenter.attachView(this);
//        BackOrderParam backOrderParam = ParamObtainUtil.getBackOrderParam(10,11);
//        backOrderPresenter.getBackOrderInfo(MethodConstants.BACK_ORDER,JsonHelper.getGson().toJson(backOrderParam));

        //物品库存清零
        mClearStockPresenter = new ClearStockPresenter();
        mClearStockPresenter.attachView(this);
        ClearStockParam clearStockParam = ParamObtainUtil.getClearStockParam();
        Timber.e("物品清零参数:"+JsonHelper.getGson().toJson(clearStockParam));
        mClearStockPresenter.clearStock(MethodConstants.CLEARSTOCK,JsonHelper.getGson().toJson(clearStockParam));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mInformationPresenter != null) {
            mInformationPresenter.detavh();
            mInformationPresenter = null;
        }
        if (mUploadExPresenter != null) {
            mUploadExPresenter.detavh();
            mUploadExPresenter = null;
        }
        if (mSellOffPresenter != null) {
            mSellOffPresenter.detavh();
            mSellOffPresenter = null;
        }
        if (placeOrderPresenter != null) {
            placeOrderPresenter.detavh();
            placeOrderPresenter = null;
        }
        if (mSliderPresenter != null) {
            mSliderPresenter.detavh();
            mSliderPresenter = null;
        }
        if (backOrderPresenter != null) {
            backOrderPresenter.detavh();
            backOrderPresenter = null;
        }
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showNetError(String error) {

    }

    @Override
    public void sellOffSuccess() {

    }

    @Override
    public void showSellOffInfo() {

    }

    @Override
    public void UplaodExSuccess() {

    }

    @Override
    public void refundmentExSuccess() {

    }

    @Override
    public void refundmentFaile() {

    }

    @Override
    public void retreaFoodSuccess() {

    }

    @Override
    public void retreaFoodFaile() {

    }

    @Override
    public void getInformationSuccess(CommonModel<InformationModle> commonModel) {
    //下单
        PlaceOrderParam placeOrderParam = new PlaceOrderParam();
        placeOrderParam.shopCode = "test";

        PlaceOrderParam.OrderData orderData = new PlaceOrderParam.OrderData();
//        orderData.BillNo=DeviceUtils.getMacAddress().replace(":", "").toUpperCase() + DataEncrypt.dataFormatString();
        orderData.BillNo="";
//        orderData.GUID = orderNo;
        orderData.GuestQty = "1";
        orderData.OrderType=1;
        orderData.TableNo = "A001";
        orderData.SumPrice = 15;
        orderData.IsPay = 0;
        orderData.PayType = 2;
        orderData.PayTime = DataEncrypt.dataFormat();
       /* orderData.PayCardCode = "";
        orderData.LID = "test";
        orderData.Waiter = "sa";
        orderData.WaiterName = "超级用户";
        orderData.Cashier = "sa";
        orderData.CashierName = "超级用户";
        orderData.Checker = "";
        orderData.CheckerName = "";
        orderData.Operator = "sa";
        orderData.OperatorName = "超级用户";*/
        orderData.OrderDate= DataEncrypt.dataFormat();
//        orderData.Remark = "多加点汤";
        List<PlaceOrderParam.OrderDetailData> orderDetailDataArr = new ArrayList<>();
        PlaceOrderParam.OrderDetailData orderDetailData = new PlaceOrderParam.OrderDetailData();
        orderDetailData.productId = "0101";
        orderDetailData.productName = "叉烧面";
        orderDetailData.price = 15;
        orderDetailData.CostPrice = 15;
        orderDetailData.SourcePrice = 15;
        orderDetailData.DishSumReal = 15;
        orderDetailData.isSuite = 0;
        orderDetailData.count = 1;
//        orderDetailDatas.Remark = "多加点料";
        orderDetailDataArr.add(orderDetailData);
        orderData.orderDetails=orderDetailDataArr;
        placeOrderParam.Order = orderData;
//        placeOrderParam.Order
        placeOrderPresenter.placeOrder(MethodConstants.GREATEORDER,JsonHelper.getGson().toJson(placeOrderParam));
    }

    @Override
    public void placeOrderSuccess(PlaceOrderModel strMsg) {
        Timber.e("退菜");
        RetreatFoodParam retreatFoodParam = new RetreatFoodParam();

        RetreatFoodParam.OrderData orderData = new RetreatFoodParam.OrderData();
        orderData.BillNo = strMsg.BillNo;
//        orderData.GUID = orderNo;
        orderData.GUID = "";
        orderData.GuestQty = "1";
        orderData.OrderType=1;
        orderData.SumPrice = 15;
        orderData.IsPay = 0;
        orderData.PayType = 2;
        orderData.PayTime =orderTimne;
        orderData.OrderDate= orderTimne;
        List<RetreatFoodParam.OrderDetailDatas> orderDetailDatases = new ArrayList<>();
        RetreatFoodParam.OrderDetailDatas orderDetailDatas = new RetreatFoodParam.OrderDetailDatas();
        orderDetailDatas.productId = "0101";
        orderDetailDatas.productName = "叉烧面";
        orderDetailDatas.price = 15;
        orderDetailDatas.count = 1;
        orderDetailDatas.isSuite = 0;
        orderDetailDatas.BackReason = "测试";
        orderDetailDatas.BackTime = orderTimne;
        orderDetailDatas.Backer = "sa";
        orderDetailDatas.BackerName = "张三";

        orderDetailDatas.CostPrice = 15;
        orderDetailDatas.SourcePrice = 15;
        orderDetailDatas.DishSumReal = 15;
        orderDetailDatas.OrderDate = orderTimne;
        orderDetailDatas.CheckTime = orderTimne;
        orderDetailDatas.Remark = "多加点料";
        orderDetailDatases.add(orderDetailDatas);
        orderData.orderDetail=orderDetailDatases;
        retreatFoodParam.Order = orderData;
        placeOrderPresenter.retreatFood(MethodConstants.GREATEORDER,JsonHelper.getGson().toJson(retreatFoodParam));
    }

    @Override
    public void SliderSuccess() {}

    @Override
    public void BackOrderSuccess() {}

    @Override
    public void clearStockSuccess(ClearStockModel strMsg) {}

    @Override
    public void setOrderStatusByIDSuccess() {}

    @Override
    public void setOrderStatusByIDFail() {}
}
