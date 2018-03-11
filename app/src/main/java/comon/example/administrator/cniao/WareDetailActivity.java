package comon.example.administrator.cniao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mob.MobSDK;

import java.io.Serializable;

import cn.sharesdk.onekeyshare.OnekeyShare;
import comon.example.administrator.cniao.bean.Wares;
import comon.example.administrator.cniao.utils.CartProvider;
import comon.example.administrator.cniao.utils.ToastUtils;
import comon.example.administrator.cniao.widget.CNiaoToolBar;
import dmax.dialog.SpotsDialog;

/**
 * Created by Administrator on 2018/2/18.
 */

public class WareDetailActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.webView)
    private WebView mWebView;

    @ViewInject(R.id.toolbar)
    private CNiaoToolBar mToolBar;

    private Wares mWare;

    private WebAppInterface mAppInterfce;//定义一个全局变量mAppInterfce

    private CartProvider cartProvider;//添加到购物车的变量

    private SpotsDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_detail);
        ViewUtils.inject(this);

        Serializable serializable = getIntent().getSerializableExtra(Contants.WARE);
        //如果数据为空的话就退出，不然下面的操作全部白费了
        if(serializable == null )
            this.finish();

        mDialog = new SpotsDialog(this,"loading...");
        mDialog.show();

        mWare = (Wares) serializable;//强转
        cartProvider= new CartProvider(this);

        initToolBar();
        initWebView();


    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebView(){

        //对setting不熟悉的话可以去查查API文档
        WebSettings settings = mWebView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);//这个默认是true//如果是true的话就是阻塞，加载不出图片
        settings.setAppCacheEnabled(true);//允许有缓存

        //通过WebView来加载地址
        mWebView.loadUrl(Contants.API.WARES_DETAIL);

        //要和前端协商名字和方法,通过文档，协商，合作开发
        mAppInterfce = new WebAppInterface(this);
        mWebView.addJavascriptInterface(mAppInterfce,"appInterface");
        mWebView.setWebViewClient(new WC());

    }

    private void initToolBar() {

        mToolBar.setNavigationOnClickListener(this);
        mToolBar.setRightButtonText("分享");

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @SuppressLint("SdCardPath")
    private void showShare() {
        //初始化ShareSDK
        MobSDK.init(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(getString(R.string.share));

        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://www.cinao5.com");

        // text是分享文本，所有平台都需要这个字段//这里我设置为商品的名字了
        oks.setText(mWare.getName());

        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数

        //如果本地和网络图片同时存在，会以本地的图片覆盖网络的图片
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImageUrl(mWare.getImgUrl());//用网络的图片

        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://www.cniao5.com");
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment(mWare.getName());

        // site是分享此内容的网站名称，仅在qq空间使用
        oks.setSite(getString(R.string.app_name));

        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.cniao5.com");

        // 启动分享GUI
        oks.show(this);
    }

    @Override
    public void onClick(View v) {

        this.finish();
    }

    //这里的WC纯粹为一个类名，并没有什么实际意义
    class WC extends WebViewClient{


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if(mDialog != null && mDialog.isShowing())
                mDialog.dismiss();

            mAppInterfce.showDetail();
        }
    }

    //通信接口
    class WebAppInterface{

        private Context mContext;
        //context是没办法创造的，只有运行时才会产生上下文//这句话不理解
        public WebAppInterface(Context context){

            mContext = context;
        }

        //javascript来调用的，所以加上@JavascriptInterface
        @JavascriptInterface
        public void showDetail(){

            //要调用主线程的代码的话，必须放在主线程中
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mWebView.loadUrl("javascript:showDetail("+mWare.getId()+")");
                }
            });
        }

        //涉及到底层的交互，这是一个最简单的交互
        @JavascriptInterface
        public void buy(long id){

            cartProvider.put(mWare);
            ToastUtils.show(mContext,"已添加到购物车");//提示已经添加到购物车中了
        }

        @JavascriptInterface
        public void addFavorites(long id){


        }
    }


}

