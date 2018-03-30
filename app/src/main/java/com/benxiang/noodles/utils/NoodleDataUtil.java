package com.benxiang.noodles.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.contants.DbTypeContants;
import com.benxiang.noodles.contants.ImagesConstants;
import com.benxiang.noodles.contants.NoodleNameConstants;
import com.benxiang.noodles.data.DBFactory;
import com.benxiang.noodles.data.DBNoodleHelper;
import com.benxiang.noodles.data.NoodleDB;
import com.benxiang.noodles.data.noodle.NoodleEventData;
import com.benxiang.noodles.data.table.RiceND;
import com.benxiang.noodles.data.table.RiceOrderND;
import com.benxiang.noodles.model.CostCardDataModel;
import com.benxiang.noodles.model.ListModle;
import com.benxiang.noodles.model.NoodleTradeModel;
import com.benxiang.noodles.model.information.InformationModle;
import com.benxiang.noodles.model.lottery.CloseLotteryModel;
import com.benxiang.noodles.model.remote.OrderNumQueryModel;
import com.benxiang.noodles.serialport.data.sp.FormulaPreferenceConfig;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * @author LIN
 * Created by 刘圣如 on 2017/10/17.
 */

@SuppressWarnings("ALL")
public class NoodleDataUtil {
    //接口物品信息

