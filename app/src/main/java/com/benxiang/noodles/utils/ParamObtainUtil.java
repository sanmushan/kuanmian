package com.benxiang.noodles.utils;

import android.os.Handler;

import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.contants.MethodConstants;
import com.benxiang.noodles.contants.NoodleNameConstants;
import com.benxiang.noodles.data.DBNoodleHelper;
import com.benxiang.noodles.data.table.DropPackageDB;
import com.benxiang.noodles.data.table.RiceND;
import com.benxiang.noodles.data.table.RiceOrderND;
import com.benxiang.noodles.model.ListModle;
import com.benxiang.noodles.model.NoodleTradeModel;
import com.benxiang.noodles.model.addMeal.AddOrderItemParam;
import com.benxiang.noodles.model.backorder.BackOrderParam;
import com.benxiang.noodles.model.clearStock.ClearStockParam;
import com.benxiang.noodles.model.lottery.BeginLotteryModel;
import com.benxiang.noodles.model.lottery.CloseLotteryModel;
import com.benxiang.noodles.model.placeorder.PlaceOrderParam;
import com.benxiang.noodles.model.retreatfood.RetreatFoodParam;
import com.benxiang.noodles.moudle.makemeal.OrderNoStatusIDParam;
import com.benxiang.noodles.moudle.makemeal.OrderNoStatusParam;
import com.benxiang.noodles.moudle.pay.PayParam;
import com.benxiang.noodles.serialport.data.sp.FormulaPreferenceConfig;
import com.blankj.utilcode.util.DeviceUtils;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by 刘圣如 on 2017/11/14.
 */

public class ParamObtainUtil {

    //退菜参数
    public static RetreatFoodParam getRetreadFood(NoodleTradeModel noodleTradeModel, List<ListModle> listModles) {
        RetreatFoodParam retreatFoodParam = new RetreatFoodParam();
        String orderTimne = DataEncrypt.dataFormat();
        RetreatFoodParam.OrderData orderData = new RetreatFoodParam.OrderData();
        orderData.BillNo = noodleTradeModel.order_No;
        orderData.GUID = noodleTradeModel.ggid;
        Timber.e("ggid" + noodleTradeModel.ggid);
        orderData.GuestQty = "1";
        orderData.OrderType = 1;
        orderData.SumPrice = FormatUtil.flaotToDouble(noodleTradeModel.total_price);
        orderData.IsPay = 0;
        orderData.PayType = noodleTradeModel.pay_type;
        orderData.PayTime = noodleTradeModel.pay_time;
        orderData.OrderDate = noodleTradeModel.pay_time;
        List<RetreatFoodParam.OrderDetailDatas> orderDetailDatases = new ArrayList<>();
        RetreatFoodParam.OrderDetailDatas orderDetailDatas = new RetreatFoodParam.OrderDetailDatas();
        for (int i = 0; i < listModles.size(); i++) {
            ListModle listModle = listModles.get(i);
            double Price = FormatUtil.flaotToDouble(listModle.goods_prive);
            orderDetailDatas.productId = listModle.productId;
            orderDetailDatas.productName = listModle.goods_name;
            orderDetailDatas.price = Price;
            orderDetailDatas.count = 1;
            orderDetailDatas.isSuite = 0;
            orderDetailDatas.BackReason = "退菜";
            orderDetailDatas.BackTime = orderTimne;
            orderDetailDatas.Backer = "sa";
            orderDetailDatas.BackerName = "张三";

            orderDetailDatas.CostPrice = Price;
            orderDetailDatas.SourcePrice = Price;
            orderDetailDatas.DishSumReal = Price;
            orderDetailDatas.OrderDate = listModle.itemTime;
            orderDetailDatas.CheckTime = noodleTradeModel.pay_time;
            orderDetailDatas.Remark = "多加点料";
            orderDetailDatases.add(orderDetailDatas);
            Timber.e("时间" + listModle.itemTime);
            Timber.e("价格" + orderDetailDatas.CostPrice + "时间" + noodleTradeModel.pay_time);
        }
        orderData.orderDetail = orderDetailDatases;
        retreatFoodParam.Order = orderData;
        return retreatFoodParam;
    }

