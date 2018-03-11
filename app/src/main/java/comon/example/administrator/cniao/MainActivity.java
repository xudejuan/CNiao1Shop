package comon.example.administrator.cniao;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import comon.example.administrator.cniao.bean.Tab;
import comon.example.administrator.cniao.fragment.CartFragment;
import comon.example.administrator.cniao.fragment.CategoryFragment;
import comon.example.administrator.cniao.fragment.HomeFragment;
import comon.example.administrator.cniao.fragment.HotFragment;
import comon.example.administrator.cniao.fragment.MineFragment;
import comon.example.administrator.cniao.widget.CnToolbar;
import comon.example.administrator.cniao.widget.FragmentTabHost;

public class MainActivity extends BaseActivity {

    private LayoutInflater mInflater;

    private FragmentTabHost mTabhost;

    private CnToolbar mToolbar;

    CartFragment cartFragment;

    MineFragment mineFragment;

    private List<Tab> mTabs = new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Fresco.initialize(this);
        setContentView(R.layout.activity_main);

        initToolBar();
        initTab();//把底部导航栏加载进来

    }

    private void initToolBar() {

        mToolbar = (CnToolbar) findViewById(R.id.toolbar);
    }

    //底部导航栏的代码
    private void initTab() {

        Tab tab_home = new Tab(R.string.home, R.drawable.selector_icon_home, HomeFragment.class);
        Tab tab_hot = new Tab(R.string.hot, R.drawable.selector_icon_hot, HotFragment.class);
        Tab tab_category = new Tab(R.string.catagory, R.drawable.selector_icon_category, CategoryFragment.class);
        Tab tab_cart = new Tab(R.string.cart, R.drawable.selector_icon_cart, CartFragment.class);
        Tab tab_mine = new Tab(R.string.mine, R.drawable.selector_icon_mine, MineFragment.class);

        mTabs.add(tab_home);
        mTabs.add(tab_hot);
        mTabs.add(tab_category);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);

        mInflater = LayoutInflater.from(this);
        mTabhost =(FragmentTabHost) this.findViewById(android.R.id.tabhost);
        mTabhost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);

        for (Tab tab :mTabs){

            TabHost.TabSpec tabSpec  = mTabhost.newTabSpec(getString(tab.getTitle()));

            tabSpec.setIndicator(buildIndicator(tab));

            mTabhost.addTab(tabSpec,tab.getFragment(),null);

        }


        mTabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                if(tabId == getString(R.string.cart) ){

                    refData();

                }
                else if(tabId == getString(R.string.mine)){

                    mToolbar.hideSearchView();
                    mToolbar.hideTitleView();
                    mToolbar.getRightButton().setVisibility(View.GONE);//把按钮也隐藏起来

                }
                else{

                    mToolbar.showSearchView();
                    mToolbar.hideTitleView();
                    mToolbar.getRightButton().setVisibility(View.GONE);//把按钮也隐藏起来
                }//为了设置购物车的Toolbar时候不影响其他fragment的Toolbar//把Toolbar还原回来



            }


        });
        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabhost.setCurrentTab(0);//默认选择第一个

    }

    private void refData(){
        if(cartFragment == null) {
            //调用fragment里面的方法，首先要拿到fragment的对象
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
            if (fragment != null) {

                cartFragment = (CartFragment) fragment;
                cartFragment.refData();//第一次初始化的时候也要刷新数据
                cartFragment.changeToolbar();
            }
        }

        else{
                cartFragment.refData();
                cartFragment.changeToolbar();
            }

    }

    private View buildIndicator(Tab tab){
        View view = mInflater.inflate(R.layout.tab_indicator,null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text =view.findViewById(R.id.txt_indicator);

        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());

        return view;

    }


}