    /**
     * @param menuData
     * @param noodleStatus  数据库所对应的面或粉的状态，11为面，22为粉
     * @param startNo
     * @return
     */
    public static NoodleTradeModel getData(InformationModle.MenuData menuData, int noodleStatus,int startNo) {
        ArrayList<ListModle> listModles = new ArrayList<>();
        NoodleTradeModel model = new NoodleTradeModel();
        //口味
        String noodleState = "";
        int riceRecipe = 0;
        //数据库是否有库存:isStapleFood(面\粉\卤水),isStock(面\粉\掉料包)
        boolean isStock = false;
        boolean isStapleFood = false;
        //价格
        float price;
        //大类编号，如：面或粉的编号   //截取最后一个数
        int noodleNo = Integer.parseInt(menuData.MenuTypeCode.substring(menuData.MenuTypeCode.length()-1));

        //由面\粉\卤水的状态status判断是否有库存
        if (DBNoodleHelper.querynoodleStatusNoolde(noodleStatus).size() > 0 && DBNoodleHelper.querynoodleStatusNoolde(DbTypeContants.BRINE_STATUS).size() > 0) {
            isStapleFood = true;
        } else {
            isStapleFood = false;
        }
        Timber.e("是否有库存 " + isStapleFood);
        for (int i = 0; i < menuData.menuItemData.size(); i++) {
            InformationModle.MenuItemData menuItemData = menuData.menuItemData.get(i);
            ListModle listModle = new ListModle();
            listModle.goods_prive = (float) menuItemData.MenuItemPrice;
            int itemType = Integer.parseInt(menuItemData.MenuItemCode.substring(menuItemData.MenuItemCode.length() - 1));
            listModle.riceType = noodleNo;
            listModle.productId = menuItemData.MenuItemCode;
//            listModle.goods_no = riceND.noodleNo;
            //TODO LIN 增加主面时，需要增加case。下面是做对应的面对应面的配料包
            if (noodleNo == 2) {
                //粉
                listModle.goods_no =startNo + i;
                switch (itemType) {
                    case 1:
                        noodleState = NoodleNameConstants.QINGTANG;
                        riceRecipe = 1;
                        isStock = isStapleFood;
                        break;
                    case 2:
                        riceRecipe = 2;
                        noodleState = NoodleNameConstants.SUANLA;
                        isStock = isStapleFood&&(DBNoodleHelper.querynoodleStatusNoolde(DbTypeContants.SUANLABAO).size() > 0 ? true : false);
                        break;
                    case 3:
                        //卤蛋
                        riceRecipe = 3;
                        noodleState = NoodleNameConstants.LUDAN;
                        isStock = isStapleFood && (DBNoodleHelper.querynoodleStatusNoolde(DbTypeContants.SUANLABAO).size() > 0 ? true : false) &&
                                (DBNoodleHelper.querynoodleStatusNoolde(DbTypeContants.LUJIDANG).size() > 0 ? true : false);
                        break;
                    case 4:
                        //鸡腿
                        riceRecipe = 4;
                        noodleState = NoodleNameConstants.HAOHUA;
                        isStock = isStapleFood && (DBNoodleHelper.querynoodleStatusNoolde(DbTypeContants.SUANLABAO).size() > 0 ? true : false) &&
                                (DBNoodleHelper.querynoodleStatusNoolde(DbTypeContants.SUANLAJITUI).size() > 0 ? true : false);
                        break;
                    default:
                        noodleState = NoodleNameConstants.QINGTANG;
                        riceRecipe = 1;
                        isStock = isStapleFood;
                        break;
                }
            } else if (noodleNo == 1){
                //面
                listModle.goods_no = i + startNo;
                switch (itemType) {
                    case 1:
                        riceRecipe = 1;
                        noodleState = NoodleNameConstants.QINGTANG;
                        isStock = isStapleFood;
                        break;
                    case 2:
                        riceRecipe = 2;
                        noodleState = NoodleNameConstants.CHONGQING;
                        isStock =isStapleFood && (DBNoodleHelper.querynoodleStatusNoolde(DbTypeContants.SUANLABAO).size() > 0 ? true : false);
                        break;
                    case 3:
                        riceRecipe = 3;
                        //TODO LIN 3.8
                        noodleState = NoodleNameConstants.LUDAN;
                        isStock = isStapleFood && (DBNoodleHelper.querynoodleStatusNoolde(DbTypeContants.SUANLABAO).size() > 0 ? true : false) &&
                                (DBNoodleHelper.querynoodleStatusNoolde(DbTypeContants.LUJIDANG).size() > 0 ? true : false);
                        break;
                    case 4:
                        riceRecipe = 4;
                        //TODO LIN 3.8
                        noodleState = NoodleNameConstants.HAOHUA;
                        isStock = isStapleFood && (DBNoodleHelper.querynoodleStatusNoolde(DbTypeContants.SUANLABAO).size() > 0 ? true : false) &&
                                (DBNoodleHelper.querynoodleStatusNoolde(DbTypeContants.SUANLAJITUI).size() > 0 ? true : false);
                        break;
                    default:
                        noodleState = NoodleNameConstants.QINGTANG;
                        riceRecipe = 1;
                        isStock = isStapleFood;
                        break;
                }
            }else{
                //新鲜面
                listModle.goods_no = i + startNo;
                switch (itemType) {
                    case 1:
                        riceRecipe = 1;
                        noodleState = NoodleNameConstants.FRESH;
                        isStock = isStapleFood && (DBNoodleHelper.querynoodleStatusNoolde(DbTypeContants.SUANLABAO).size() > 0 ? true : false);
                        break;
                    default:
                        noodleState = NoodleNameConstants.FRESH;
                        isStock = isStapleFood && (DBNoodleHelper.querynoodleStatusNoolde(DbTypeContants.SUANLABAO).size() > 0 ? true : false);
                        riceRecipe = 1;
                        break;
                }
            }

            listModle.rice_recipe_Type = riceRecipe;
            listModle.stock = isStock;
            Log.e("图片", "rice_recipe_Type: "+noodleNo+isStock);
            switch (noodleNo) {
                case 1:
                    if (listModle.stock) {
                        Log.e("图片", "getNoodleTradeModel: "+ ImagesConstants.noodle_ig_stocks[i]);
                        listModle.ig_url = ImagesConstants.noodle_ig_stocks[i];
                    } else {
                        listModle.ig_url = ImagesConstants.noodle_ig_unstocks[i];
                    }
                    break;
                case 2:
                    if (listModle.stock) {
                            Log.e("图片", "getNoodleTradeModel2: "+ ImagesConstants.rice_ig_stocks[i]);
                            listModle.ig_url = ImagesConstants.rice_ig_stocks[i];
                        }else {
                            Log.e("图片", "getNoodleTradeModel3: "+ ImagesConstants.rice_ig_unstocks[i]);
                            listModle.ig_url = ImagesConstants.rice_ig_unstocks[i];
                        }
                    break;
                    //TODO  ------------------------------------------------------------3.13
                case 3:
                    if (listModle.stock) {
                        Log.e("图片", "getNoodleTradeModel2: "+ ImagesConstants.rice_ig_stocks[i]);
                        listModle.ig_url = ImagesConstants.rice_ig_stocks[i];
                    }else {
                        Log.e("图片", "getNoodleTradeModel3: "+ ImagesConstants.rice_ig_unstocks[i]);
                        listModle.ig_url = ImagesConstants.rice_ig_unstocks[i];
                    }
                    break;

                case 4:
                    if (listModle.stock) {
                        Log.e("图片", "getNoodleTradeModel2: "+ ImagesConstants.rice_ig_stocks[i]);
                        listModle.ig_url = ImagesConstants.rice_ig_stocks[i];
                    }else {
                        Log.e("图片", "getNoodleTradeModel3: "+ ImagesConstants.rice_ig_unstocks[i]);
                        listModle.ig_url = ImagesConstants.rice_ig_unstocks[i];
                    }
                    break;

//                    //-------------------------------------------------------------------------------
                    default:
                        if (listModle.stock) {
                            Log.e("图片", "getNoodleTradeModel: "+ ImagesConstants.noodle_ig_stocks[i]);
                            listModle.ig_url = ImagesConstants.noodle_ig_stocks[i];
                        } else {
                            listModle.ig_url = ImagesConstants.noodle_ig_unstocks[i];
                        }
                    break;
            }
            listModle.ImagePath =PreferenceUtil.config().getHttpAddress(Constants.HTTP_ADDRESS) + menuData.menuItemData.get(i).ImagePath;
            listModle.SmallImagePath =PreferenceUtil.config().getHttpAddress(Constants.HTTP_ADDRESS) + menuData.menuItemData.get(i).SmallImagePath;
            listModle.riceTaste = noodleState;
            listModle.goods_name = menuItemData.MenuItemCName;
            listModles.add(listModle);
        }
        model.listModles = listModles;
        return model;
    }

