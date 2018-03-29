package com.benxiang.noodles.moudle.luckydraw;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.benxiang.noodles.BuildConfig;
import com.benxiang.noodles.R;
import com.benxiang.noodles.base.BaseActivity;
import com.benxiang.noodles.contants.Constants;
import com.benxiang.noodles.contants.MethodConstants;
import com.benxiang.noodles.model.NoodleTradeModel;
import com.benxiang.noodles.model.addMeal.AddOrderItemModel;
import com.benxiang.noodles.model.addMeal.AddOrderItemParam;
import com.benxiang.noodles.model.addMeal.AddOrderItemPresenter;
import com.benxiang.noodles.model.addMeal.AddOrderItemView;
import com.benxiang.noodles.moudle.makenoodle.NoodlesMakeActivity;
import com.benxiang.noodles.utils.JsonHelper;
import com.benxiang.noodles.utils.NoodleDataUtil;
import com.benxiang.noodles.utils.NoodleTradeFieldUtil;
import com.benxiang.noodles.utils.ParamObtainUtil;
import com.benxiang.noodles.utils.PreferenceUtil;
import com.benxiang.noodles.utils.ShotrNoUtil;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;

import timber.log.Timber;

/**
 * 抽奖界面
 * Created by 刘圣如 on 2017/11/25.
 */

public class LuckyDrawActivity extends BaseActivity implements AddOrderItemView{

    private NoodleTradeModel mNoodleTradeModel;
    private BridgeWebView bridgeWebView;
    AddOrderItemPresenter mAddOrderItemPresenter;

    @Override
    public int getContentViewID() {
        return R.layout.activity_lucky_draw;
    }

    @Override
    protected void afterContentViewSet() {
        mNoodleTradeModel = getIntent().getParcelableExtra("noodle");
        if (mNoodleTradeModel != null){
            String json = JsonHelper.getGson().toJson(mNoodleTradeModel);
            Timber.e("jsbridge接收的数据:"+json);
        }
        setupWebView();
        startLuckyDraw();
        stopLuckyDraw();
    }

    @Override
    protected void onStart() {
        super.onStart();
        int size = mNoodleTradeModel.listModles.size();
        if (BuildConfig.DEBUG){
            setEnableCountdown(true,60+30*(size-1));
        }else {
            setEnableCountdown(true,Constants.COUNT_DOWN_TIME+30*(size-1));
        }
        startDownTime();
        speakText("亲，试试手气吧");
    }

    //点击不跳转到下一个页面
    @Override
    protected void jumpToNextPager() {
        Timber.e("没有执行到");
    }

    @Override
    protected void countdownOver() {
        boolean isException = PreferenceUtil.config().getBooleanValue(Constants.MACHINE_EXCEPTION);
        if (isException){
            return;
        }

        dbTest(ShotrNoUtil.getShotrNo(true), mNoodleTradeModel);

        startRefundment(LuckyDrawActivity.this, NoodlesMakeActivity.class, mNoodleTradeModel, mNoodleTradeModel.pay_type);
        finish();
    }

    private void startLuckyDraw() {
        bridgeWebView.registerHandler(Constants.BEGIN_LOTTERY, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Timber.e("开始抽奖："+data);
                ParamObtainUtil.getLuckDrawParams(function,getMainHandler(),mNoodleTradeModel);
            }
        });
    }

    private void stopLuckyDraw() {
        bridgeWebView.registerHandler(Constants.COLSE_LOTTERY, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                function.onCallBack(null);
                NoodleTradeFieldUtil.setCloseLotteryModels(mNoodleTradeModel,data);

//                if (BuildConfig.DEBUG) {
                    dbTest(ShotrNoUtil.getShotrNo(true), mNoodleTradeModel);
//                    getnumberList();
//                }
                ToAddOrderItem();
//
                getMainHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startRefundment(LuckyDrawActivity.this, NoodlesMakeActivity.class, mNoodleTradeModel, mNoodleTradeModel.pay_type);
                        finish();
                    }
                },3000);
            }
        });
    }

    /**
     * 抽奖完成后加菜
     */
    private void ToAddOrderItem() {
        AddOrderItemParam param = ParamObtainUtil.getAddOrderItemParam(mNoodleTradeModel);
        if (param.Order.orderDetails.size()<=0){
            Timber.e("抽到的都是谢谢惠顾，不用加菜");
            return;
        }
        mAddOrderItemPresenter = new AddOrderItemPresenter();
        mAddOrderItemPresenter.attachView(this);
        Timber.e("加菜传的参数:"+JsonHelper.getGson().toJson(param));
        mAddOrderItemPresenter.addOrderItem(MethodConstants.ADDORDERITEM,JsonHelper.getGson().toJson(param));
    }

    /**
     * 把做面的数据写入到数据库中
     * @param shotrNo
     * @param mNoodleTradeModel
     */
    private void dbTest(int shotrNo, NoodleTradeModel mNoodleTradeModel) {
        NoodleDataUtil.getOrderToDB(shotrNo, mNoodleTradeModel);
    }

    private void setupWebView() {
        bridgeWebView = (BridgeWebView) findViewById(R.id.bridge_web_views);
        WebSettings settings = bridgeWebView.getSettings();
        //支持js
        settings.setJavaScriptEnabled(true);
        //允许js弹出窗口
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        bridgeWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        bridgeWebView.setDefaultHandler(new DefaultHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {

            }
        });

        bridgeWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b2 = new AlertDialog.Builder(LuckyDrawActivity.this)
//                        .setTitle("url:"+url)
                        .setMessage(message)
                        .setPositiveButton("ok",
                                new AlertDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        result.confirm();
                                        // MyWebView.this.finish();
                                    }
                                });

                b2.setCancelable(false);
                b2.create();
                b2.show();
                return true;
            }
        });

        bridgeWebView.setWebViewClient(new BridgeWebViewClient(bridgeWebView) {



            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Timber.e("接收错误数据:errorcode=" + errorCode + ",description=" + description + ",failingUrl:" + failingUrl);
                loadUrlClearCache("file:///android_asset/error.html");

            }

            @Override
            public void onLoadResource(WebView view, String url) {

                if (url.endsWith(".html")) {
                    Timber.e("加载的资源:" + url);
                }
                super.onLoadResource(view, url);
            }

        });
        //TODO LIN 这是抽奖模式（调试或正常）
        loadUrlClearCache(Constants.LOCKY_DRAW_URL);
        bridgeWebView.send("luck one");
        //每隔一秒去检测页面,没网络时加载错误页面
//        getMainHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getMainHandler().postDelayed(this, 2000);
//                if (!NetworkUtils.isConnected()) {
//                    loadUrlClearCache("file:///android_asset/error.html");
////                    mFailingUrl = startPage + "/index/index.html";
//                }
////                Timber.e("每秒执行一次");
//            }
//        }, 1000);
    }

    private void loadUrlClearCache(String url) {
        bridgeWebView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bridgeWebView.clearCache(true);
        if (mAddOrderItemPresenter != null){
            mAddOrderItemPresenter.detavh();
            mAddOrderItemPresenter = null;
        }
    }

    @Override
    public void addOrderItemSuccess(AddOrderItemModel addOrderItemModel) {

    }

}
