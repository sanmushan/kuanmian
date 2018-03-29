package com.benxiang.noodles.network;


import com.benxiang.noodles.model.CostCardDataModel;
import com.benxiang.noodles.model.Slider.SliderModel;
import com.benxiang.noodles.model.addMeal.AddOrderItemModel;
import com.benxiang.noodles.model.clearStock.ClearStockModel;
import com.benxiang.noodles.model.information.InformationModle;
import com.benxiang.noodles.model.placeorder.PlaceOrderModel;
import com.benxiang.noodles.model.remote.CommonModel;
import com.benxiang.noodles.model.remote.KindModel;
import com.benxiang.noodles.model.remote.MechanicalModel;
import com.benxiang.noodles.model.remote.MerchantModel;
import com.benxiang.noodles.model.remote.OrderNumQueryModel;
import com.benxiang.noodles.model.remote.PayModel;
import com.benxiang.noodles.model.remote.QrModel;
import com.benxiang.noodles.model.remote.RecipeModle;
import com.benxiang.noodles.model.remote.ReliefModle;
import com.benxiang.noodles.model.remote.StrMsgCommonModel;
import com.benxiang.noodles.model.selloff.SellOffListInfoModel;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 刘圣如 on 2017/9/5.
 */

public interface NoodlesApi {

    //注册机器
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<MechanicalModel>> addMacAddress(@FieldMap Map<String, String> map);

    //查询供应商
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<MerchantModel>> QuerySupplier(@FieldMap Map<String, String> map);

    /**
     * post
     */
 /*   @POST("STTXPTAPI/STTXApi.ashx")
    Observable<CommonModel> uploadGuestInfo(@Body MultipartBody guestInfo);*/
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<KindModel>> uploadGuestInfo(@FieldMap Map<String, String> map);

    //获取二维码
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<QrModel>> getQrcodeToNet(@FieldMap Map<String, String> map);

    //支付状态查询
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<PayModel>> getOrderPayStatus(@FieldMap Map<String, String> map);

    //支付退款
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<StrMsgCommonModel>> refundment(@FieldMap Map<String, String> map);


    //取餐号查询
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<OrderNumQueryModel>> getOrderDetial(@FieldMap Map<String, String> map);

    //获取配方
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<RecipeModle>> getRecipe(@FieldMap Map<String, String> map);

    //退菜
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<StrMsgCommonModel>> retreatFood(@FieldMap Map<String, String> map);

    //扣库
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<StrMsgCommonModel>> buckleDB(@FieldMap Map<String, String> map);

    //沽清
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<StrMsgCommonModel>> sellOff(@FieldMap Map<String, String> map);

    //沽清列表
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<SellOffListInfoModel>> getSellOffList(@FieldMap Map<String, String> map);

    //解除异常
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<ReliefModle>> ToexceptionRelief(@FieldMap Map<String, String> map);

  //上传异常
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<StrMsgCommonModel>> uploadException(@FieldMap Map<String, String> map);

    //下单
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<PlaceOrderModel>> placeOrder(@FieldMap Map<String, String> map);

/*    //退菜
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<StrMsgCommonModel>> retreatFood(@FieldMap Map<String, String> map);*/
    //物品信息
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<InformationModle>> getInformation(@FieldMap Map<String, String> map);

    //APP更新
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<StrMsgCommonModel>> udapeAPP(@FieldMap Map<String, String> map);

    //根据取餐号修改订单状态
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<StrMsgCommonModel>> setOrderNoStatus(@FieldMap Map<String, String> map);

    //轮播图
    @FormUrlEncoded  //post请求必须加上
    @POST("AppDataApi.ashx")
    Observable<CommonModel<SliderModel>> flexSliderChange(@FieldMap Map<String, String> map);
    //补货
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<StrMsgCommonModel>> toBackOrder(@FieldMap Map<String, String> map);

    //物品库存清零
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<ClearStockModel>> clearStock(@FieldMap Map<String, String> map);

    //加菜
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<AddOrderItemModel>> addOrderItem(@FieldMap Map<String, String> map);

    //设置订单状态根据订单GUID
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<StrMsgCommonModel>> setOrderNoStatusByGuid(@FieldMap Map<String, String> map);

    //设置订单状态根据订单GUID
    @FormUrlEncoded  //post请求必须加上
    @POST("STTXApi.ashx")
    Observable<CommonModel<CostCardDataModel>> getCostCardData(@FieldMap Map<String, String> map);

}