    //由排队号和models设置数据库的订单信息（1）
    private static NoodleTradeModel mNoodleTradeModel;
    public static void getOrderToDB(int sortNo, NoodleTradeModel noodleTradeModel) {
        mNoodleTradeModel = noodleTradeModel;
        DblistModles(sortNo, noodleTradeModel.listModles);
    }

    //由排队号和models设置数据库的订单信息（1）
    private static void getOrderToDB(int sortNo, List<ListModle> models) {
        DblistModles(sortNo, models);
    }

    public static List<ListModle> getlistModles(List<ListModle> listModles) {

        List<ListModle> noodleLists = new ArrayList<>();
        for (int i = 0; i < listModles.size(); i++) {
            int num = listModles.get(i).goods_num;
            Timber.e("数量" + listModles.get(i).goods_num);
            for (int j = 0; j < num; j++) {
                Timber.e("次数" + num);
                ListModle listModle = new ListModle();
                listModle.goods_num = 1;
                listModle.rice_recipe_Type = listModles.get(i).rice_recipe_Type;
                listModle.riceTaste = listModles.get(i).riceTaste;
                listModle.goods_name = listModles.get(i).goods_name;
                listModle.goods_no = listModles.get(i).goods_no;
                listModle.goods_prive = listModles.get(i).goods_prive;
                listModle.riceType = listModles.get(i).riceType;
                listModle.stock = listModles.get(i).stock;

//                listModle.goods_num = 1;
                noodleLists.add(listModle);
            }
        }
        return noodleLists;
    }

