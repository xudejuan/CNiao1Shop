package comon.example.administrator.cniao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.mob.MobSDK;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.UserInterruptException;
import cn.smssdk.gui.RegisterPage;
import cn.smssdk.utils.SMSLog;
import comon.example.administrator.cniao.utils.ToastUtils;
import comon.example.administrator.cniao.widget.CNiaoToolBar;
import comon.example.administrator.cniao.widget.ClearEditText;
import dmax.dialog.SpotsDialog;

public class RegActivity extends AppCompatActivity {

    private static final String TAG = "RegActivity";

    //默认使用中国区号//做人脾气要温和，无论遇到什么事都要冷静，同时思考一下，但不要钻牛角尖，不会就不会，没什么好烦躁的，只要在学习时间学习就可以了
    private static final String DEFAULT_COUNTRY_ID = "42";

    @ViewInject(R.id.toolbar)
    private CNiaoToolBar mToolBar;

    @ViewInject(R.id.txtCountry)
    private TextView mTxtCountry;

    @ViewInject(R.id.txtCountryCode)
    private TextView mTxtCountryCode;

    @ViewInject(R.id.edittxt_phone)
    private ClearEditText mEtxtPhone;


    @ViewInject(R.id.edittxt_pwd)
    private ClearEditText mEtxtPwd;

    private SpotsDialog dialog;

    //把eventHanlder定义成一个全局变量
    private SMSEvenHanlder evenHanlder;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        initToolBar();

        MobSDK.init(this);



        evenHanlder = new SMSEvenHanlder();
        SMSSDK.registerEventHandler(evenHanlder);


        String[] country = this.getCurrentCountry(DEFAULT_COUNTRY_ID);
        if(country != null) {


            mTxtCountryCode.setText("+"+country[1]);
            mTxtCountry.setText(country[0]);

        }

        SMSSDK.getSupportedCountries();
    }

    class SMSEvenHanlder extends EventHandler{


        @Override
        public void afterEvent(final int event, final int result, final Object data) {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {


                            onCountryListGot((ArrayList<HashMap<String,Object>>) data);
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //请求验证码后，跳转到验证码填写页面

                            afterVerificationCodeRequested((Boolean) data);

                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {

                        }
                    } else {

                        if (event == 2 && data != null && data instanceof UserInterruptException) {
                            return;
                        }

                        int status = 0;

                        //根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");
                            status = object.optInt("status");
                            if (!TextUtils.isEmpty(des)) {
                                ToastUtils.show(RegActivity.this,des);
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }



                    }
                }
            });

        }
    }


    private void initToolBar(){

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                SMSSDK.getVerificationCode();

                getCode();

            }
        });
    }

    private void getCode(){

        String phone = mEtxtPhone.getText().toString().trim().replaceAll("\\s*","");
        String code = mTxtCountryCode.getText().toString().trim();
        String pwd = mEtxtPwd.getText().toString().trim();

        checkPhoneNum(phone,code);

        //not 86  +86
        SMSSDK.getVerificationCode(code,phone);
    }

    private void checkPhoneNum(String phone,String code){

        if (code.startsWith("+")){
            code = code.substring(1);
        }

        if(TextUtils.isEmpty(phone)){
            ToastUtils.show(this,"请输入手机号码");
            return;
        }

        if (code == "86"){
            if(phone.length() != 11){
                ToastUtils.show(this,"手机号码长度不对");
                return;
            }
        }

        //不就是失恋吗，无论遇到什么都不要难过，烦躁，都要敲代码，唯有代码是真爱。
        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);

        if (!m.matches()){
            ToastUtils.show(this,"您输入的手机号码格式不正确");
            return;
        }
    }


    private void onCountryListGot(ArrayList<HashMap<String, Object>> countries){

        //解析国家列表
        for(HashMap<String, Object> country : countries){
            String code = (String) country.get("zone");
            String rule = (String) country.get("rule");
            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)){
                continue;
            }

            //打印代码用Log.d(TAG,"需要打印的字符串")
            Log.d(TAG,"code="+code+"rule="+rule);
        }
    }


    //请求验证码后，跳转到验证码填写页面
    private void afterVerificationCodeRequested(boolean smart) {

        //第一个参数是手机号码，第二个参数是国家代码，第三个参数是密码
        String phone = mEtxtPhone.getText().toString().trim().replaceAll("//s*","");
        String code = mTxtCountryCode.getText().toString().trim();
        String pwd = mEtxtPwd.getText().toString().trim();

        if(code.startsWith("+")){
            code = code.substring(1);
        }

        Intent intent = new Intent(this,RegSecondActivity.class);
        intent.putExtra("phone",phone);
        intent.putExtra("pwd",pwd);
        intent.putExtra("countryCode",code);

        startActivity(intent);
    }

    public void sendCode() {

        //打开注册页面
        RegisterPage page = new RegisterPage();
        page.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {

// 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 处理成功的结果
                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country"); // 国家代码，如“86”
                    String phone = (String) phoneMap.get("phone"); // 手机号码，如“13800138000”

//  提交用户信息
                   Log.d(TAG,country);
                    // TODO 利用国家代码和手机号码进行后续的操作
                } else{
                    // TODO 处理错误的结果
                }
            }
        });
        page.show(this);
    }




    private String[] getCurrentCountry(String defaultCountryId) {
        String mcc = this.getMCC();
        String[] country = null;
        if(!TextUtils.isEmpty(mcc)) {
            country = SMSSDK.getCountryByMCC(mcc);
        }

        if(country == null) {
            SMSLog.getInstance().d("no country found by MCC: " + mcc, new Object[0]);
            country = SMSSDK.getCountry("42");
        }

        return country;
    }


    private String getMCC() {
        @SuppressLint("WrongConstant") TelephonyManager tm = (TelephonyManager)getSystemService("phone");
        String networkOperator = tm.getNetworkOperator();
        return !TextUtils.isEmpty(networkOperator)?networkOperator:tm.getSimOperator();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        SMSSDK.unregisterEventHandler(evenHanlder);
    }


}