    //下单参数
    public static PlaceOrderParam getPlaceOrderParam(NoodleTradeModel noodleTradeModel, List<ListModle> listModles) {
        PlaceOrderParam placeOrderParam = new PlaceOrderParam();
        placeOrderParam.shopCode = MethodConstants.SHOPCODE;

        PlaceOrderParam.OrderData orderData = new PlaceOrderParam.OrderData();
        if (DeviceUtils.getMacAddress() != null) {
            orderData.BillNo = DeviceUtils.getMacAddress().replace(":", "").toUpperCase() + DataEncrypt.dataFormatString();
        }else {
            orderData.BillNo = DataEncrypt.dataFormatString();
        }
        orderData.GUID = noodleTradeModel.order_No;
        orderData.GuestQty = "1";
        orderData.OrderType = 1;
        orderData.TableNo = "01";
        orderData.SumPrice = FormatUtil.flaotToDouble(noodleTradeModel.total_price);
        orderData.IsPay = 1;
        orderData.PayType = noodleTradeModel.pay_type;
        orderData.PayTime = noodleTradeModel.pay_time;
        //店标,和门店编号shopCode的值一样
        orderData.LID = MethodConstants.SHOPCODE;
//        orderData.LID = "test";
        Timber.e("LID的值:"+orderData.LID);
        orderData.Waiter = "01";
        orderData.WaiterName = "E面机器";
        orderData.Cashier = "01";
        orderData.CashierName = "E面机器";
        orderData.Checker = "01";
        orderData.CheckerName = "E面机器";
        orderData.Operator = "01";
        orderData.OperatorName = "E面机器";
        orderData.OrderDate = noodleTradeModel.pay_time;
//        orderData.Remark = "多加点汤";
        List<PlaceOrderParam.OrderDetailData> orderDetailDataArr = new ArrayList<>();
        for (int i = 0; i < listModles.size(); i++) {
            ListModle listModle = listModles.get(i);
            PlaceOrderParam.OrderDetailData orderDetailData = new PlaceOrderParam.OrderDetailData();
            double Price = FormatUtil.flaotToDouble(listModle.goods_prive);
            orderDetailData.productId = listModle.productId;
            orderDetailData.productName = listModle.goods_name;
            orderDetailData.price = (float) Price;
            orderDetailData.CostPrice = (float) Price;
            orderDetailData.SourcePrice = (float) Price;
            orderDetailData.DishSumReal = (float) Price;
            orderDetailData.isSuite = 0;
            orderDetailData.count = listModle.goods_num;
            orderDetailDataArr.add(orderDetailData);
        }
        orderData.orderDetails = orderDetailDataArr;
        placeOrderParam.Order = orderData;
        return placeOrderParam;
    }

    //订单状态(1)
    public static OrderNoStatusParam getOrderNoStatusParam(int orderStatus, String take_meal_no) {
        OrderNoStatusParam statusParam = new OrderNoStatusParam();
        statusParam.shopCode = MethodConstants.SHOPCODE;
        statusParam.BillStatus = String.valueOf(orderStatus);
        statusParam.TakeNumber = take_meal_no;
        Timber.e("TakeNumber设置订单状态的参数:"+JsonHelper.getGson().toJson(statusParam));
        return statusParam;
    }

    //订单状态(2)
    public static OrderNoStatusIDParam getOrderNoStatusIDParam(int orderStatus, String id) {
        OrderNoStatusIDParam statusParam = new OrderNoStatusIDParam();
        statusParam.shopCode = MethodConstants.SHOPCODE;
        statusParam.BillStatus = String.valueOf(orderStatus);
        statusParam.ID = id;
        Timber.e("GUID设置订单状态的参数:"+JsonHelper.getGson().toJson(statusParam));
        return statusParam;
    }