    /**
     * 由排队号和models设置数据库的订单信息（2）
     * @param shorNo
     * @param listModles
     */
    @SuppressLint("BinaryOperationInTimber")
    public static void DblistModles(int shorNo, List<ListModle> listModles) {
        for (int i = 0; i < listModles.size(); i++) {
            int num = listModles.get(i).goods_num;

            Timber.e("数量" + listModles.get(i).goods_num);

            for (int j = 0; j < num; j++) {

                Timber.e("类型" + listModles.get(i).riceType);

                switch (listModles.get(i).riceType) {
                    //TODO LIN 扣除本地库存
                    case 1:
                        dbChange(shorNo, DbTypeContants.MIANTIAO, listModles.get(i),listModles.get(i).closeLotteryModels.get(j));
                        Log.w("linbin","扣除本地库存 面库存 ");
                        break;
                    case 2:
                        dbChange(shorNo, DbTypeContants.MIFEN, listModles.get(i),listModles.get(i).closeLotteryModels.get(j));
                        Log.w("linbin","扣除本地库存 粉库存 ");
                        break;
                    //新鲜面
                    case 5:
                        dbChange(shorNo, DbTypeContants.FRESH_NOODLES, listModles.get(i),listModles.get(i).closeLotteryModels.get(j));
                        Log.w("linbin","扣除本地库存 新鲜面库存 ");
                        break;
                        default:
                }
                Timber.e("次数" + num);
            }
        }
    }

    /**
     * 由排队号和models设置数据库的订单信息（3）
     * @param shortNo
     * @param miantiaoNo
     * @param listModle
     * @param closeLotteryModel
     */
    private static void dbChange(int shortNo, int miantiaoNo, ListModle listModle, CloseLotteryModel closeLotteryModel) {
        RiceND riceND = DBNoodleHelper.querynoodleStatusNoolde(miantiaoNo).get(0);
        RiceOrderND riceOrderND = new RiceOrderND();
        riceOrderND.noodleState = listModle.riceTaste;
        riceOrderND.noodleSign = 0;
        riceOrderND.noodleNo = riceND.noodleNo;
        riceOrderND.noodleName = listModle.goods_name;
        riceOrderND.sortNo = shortNo;
        riceOrderND.spoilName = closeLotteryModel.spoilName;
        //多单多碗退款添加的字段
        riceOrderND.goodsPrice = listModle.goods_prive;
        riceOrderND.total_price = mNoodleTradeModel.total_price;
        riceOrderND.payType = mNoodleTradeModel.pay_type;
        riceOrderND.order_No = mNoodleTradeModel.order_No;
        riceOrderND.take_meal_No = mNoodleTradeModel.take_meal_No;
        riceOrderND.isSetBillStatusByTakeNumber = mNoodleTradeModel.isSetBillStatusByTakeNumber;
        DBFactory.getmDBNoodle().insert(riceOrderND);
        DBNoodleHelper.upateNoodleNum(riceND.noodleNo, riceND.totalNum - 1);
        //卤水的碗数减一
        RiceND brineND = DBNoodleHelper.querynoodleStatusNoolde(DbTypeContants.BRINE_STATUS).get(0);
        DBNoodleHelper.upateNoodleNum(brineND.noodleNo, brineND.totalNum - 1);
    }


    //将数据添加到数据库中（1）
    public static void addRiceDb(List<NoodleEventData> listModles) {
        for (int i = 0; i < listModles.size(); i++) {
            RiceND riceND = DBNoodleHelper.queryNooldeNum(listModles.get(i).noodle_no);
            DBNoodleHelper.upateNoodleNum(listModles.get(i).noodle_no, riceND.totalNum + 1);
        }
    }

    /**
     * 将数据添加到数据库中,多单多碗中添加的库存设置（2）
     * @param riceOrderNDs
     */
    public static void addRiceDbMany(List<RiceOrderND> riceOrderNDs) {
        for (int i = 0; i < riceOrderNDs.size(); i++) {
            RiceND riceND = DBNoodleHelper.queryNooldeNum(riceOrderNDs.get(i).noodleNo);
            DBNoodleHelper.upateNoodleNum(riceOrderNDs.get(i).noodleNo, riceND.totalNum + 1);
        }
    }

