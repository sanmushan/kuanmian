package com.benxiang.noodles.moudle.makenoodle;

/**
 * Created by 刘圣如 on 2017/10/10.
 */

public class ExceptionParam {
    /**
     * {
     "mechanical_num": "R101",
     "abnormal_level": "1",
     "abnormal_type": "地球",
     "abnormal_detail": "地核爆炸",
     "time": "1124545544",
     "merchant_id": "1",
     "remark": "212"

     }
     */
    public String mechanical_num;
    public String abnormal_level;
    public String abnormal_type;
    public String abnormal_detail;
    public String time;
    public String merchant_id;
    public String remark;
    public String LIDCode;
//    public String shopCode;

    @Override
    public String toString() {
        return "机器编码="+mechanical_num+
                "\n异常级别="+abnormal_level+
                "\n异常类型="+abnormal_type+
                "\n异常内容="+abnormal_detail+
                "\n时间="+time+
                "\n所属商户ID="+merchant_id+
                "\n备注="+remark;
    }
}