    //退款信息
    public static PayParam getPayParam(String money, String orderPrice, String orderNo, int payType) {
        PayParam payParam = new PayParam();
        payParam.shopCode = MethodConstants.SHOPCODE;
        payParam.money = money;
        if (payType == Constants.WEIXIN) {
            payParam.orderPrice = orderPrice;
        }
        payParam.orderNo = orderNo;
        return payParam;
    }

    public static PayParam getReturnPayParam(NoodleTradeModel noodleTradeModel, List<ListModle> listModles, int payWay) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        PayParam payParam = new PayParam();
        float money = 0;
        Timber.e("退款订单数量:"+listModles.size());
        for (int i = 0; i < listModles.size(); i++) {
            money += listModles.get(i).goods_prive;
            Timber.e("money的值:"+money);
        }
        String method = "";
        if (payWay == Constants.ALIPAY) {
            method = MethodConstants.RUNalipayREFUND;
        } else if (payWay == Constants.WEIXIN) {
            method = MethodConstants.RUNWXREFUND;
            payParam.orderPrice = decimalFormat.format(noodleTradeModel.total_price);
        }

        payParam.shopCode = MethodConstants.SHOPCODE;
        payParam.money = decimalFormat.format(money);

        payParam.orderNo = noodleTradeModel.order_No;
        return payParam;
    }

    public static PayParam getReturnPayParamMany(float total_price, List<RiceOrderND> riceOrderNDs, int payWay) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        PayParam payParam = new PayParam();
        float money = 0;
        Timber.e("退款订单数量:"+riceOrderNDs.size());
        for (int i = 0; i < riceOrderNDs.size(); i++) {
            money += riceOrderNDs.get(i).goodsPrice;
            Timber.e("money的值:"+money);
        }
        String method = "";
        if (payWay == Constants.ALIPAY) {
            method = MethodConstants.RUNalipayREFUND;
        } else if (payWay == Constants.WEIXIN) {
            method = MethodConstants.RUNWXREFUND;
            payParam.orderPrice = decimalFormat.format(total_price);
        }

        payParam.shopCode = MethodConstants.SHOPCODE;
        payParam.money = decimalFormat.format(money);

        payParam.orderNo = riceOrderNDs.get(0).order_No;
        return payParam;
    }

    //获取补货参数
    public static BackOrderParam getBackOrderParam(int noodleCount,int riceCount,int spicyCount,int legCount,int eggCount,int fourCatogoryCount) {
//        String noodle_price = PreferenceUtil.config().getStringValue(Constants.NOODLE_PRICE);
//        String rice_price = PreferenceUtil.config().getStringValue(Constants.RICE_PRICE);
        BackOrderParam backOderParam = new BackOrderParam();
        backOderParam.LIDCode = MethodConstants.SHOPCODE;
        List<BackOrderParam.Detail> details = new ArrayList<>();
        //面补货
        BackOrderParam.Detail noodleDetail = new BackOrderParam.Detail();
        noodleDetail.ProductCode = "0101";
        noodleDetail.ProductName = "面饼(小面)";
        noodleDetail.ProductUnit="个";
        noodleDetail.ProductStandard = "个";
        noodleDetail.ProductPrice = Double.parseDouble("0");
        noodleDetail.ProductQuantity = noodleCount;
        noodleDetail.ProductTotalMoney = noodleDetail.ProductPrice * noodleDetail.ProductQuantity;
        details.add(noodleDetail);
        //粉补货
        BackOrderParam.Detail riceDetail = new BackOrderParam.Detail();
        riceDetail.ProductCode = "0102";
        riceDetail.ProductName = "粉饼(薯粉)";
        riceDetail.ProductUnit="个";
        riceDetail.ProductStandard = "个";
        riceDetail.ProductPrice = Double.parseDouble("0");
        riceDetail.ProductQuantity = riceCount;
        riceDetail.ProductTotalMoney = riceDetail.ProductPrice * riceDetail.ProductQuantity;
        details.add(riceDetail);
        //TODO LIN 新鲜面补货 -------------------
       /* BackOrderParam.Detail freshDetail = new BackOrderParam.Detail();
        freshDetail.ProductCode = "0105";
        freshDetail.ProductName = "面饼(宽面)";
        freshDetail.ProductUnit="个";
        freshDetail.ProductStandard = "个";
        freshDetail.ProductPrice = Double.parseDouble("0");
        freshDetail.ProductQuantity = riceCount;
        freshDetail.ProductTotalMoney = freshDetail.ProductPrice * freshDetail.ProductQuantity;
        details.add(freshDetail);*/
        //一品类补货
        BackOrderParam.Detail suanLabaoDetail = new BackOrderParam.Detail();
        suanLabaoDetail.ProductCode = "0301";
        suanLabaoDetail.ProductName = "香辣包";
        suanLabaoDetail.ProductUnit="包";
        suanLabaoDetail.ProductStandard = "包";
        suanLabaoDetail.ProductPrice = Double.parseDouble("0");
        suanLabaoDetail.ProductQuantity = spicyCount;
        suanLabaoDetail.ProductTotalMoney = suanLabaoDetail.ProductPrice * suanLabaoDetail.ProductQuantity;
        details.add(suanLabaoDetail);
        //二,三,四品类补货
        ArrayList<DropPackageDB> dropPackageDBs = DBNoodleHelper.queryByProductStatus("");
        for (int i = 0; i < dropPackageDBs.size(); i++) {
            DropPackageDB dropPackageDB = dropPackageDBs.get(i);
            BackOrderParam.Detail detail = new BackOrderParam.Detail();
            detail.ProductCode = dropPackageDB.ProdutCode;
            detail.ProductName = dropPackageDB.ProductName;
            detail.ProductUnit=dropPackageDB.storeUnit;
            detail.ProductStandard = dropPackageDB.standard;
            detail.ProductPrice = Double.parseDouble("0");
            detail.ProductQuantity = getCount(i,legCount,eggCount,fourCatogoryCount);
            detail.ProductTotalMoney = detail.ProductPrice * detail.ProductQuantity;
            details.add(detail);
        }

        backOderParam.Details = details;

        //补货总钱数
        int totalMoney = 0;
        for (int i = 0; i < details.size(); i++) {
            totalMoney += details.get(i).ProductTotalMoney;
        }
        backOderParam.TotalMoney = totalMoney+"";
//        Timber.e("补货的参数:"+JsonHelper.getGson().toJson(backOderParam));
        return backOderParam;
    }

    private static int getCount(int index,int twoCatogoryCount,int threeCatogoryCount,int fourCatogoryCount){
        int count=0;
        switch (index){
            case 0:
                count=twoCatogoryCount;
                break;
            case 1:count=threeCatogoryCount;
                break;
            case 2:count = fourCatogoryCount;
                break;
            case 3:Timber.e("赠品列表超过3种");
                break;
                default:
        }
        return count;
    }


    //获取返给给js端的抽奖等级和对应的库存数
    public static void getLuckDrawParams(final CallBackFunction function, Handler mainHandler, final NoodleTradeModel mNoodleTradeModel) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {

                int firstPrizeNum=0;
                int secondPrizeNum = 0;
                int thirdPrizeNum = 0;
                ArrayList<RiceND> firstPrizeNDs = DBNoodleHelper.querynoodleStatusNoolde(FormulaPreferenceConfig.getFirstPrizeCategory());
                ArrayList<RiceND> secondPrizeNDs = DBNoodleHelper.querynoodleStatusNoolde(FormulaPreferenceConfig.getSecondPrizeCategory());
                ArrayList<RiceND> thirdPrizeNDs = DBNoodleHelper.querynoodleStatusNoolde(FormulaPreferenceConfig.getThirdPrizeCategory());// 鸡腿改成了榨菜，但最大的库存量不变
                for (int i=0;i<firstPrizeNDs.size();i++){
                    firstPrizeNum+=firstPrizeNDs.get(i).totalNum;
                }
                for (int i=0;i<secondPrizeNDs.size();i++){
                    secondPrizeNum+=secondPrizeNDs.get(i).totalNum;
                }
                for (int i=0;i<thirdPrizeNDs.size();i++){
                    thirdPrizeNum+=thirdPrizeNDs.get(i).totalNum;
                }
                String firstPrizeProductID = NoodleTradeFieldUtil.getProductID(NoodleNameConstants.SPOIL_FIRST_PRIZE);
                String secondPrizeProductID = NoodleTradeFieldUtil.getProductID(NoodleNameConstants.SPOIL_SECOND_PRIZE);
                String thirdPrizeProductID = NoodleTradeFieldUtil.getProductID(NoodleNameConstants.SPOIL_THIRD_PRIZE);

                BeginLotteryModel beginLotteryModel = new BeginLotteryModel();
                //获取SellOutIyem
                List<BeginLotteryModel.SellOutIyem> list = new ArrayList<BeginLotteryModel.SellOutIyem>();
                //99赠品类：如：9901卤蛋，9902榨菜，9903鸡腿
                BeginLotteryModel.SellOutIyem firstPrizesellOutIyem = beginLotteryModel.new SellOutIyem();
                firstPrizesellOutIyem.MenuTypeCode = firstPrizeProductID;
                firstPrizesellOutIyem.MenuItemCode = "";
                firstPrizesellOutIyem.MenuItemCName = NoodleNameConstants.SPOIL_FIRST_PRIZE;
                firstPrizesellOutIyem.stockNum = firstPrizeNum;

                BeginLotteryModel.SellOutIyem secondPrizeSellOutIyem = beginLotteryModel.new SellOutIyem();
                secondPrizeSellOutIyem.MenuTypeCode = secondPrizeProductID;
                secondPrizeSellOutIyem.MenuItemCode = "";
                secondPrizeSellOutIyem.MenuItemCName = NoodleNameConstants.SPOIL_SECOND_PRIZE;
                secondPrizeSellOutIyem.stockNum = secondPrizeNum;

                BeginLotteryModel.SellOutIyem thirdPrizeSellOutIyem = beginLotteryModel.new SellOutIyem();
                thirdPrizeSellOutIyem.MenuTypeCode = thirdPrizeProductID;
                thirdPrizeSellOutIyem.MenuItemCode = "";
                thirdPrizeSellOutIyem.MenuItemCName = NoodleNameConstants.SPOIL_THIRD_PRIZE;
                thirdPrizeSellOutIyem.stockNum = thirdPrizeNum;

                list.add(firstPrizesellOutIyem);
                list.add(secondPrizeSellOutIyem);
                list.add(thirdPrizeSellOutIyem);
                //返回的数据
                beginLotteryModel.lotteryNum = mNoodleTradeModel.total_num;
                beginLotteryModel.sellOutIyem = list;

                String json = JsonHelper.getGson().toJson(beginLotteryModel);
                Timber.e("返回给浏览器的数据:"+json);

                function.onCallBack(JsonHelper.getGson().toJson(beginLotteryModel));
            }
        });
    }

    //清除物品信息参数
    public static ClearStockParam getClearStockParam() {
        ClearStockParam clearStockParam = new ClearStockParam();
        clearStockParam.LIDCode = MethodConstants.SHOPCODE;

       /* ArrayList<String> list = new ArrayList<String>();
        list.add("");*/

        clearStockParam.ProductCode = "";

        return clearStockParam;
    }

    //加菜参数
    public static AddOrderItemParam getAddOrderItemParam(NoodleTradeModel noodleTradeModel) {
        AddOrderItemParam addOrderItemParam = new AddOrderItemParam();
        addOrderItemParam.shopCode = MethodConstants.SHOPCODE;

        AddOrderItemParam.OrderData order = new AddOrderItemParam.OrderData();
        order.BillNo = noodleTradeModel.take_meal_No;
        order.GUID = noodleTradeModel.order_No;
        order.GuestQty ="1";
        order.OrderType =1;
        order.TableNo ="01";
        order.SumPrice = FormatUtil.flaotToDouble(noodleTradeModel.total_price);
        order.IsPay = 0;
        order.PayType = noodleTradeModel.pay_type;
        order.PayTime = noodleTradeModel.pay_time;
        //店标,和门店编号shopCode的值一样
        order.LID = MethodConstants.SHOPCODE;
       /* orderData.PayCardCode = "";*/
        order.Waiter = "01";
        order.WaiterName = "E面机器";
        order.Cashier = "01";
        order.CashierName = "E面机器";
        order.Checker = "01";
        order.CheckerName = "E面机器";
        order.Operator = "01";
        order.OperatorName = "E面机器";
        order.OrderDate = noodleTradeModel.pay_time;
        List<AddOrderItemParam.OrderDetail> orderDetails = new ArrayList<>();
        List<CloseLotteryModel> lotteryList = NoodleTradeFieldUtil.getLotteryList(noodleTradeModel);
        for (int i = 0; i < lotteryList.size(); i++) {
            CloseLotteryModel closeLotteryModel = lotteryList.get(i);
            AddOrderItemParam.OrderDetail orderDetail = new AddOrderItemParam.OrderDetail();


            double Price = FormatUtil.flaotToDouble(0);
            String productID = NoodleTradeFieldUtil.getProductID(closeLotteryModel.spoilName);
            orderDetail.productId = productID;
            orderDetail.productName = NoodleTradeFieldUtil.getProductName(productID);
            orderDetail.price = (float) 0;
            orderDetail.CostPrice = (float) 0;
            orderDetail.SourcePrice = (float) 0;
            orderDetail.isSuite = 0;
            orderDetail.count = closeLotteryModel.count;
//            orderDetail.
            orderDetail.Remark = "赠品";
            orderDetail.OrderDate=noodleTradeModel.pay_time;
            orderDetail.CheckTime=noodleTradeModel.pay_time;
            orderDetails.add(orderDetail);
        }
        order.orderDetails = orderDetails;
        addOrderItemParam.Order = order;
        return addOrderItemParam;
    }

    //加菜参数
    public static AddOrderItemParam getAddOrderBrineParam(NoodleTradeModel noodleTradeModel) {
        AddOrderItemParam addOrderItemParam = new AddOrderItemParam();
        addOrderItemParam.shopCode = MethodConstants.SHOPCODE;

        AddOrderItemParam.OrderData order = new AddOrderItemParam.OrderData();
        order.BillNo = noodleTradeModel.take_meal_No;
        order.GUID = noodleTradeModel.order_No;
        order.GuestQty ="1";
        order.OrderType =1;
        order.TableNo ="01";
        order.SumPrice = FormatUtil.flaotToDouble(noodleTradeModel.total_price);
        order.IsPay = 0;
        order.PayType = noodleTradeModel.pay_type;
        order.PayTime = noodleTradeModel.pay_time;
        //店标,和门店编号shopCode的值一样
        order.LID = MethodConstants.SHOPCODE;
       /* orderData.PayCardCode = "";*/
        order.Waiter = "01";
        order.WaiterName = "E面机器";
        order.Cashier = "01";
        order.CashierName = "E面机器";
        order.Checker = "01";
        order.CheckerName = "E面机器";
        order.Operator = "01";
        order.OperatorName = "E面机器";
        order.OrderDate = noodleTradeModel.pay_time;
        List<AddOrderItemParam.OrderDetail> orderDetails = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            AddOrderItemParam.OrderDetail orderDetail = new AddOrderItemParam.OrderDetail();
            double Price = FormatUtil.flaotToDouble(0);
            orderDetail.productId = "1198";
            orderDetail.productName = "卤水";
            orderDetail.price = (float) 0;
            orderDetail.CostPrice = (float) 0;
            orderDetail.SourcePrice = (float) 0;
            orderDetail.isSuite = 0;
            orderDetail.count = noodleTradeModel.total_num * NoodleDataUtil.getMaxCapacityBrine();
            orderDetail.Remark = "赠品";
            orderDetail.OrderDate=noodleTradeModel.pay_time;
            orderDetail.CheckTime=noodleTradeModel.pay_time;
            orderDetails.add(orderDetail);
        }
        order.orderDetails = orderDetails;
        addOrderItemParam.Order = order;
        return addOrderItemParam;
    }
}