    //获取订单的值
    public static NoodleTradeModel getNoodleTradeModel(OrderNumQueryModel strMsg, String mealNo) {
        NoodleTradeModel mNoodleTradeModel = new NoodleTradeModel();
        List<ListModle> listModles = new ArrayList<>();
//        listModles.clear();
        //数据库是否有库存:isStapleFood(面\粉\卤水),isStock(面\粉\掉料包)
        boolean isStapleFood=false;
        boolean isNonStapleFood=true;
        String noodleState = "";
        //获得当前各种物品的库存量
        int riceNo=getDbNum(DbTypeContants.MIFEN);
        int noodleNo=getDbNum(DbTypeContants.MIANTIAO);
        int freshNo=getDbNum(DbTypeContants.FRESH_NOODLES);
        int suanlabaoNo=getDbNum(DbTypeContants.SUANLABAO);
        int brineNo=getDbNum(DbTypeContants.BRINE_STATUS);


        //TODO 3.8 修改，增加鸡腿面，卤蛋面 LIN ----------------------------------------------------------------------------------
        int chickenLeg = getDbNum(DbTypeContants.SUANLAJITUI);
        int brineEgg = getDbNum(DbTypeContants.LUJIDANG);


        Timber.e("酸辣包数量"+suanlabaoNo);
        mNoodleTradeModel.ggid = strMsg.billHis.GUID;
        mNoodleTradeModel.total_num = strMsg.billHis.BillItemCount;
        mNoodleTradeModel.total_price = (float) strMsg.billHis.OrderTotal;
        mNoodleTradeModel.take_meal_No = mealNo;
        mNoodleTradeModel.order_No = strMsg.billHis.OutTradeNo;
        mNoodleTradeModel.pay_time = strMsg.billHis.BillDateTime.replace("T"," ");
        mNoodleTradeModel.pay_type = Integer.parseInt(strMsg.billHis.PayType);
        List<OrderNumQueryModel.BillHis.BillItems> billItemsList = strMsg.billHis.billItemsList;
        for (int i = 0; i < billItemsList.size(); i++) {
            for (int j=0;j<billItemsList.get(i).ItemCount;j++) {
                Timber.e("米粉数量 =" + riceNo);
                Timber.e("面条数量 = " + noodleNo);
                Timber.e("新鲜面数量 = " + freshNo);
                ListModle listModle = new ListModle();
                OrderNumQueryModel.BillHis.BillItems billItems = billItemsList.get(i);
                listModle.goods_num = 1;
                listModle.goods_name = billItems.ItemName;
                listModle.goods_prive = (float) billItemsList.get(i).ItemPrice;
                listModle.itemTime = billItems.SyncTime.replace("T", " ");
                //大类编号，判断面或粉有无库存
                int rice_recipe_Type = Integer.parseInt(billItems.MenuTypeCode.substring(billItems.MenuTypeCode.length() - 1));
                switch (rice_recipe_Type) {
                    case 1:
                        if (noodleNo > 0 && brineNo >0) {
                            isStapleFood = true;
                        } else {
                            isStapleFood = false;
                        }
                        noodleNo--;
                        brineNo--;
                        listModle.riceType = 1;
                        break;
                    case 2:
                        if (riceNo > 0 && brineNo >0 ) {
                            isStapleFood = true;
                        } else {
                            isStapleFood = false;
                        }
                        riceNo--;
                        brineNo--;
                        listModle.riceType = 2;
                        break;
                        //新鲜面
                    case 5:
                        if (freshNo > 0 && brineNo >0 ) {
                            isStapleFood = true;
                        } else {
                            isStapleFood = false;
                        }
                        freshNo--;
                        brineNo--;
                        listModle.riceType = 8;
                        break;
                        default:
                }
                listModle.productId = billItems.ItemCode;
                //小类编号，判断掉料包是否有库存
                int riceType = Integer.parseInt(billItems.ItemCode.substring(billItems.ItemCode.length() - 1));
                switch (riceType) {
                    case 1:
                        noodleState = NoodleNameConstants.QINGTANG;
                        break;
                    case 2:
                        if (suanlabaoNo < 1) {
                            isNonStapleFood = false;
                        }
                        suanlabaoNo--;
                        if (rice_recipe_Type == 1) {
                            noodleState = NoodleNameConstants.CHONGQING;
                        } else {
                            noodleState = NoodleNameConstants.SUANLA;
                        }
                        break;

                    //TODO LIN 3.8  --------------------------------------------------------------------------------------------------
                    case 3:
                        if (brineEgg < 1) {
                            isNonStapleFood = false;
                        }
                        brineEgg--;
                        noodleState = NoodleNameConstants.LUDAN;

                        break;
                    case 4:
                        if (chickenLeg < 1) {
                            isNonStapleFood = false;
                        }
                        chickenLeg--;
                        //鸡腿
                        noodleState = NoodleNameConstants.HAOHUA;
                        break;
                        default:
                }
                listModle.stock = isNonStapleFood && isStapleFood;



                listModle.riceTaste = noodleState;
                listModles.add(listModle);
            }
        }
        mNoodleTradeModel.listModles = listModles;

        //TODO LIN 3.8  --------------------------------------------------------------------------------------------------
        mNoodleTradeModel.hasEnoughIngredients = (noodleNo >= 0 && riceNo >= 0 && freshNo >= 0 && suanlabaoNo >= 0 && brineNo >= 0 && chickenLeg >= 0 && brineEgg >= 0);


        return mNoodleTradeModel;
    }

