package com.benxiang.noodles.model.remote;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 刘圣如 on 2017/9/29.
 */

public class RecipeModle {


    @SerializedName("ResultMsg")
    public String ResultMsg;
    @SerializedName("data")
    public List<RecipeData> recipeData;

    public class RecipeData{

        @SerializedName("id")
        public int id;
        @SerializedName("r_water_time")
        public int r_water_time;
        @SerializedName("r_fat_time")
        public int r_fat_time;
        @SerializedName("r_brine_time")
        public int r_brine_time;
        @SerializedName("r_vinegar_time")
        public int r_vinegar_time;
        @SerializedName("r_warm_time")
        public int r_warm_time;
        @SerializedName("r_warm_size")
        public int r_warm_size;
        @SerializedName("n_water_time")
        public int n_water_time;
        @SerializedName("n_fat_time")
        public int n_fat_time;
        @SerializedName("n_brine_time")
        public int n_brine_time;
        @SerializedName("n_vinegar_time")
        public int n_vinegar_time;
        @SerializedName("n_warm_time")
        public int n_warm_time;
        @SerializedName("n_warm_size")
        public int n_warm_size;
        @SerializedName("name")
        public String name;
        @SerializedName("mechanical_num")
        public String mechanical_num;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("RecipeData{");
            sb.append("id=").append(id);
            sb.append(", r_water_time=").append(r_water_time);
            sb.append(", r_fat_time=").append(r_fat_time);
            sb.append(", r_brine_time=").append(r_brine_time);
            sb.append(", r_vinegar_time=").append(r_vinegar_time);
            sb.append(", r_warm_time=").append(r_warm_time);
            sb.append(", r_warm_size=").append(r_warm_size);
            sb.append(", n_water_time=").append(n_water_time);
            sb.append(", n_fat_time=").append(n_fat_time);
            sb.append(", n_brine_time=").append(n_brine_time);
            sb.append(", n_vinegar_time=").append(n_vinegar_time);
            sb.append(", n_warm_time=").append(n_warm_time);
            sb.append(", n_warm_size=").append(n_warm_size);
            sb.append(", name='").append(name).append('\'');
            sb.append(", mechanical_num='").append(mechanical_num).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
    @SerializedName("Result")
    public int Result;
}
