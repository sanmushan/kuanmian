package com.benxiang.noodles.moudle.config;

import com.benxiang.noodles.base.BaseView;
import com.benxiang.noodles.model.remote.RecipeModle;

import java.util.List;

/**
 * Created by 刘圣如 on 2017/9/6.
 */

public interface SplashView extends BaseView {
    void getRecipeSuccess(List<RecipeModle.RecipeData> recipeData);
    void getRecipeFaile();
}
