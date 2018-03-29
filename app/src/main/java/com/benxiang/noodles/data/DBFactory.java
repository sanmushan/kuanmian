package com.benxiang.noodles.data;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;

/**
 * Created by 刘圣如 on 2017/8/24.
 */

public class DBFactory {
    public static LiteOrm mDBNoodle;
    public static void initNoodle(DataBaseConfig config){
        if (mDBNoodle == null) {
            synchronized (DBFactory.class) {
                if (mDBNoodle == null) {
                    mDBNoodle = LiteOrm.newCascadeInstance(config);
                }
            }
        }
    }

    public static LiteOrm getmDBNoodle(){
        if (mDBNoodle == null) throw new NullPointerException("The Noodles DB never call init()");
            return mDBNoodle;
    }
}
