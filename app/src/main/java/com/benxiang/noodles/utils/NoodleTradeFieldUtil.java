package com.benxiang.noodles.utils;

import android.annotation.SuppressLint;

import com.benxiang.noodles.contants.NoodleNameConstants;
import com.benxiang.noodles.data.DBNoodleHelper;
import com.benxiang.noodles.data.table.DropPackageDB;
import com.benxiang.noodles.model.ListModle;
import com.benxiang.noodles.model.NoodleTradeModel;
import com.benxiang.noodles.model.lottery.CloseLotteryModel;
import com.benxiang.noodles.serialport.data.sp.FormulaPreferenceConfig;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Administrator on 2017/12/12.
 */
public class NoodleTradeFieldUtil {
    /**
     * 将奖品全部设置为谢谢惠顾
     */
    public static void setListModlesWithLottery(NoodleTradeModel noodleTradeModel) {
        //抽奖时抽到的数据
        List<ListModle> listModles = noodleTradeModel.listModles;
        for (int i=0;i<listModles.size();i++){
            ListModle listModle = listModles.get(i);
            int goods_num = listModle.goods_num;
            //每个listModle对应一个closeLotteryModels
            List<CloseLotteryModel> closeLotteryModels = new ArrayList<CloseLotteryModel>();
            for (int j=0;j<goods_num;j++){
                CloseLotteryModel closeLotteryModel = new CloseLotteryModel();
                closeLotteryModel.spoilName="谢谢惠顾";
                closeLotteryModel.spoilNo = "";
                closeLotteryModels.add(closeLotteryModel);
            }
            listModle.closeLotteryModels = closeLotteryModels;
        }
    }

    /**
     * 将抽取到的奖品赋值给NoodleTradeModel
     * @param noodleTradeModel
     * @param data
     */
    @SuppressLint("BinaryOperationInTimber")
    public static void setCloseLotteryModels(NoodleTradeModel noodleTradeModel, String data) {
        Timber.e("抽奖抽到的奖品："+data);
        //数据装换成对象==>Data To Object
        Type closeLotteryType = new TypeToken<List<CloseLotteryModel>>() {}.getType();
        List<CloseLotteryModel> closeLotteryBeans = JsonHelper.getGson().fromJson(data,closeLotteryType);
        if (closeLotteryBeans==null || noodleTradeModel==null){
            return;
        }
        //赋值
        int lotterySize = closeLotteryBeans.size();
        int index = 0;
        List<ListModle> listModles = noodleTradeModel.listModles;
        for (int i=0;i<listModles.size();i++){
            ListModle listModle = listModles.get(i);
            int goods_num = listModle.goods_num;
            //每个listModle对应一个closeLotteryModels
            List<CloseLotteryModel> closeLotteryModels = new ArrayList<CloseLotteryModel>();
            for (int j=0;j<goods_num;j++){
                closeLotteryModels.add(closeLotteryBeans.get(index++));
            }
            listModle.closeLotteryModels = closeLotteryModels;
        }
    }

    //用于加菜接口的转化，获取每种赠品的数量
    public static List<CloseLotteryModel> getLotteryList(NoodleTradeModel noodleTradeModel) {
        CloseLotteryModel jiTui = new CloseLotteryModel();
        CloseLotteryModel luDan = new CloseLotteryModel();
        CloseLotteryModel zhaCai = new CloseLotteryModel();
        List<ListModle> list = noodleTradeModel.listModles;
        for (int i = 0; i < list.size(); i++) {
            ListModle listModle = list.get(i);
            for (int j = 0; j < listModle.closeLotteryModels.size(); j++) {
                CloseLotteryModel closeLotteryModel = listModle.closeLotteryModels.get(j);
                if (closeLotteryModel.spoilName.equals("鸡腿")){//"鸡腿"
                    jiTui.spoilName=closeLotteryModel.spoilName;
                    jiTui.count+=1;
                } else if (closeLotteryModel.spoilName.equals("卤蛋")){//"卤蛋"
                    luDan.spoilName=closeLotteryModel.spoilName;
                    luDan.count+=1;
                } else if (closeLotteryModel.spoilName.equals("榨菜")){//"榨菜"
                    zhaCai.spoilName=closeLotteryModel.spoilName;
                    zhaCai.count+=1;
                }else if (closeLotteryModel.spoilName.equals(NoodleNameConstants.SPOIL_FIRST_PRIZE)){//"一等奖"
                    jiTui.spoilName=closeLotteryModel.spoilName;
                    jiTui.count+=1;
                }else if (closeLotteryModel.spoilName.equals(NoodleNameConstants.SPOIL_SECOND_PRIZE)){//"二等奖"
                    luDan.spoilName=closeLotteryModel.spoilName;
                    luDan.count+=1;
                }else if (closeLotteryModel.spoilName.equals(NoodleNameConstants.SPOIL_THIRD_PRIZE)){//"三等奖"
                    zhaCai.spoilName=closeLotteryModel.spoilName;
                    zhaCai.count+=1;
                }
            }
        }
        List<CloseLotteryModel> closeLotteryModelList = new ArrayList<>();
        if (jiTui.count>0){
            closeLotteryModelList.add(jiTui);
        }
        if (luDan.count>0){
            closeLotteryModelList.add(luDan);
        }
        if (zhaCai.count>0){
            closeLotteryModelList.add(zhaCai);
        }
        return closeLotteryModelList;
    }

    /**
     * 由奖品名获得菜品Id
     */
    public static String getProductID(String spoilName){
        String productId="";
        switch (spoilName){
            case "谢谢惠顾":
                break;
            case "卤蛋":
                productId="9901";
                break;
            case "榨菜":
                productId="9902";
                break;
            case "鸡腿":
                productId="9903";
                break;
            case NoodleNameConstants.SPOIL_FIRST_PRIZE:
                productId=getProductId(FormulaPreferenceConfig.getFirstPrizeProductCode());
                break;
            case NoodleNameConstants.SPOIL_SECOND_PRIZE:
                productId=getProductId(FormulaPreferenceConfig.getSecondPrizeProductCode());
                break;
            case NoodleNameConstants.SPOIL_THIRD_PRIZE:
                productId=getProductId(FormulaPreferenceConfig.getThirdPrizeProductCode());
                break;
            default:
                Timber.e("未知奖品");
                break;
        }
        return productId;
    }

    private static String getProductId(String productCode){
        String productId="";
        ArrayList<DropPackageDB> dropPackageDBs = DBNoodleHelper.queryByProductCode(productCode);
        if (dropPackageDBs.size()>0){
            productId=dropPackageDBs.get(0).menuItemCode;
        }
        return productId;
    }

    /**
     * 由产品code获得菜品名
     */
    public static String getProductName(String productCode){
        String productName="";
        ArrayList<DropPackageDB> dropPackageDBs = DBNoodleHelper.queryByProductCode(productCode);
        if (dropPackageDBs.size()>0){
            productName=dropPackageDBs.get(0).menuItemCName;
        }
        return productName;
    }

}
