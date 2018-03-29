package com.benxiang.noodles.data.noodle;

/**
 * Created by 刘圣如 on 2017/10/16.
 */

public class NoodleEventData {

    public int noodle_no;
    public String noodle_state;
    public String noodle_name;
//    public String orderID;
    //多单多碗时用于判断上一碗有没有被选中
    public boolean isSelected = false;
    //抽中的奖品：如：鸡蛋,榨菜,谢谢惠顾
    public String spoilName;



        @Override
    public String toString() {
        return "noodle_no=" + noodle_no +
                "/n noodle_state=" + noodle_state +
                "/n noodle_name=" + noodle_name +
                "/n isSelected" + isSelected;
    }
}
