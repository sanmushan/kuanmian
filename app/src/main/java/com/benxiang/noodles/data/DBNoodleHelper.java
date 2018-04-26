package com.benxiang.noodles.data;

import android.text.TextUtils;
import android.util.Log;

import com.benxiang.noodles.contants.DbTypeContants;
import com.benxiang.noodles.data.table.DropPackageDB;
import com.benxiang.noodles.data.table.RiceND;
import com.benxiang.noodles.data.table.RiceOrderND;
import com.benxiang.noodles.utils.NoodleDataUtil;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.litesuits.orm.db.model.ColumnsValue;
import com.litesuits.orm.db.model.ConflictAlgorithm;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.benxiang.noodles.data.DBFactory.getmDBNoodle;

/**
 * Created by 刘圣如 on 2017/8/24.
 */

public class DBNoodleHelper {
    private static final String TAG = "DBNoodleHelper";

    //RiceND表
    public static ArrayList<RiceND> queryNoTypeNoolde(int noodleNo) {
        return getmDBNoodle().query(QueryBuilder.create(RiceND.class).whereEquals(RiceND.COL_RICE_ND_TYPE, noodleNo));
    }

    public static ArrayList<RiceND> querynoodleStatusNoolde(int noodleStatus) {
        return getmDBNoodle().query(QueryBuilder.create(RiceND.class).whereEquals(RiceND.COL_RICE_ND_STATUS, noodleStatus));
    }

    public static RiceND queryNooldeNum(int noodleNo) {
        ArrayList<RiceND> riceNDs=DBFactory.getmDBNoodle().query(QueryBuilder.create(RiceND.class).whereEquals(RiceND.COL_RICE_ND, noodleNo));
        return riceNDs.get(0);
    }

    public static void upateNoodleStatus(int noodleNo, int status) {
        ColumnsValue cv = new ColumnsValue(new String[]{RiceND.COL_RICE_ND_STATUS}, new Object[]{status});
        getmDBNoodle().update(WhereBuilder.create(RiceND.class)
                .equals(RiceND.COL_RICE_ND, noodleNo), cv, ConflictAlgorithm.None);
    }

    //更新米粉号的总数量
    public static void upateNoodleNum(int noodleNo, int totalNum) {
        ColumnsValue cv = new ColumnsValue(new String[]{RiceND.COL_RICE_TOTAL_NUM}, new Object[]{totalNum});

        getmDBNoodle().update(WhereBuilder.create(RiceND.class)
                .equals(RiceND.COL_RICE_ND, noodleNo), cv, ConflictAlgorithm.None);


        //当数量为0，状态设置为0，没货
        if (totalNum == 0) {
            ColumnsValue cv2 = new ColumnsValue(new String[]{RiceND.COL_RICE_ND_STATUS}, new Object[]{0});
            getmDBNoodle().update(WhereBuilder.create(RiceND.class)
                    .equals(RiceND.COL_RICE_ND, noodleNo), cv2, ConflictAlgorithm.None);
        }
    }

    //根据标识获取已付款的库存信息：0：没做，1:正在做，2：已经做完
    //RiceOrderND
    public static List<RiceOrderND> queryWithoutNoodle(int noodleSign) {
        return getmDBNoodle().query(QueryBuilder.create(RiceOrderND.class).whereEquals(RiceOrderND.COL_RICE_SIGN,noodleSign));
    }


    public static void upateNoodleSign(int noodleNo,int startNoodleSign,int endNoodleSign) {
        RiceOrderND riceOrderND = queryWithoutNoodle(startNoodleSign).get(0);
        ColumnsValue columnsValue = new ColumnsValue(new String[]{RiceOrderND.COL_RICE_SIGN}, new Object[]{endNoodleSign});
        getmDBNoodle().update(WhereBuilder.create(RiceOrderND.class).equals(RiceOrderND.COL_ID, riceOrderND.id)
                ,columnsValue,ConflictAlgorithm.None);
    }

    public static void deleteNoodle(RiceOrderND riceOrderND) {
        getmDBNoodle().delete(riceOrderND);
    }

    //--------------------------------------------------------------------排队号-----------------------------------------------------------------------
    //根据排队号删除订单
    public static void deleteBySortNo(int sortNo){
        getmDBNoodle().delete(WhereBuilder.create(RiceOrderND.class).equals(RiceOrderND.COL_SORT_NO,sortNo));
    }

    public static void deleteAll(){
        getmDBNoodle().deleteAll(RiceOrderND.class);
    }

    //根据排队号查询订单
    public static ArrayList<RiceOrderND> queryBySortNo(int sortNo){
        return getmDBNoodle().query(QueryBuilder.create(RiceOrderND.class).whereEquals(RiceOrderND.COL_SORT_NO, sortNo));
    }

    //--------------------------------------------------------------------DropPackageDB-----------------------------------------------------------------------
    public static ArrayList<DropPackageDB> queryByProductStatus(String productStatus) {
        if (TextUtils.isEmpty(productStatus)){
            return getmDBNoodle().query(QueryBuilder.create(DropPackageDB.class).whereEquals(DropPackageDB.COL_PRODUCT_STATUS, DbTypeContants.PRODUCT_STATUS));
        }
        return getmDBNoodle().query(QueryBuilder.create(DropPackageDB.class).whereEquals(DropPackageDB.COL_PRODUCT_STATUS, productStatus));
    }

    public static ArrayList<DropPackageDB> queryByProductCode(String productCode) {
        return getmDBNoodle().query(QueryBuilder.create(DropPackageDB.class).whereEquals(DropPackageDB.COL_PRODUCT_CODE, productCode));
    }

}
