package comon.example.administrator.cniao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import comon.example.administrator.cniao.bean.User;

/**
 * Created by Administrator on 2018/2/21.
 */

public class BaseActivity extends AppCompatActivity {

    public void startActivity(Intent intent, boolean isNeedLogin){


        if(isNeedLogin){

            User user = CniaoApplication.getInstance().getUser();
            if(user !=null){
                super.startActivity(intent);//已经登录了的话就跳转到目标就可以了
            }
            else{

                CniaoApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                super.startActivity(loginIntent);

            }

        }
        else{
            super.startActivity(intent);
        }

    }


}