    /**
     * 由米粉类型获得对应存库的总数
     * @param goodsType
     * @return
     */
    public static int getDbNum(int goodsType) {
        int sum = 0;
        ArrayList<RiceND> list = DBNoodleHelper.querynoodleStatusNoolde(goodsType);
        for (int i = 0; i < list.size(); i++) {
            sum += list.get(i).totalNum;
        }
        Timber.e("库存的量=%d" + sum);
        return sum;
    }

    public static List<ListModle> changeListModles(List<ListModle> listModles, boolean isStrock) {
        List<ListModle> newListModles = new ArrayList<>();
        for (int i=0;i<listModles.size();i++) {
            if (listModles.get(i).stock == isStrock) {
                newListModles.add(listModles.get(i));
            }
        }
        return newListModles;
    }

    /**
     * 获得一碗面或粉需要卤水的最大容量
     * @return
     */
    public static int getMaxCapacityBrine(){
        int maxCapacity=0;
        if (FormulaPreferenceConfig.getTypeABrine()>=FormulaPreferenceConfig.getTypeBBrine()){
            maxCapacity = FormulaPreferenceConfig.getTypeABrine();
        }else {
            maxCapacity = FormulaPreferenceConfig.getTypeBBrine();
        }
        return maxCapacity;
    }

    /**
     * @param powelCount    做的碗数
     * @return  一共消耗毫升数
     */
    public static int getCalBrine(int powelCount){
        int calBrine=0;
        calBrine = powelCount * getMaxCapacityBrine();
        return calBrine;
    }

    /**
     * 将成本卡的品类信息添加到数据库中，并设置一、二、三等奖对应的品类
     * @param cardDataModel
     */
    public static void setCategory(CostCardDataModel cardDataModel){
        List<String> list = new ArrayList<>();
        boolean isDelete = true;
        List<CostCardDataModel.CardData> carDatas = cardDataModel.carDatas;
        for (int i=0 ; i<carDatas.size(); i++){
            CostCardDataModel.CardData cardData = carDatas.get(i);
            String codePrefix= cardData.MenuItemCode.substring(0, 2);
            CostCardDataModel.Product product = cardData.Products.get(0);
            Timber.e("codePrefix:"+codePrefix+",ProductCode"+product.ProductCode);
            if (codePrefix.equals("99")){
                list.add(product.ProductCode);
                NoodleDB.initDropPackageDB(product.ProductCode,product.ProductName,product.StoreUnit,codePrefix,cardData.MenuItemCode,cardData.MenuItemCName,cardData.Standard,isDelete);
                isDelete = false;
            }
        }  //TODO 3.8 LIN ----------------------------------------------------------------------------------------
        for (int i=0; i<list.size();i++){
            switch (i){
                case 0:
                    FormulaPreferenceConfig.setFirstPrizeProductCode(list.get(i));
                    break;
                case 1:
                    FormulaPreferenceConfig.setSecondPrizeProductCode(list.get(i));
                    break;
                case 2:
                    FormulaPreferenceConfig.setThirdPrizeProductCode(list.get(i));
                    break;
                case 3:
                    break;
                    default:
            }
        }
    }

}