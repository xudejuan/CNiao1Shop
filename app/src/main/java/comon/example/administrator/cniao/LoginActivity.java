package comon.example.administrator.cniao;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.Map;

import comon.example.administrator.cniao.bean.User;
import comon.example.administrator.cniao.http.OkHttpHelper;
import comon.example.administrator.cniao.http.SpotsCallBack;
import comon.example.administrator.cniao.msg.LoginRespMsg;
import comon.example.administrator.cniao.utils.DESUtil;
import comon.example.administrator.cniao.utils.ToastUtils;
import comon.example.administrator.cniao.widget.CNiaoToolBar;
import comon.example.administrator.cniao.widget.ClearEditText;


public class LoginActivity extends AppCompatActivity {


    @ViewInject(R.id.toolbar)
    private CNiaoToolBar mToolBar;
    @ViewInject(R.id.etxt_phone)
    private ClearEditText mEtxtPhone;
    @ViewInject(R.id.etxt_pwd)
    private ClearEditText mEtxtPwd;



    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);

        initToolBar();
    }


    private void initToolBar(){


        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginActivity.this.finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    //点击登录按钮的时候
    @OnClick(R.id.btn_login)
    public void login(View view){


        String phone = mEtxtPhone.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtils.show(this, "请输入手机号码");
            return;
        }

        String pwd = mEtxtPwd.getText().toString().trim();
        if(TextUtils.isEmpty(pwd)){
            ToastUtils.show(this,"请输入密码");
            return;
        }







        Map<String,String> params = new HashMap<>(2);
        params.put("phone",phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY,pwd));

        okHttpHelper.post(Contants.API.LOGIN, params, new SpotsCallBack<LoginRespMsg<User>>(this) {


            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {


               CniaoApplication application =  CniaoApplication.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());


                if(application.getIntent() == null){
                    setResult(RESULT_OK);
                    finish();
                }else{

                   application.jumpToTargetActivity(LoginActivity.this);
                    finish();
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });





    }



}
