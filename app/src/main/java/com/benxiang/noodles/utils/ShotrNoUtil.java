package com.benxiang.noodles.utils;

import com.benxiang.noodles.AppApplication;

/**
 * Created by 刘圣如 on 2017/10/16.
 */

public class ShotrNoUtil {

    public static int getShotrNo(boolean change) {
        int num = 0;
        if (change){
            num = AppApplication.getSortNo()+1;
        }else {
            num = AppApplication.getSortNo();
        }

        if (num > 99) {
            AppApplication.setSortNo(1);
        }
        else {
            AppApplication.setSortNo(num);
        }

        return AppApplication.getSortNo();
    }

}
