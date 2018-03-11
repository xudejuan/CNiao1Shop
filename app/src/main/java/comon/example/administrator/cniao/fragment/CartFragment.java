package comon.example.administrator.cniao.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

import comon.example.administrator.cniao.MainActivity;
import comon.example.administrator.cniao.NewOrderActivity;
import comon.example.administrator.cniao.R;
import comon.example.administrator.cniao.adatper.CartAdapter;
import comon.example.administrator.cniao.adatper.DividerItemDecoration;
import comon.example.administrator.cniao.bean.ShoppingCart;
import comon.example.administrator.cniao.http.OkHttpHelper;
import comon.example.administrator.cniao.utils.CartProvider;
import comon.example.administrator.cniao.widget.CnToolbar;




/**
 * Created by Administrator on 2017/12/17.
 */

public class CartFragment extends BaseFragment implements View.OnClickListener {


    //定义购物车编辑和完成两种状态
    public static final int ACTION_ENDIT = 1;
    public static final int  ACTION_CAMPLATE = 2;
    private static final String TAG = "CartFragment";

    //初始化控件
    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.checkbox_all)
    private CheckBox mCheckBox;

    @ViewInject(R.id.txt_total)
    private TextView mTextTotal;

    @ViewInject(R.id.btn_order)
    private Button mBtnOrder;

    @ViewInject(R.id.btn_del)
    private Button mBtnDel;

    private CnToolbar mToolbar;


    //初始化
    private CartAdapter mAdapter;
    private CartProvider cartProvider;

    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        return view;
    }




    @Override
    public void init() {
        cartProvider = new CartProvider(getContext());

        changeToolbar();
        showData();
    }

    @OnClick(R.id.btn_del)
    public void delCart(View view){

        mAdapter.delCart();
    }

    @OnClick(R.id.btn_order)
    public void toOrder(View view){

        Intent intent = new Intent(getActivity(), NewOrderActivity.class);
        startActivity(intent,true);
    }

    //展示数据的方法
    private void showData(){
        List<ShoppingCart> carts = cartProvider.getAll();

        mAdapter = new CartAdapter(getContext(),carts,mCheckBox,mTextTotal);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
    }


    //定义一个用于刷新数据的方法
    public void refData(){

        mAdapter.clear();//对CartAdapter进行clear
        List<ShoppingCart> carts = cartProvider.getAll();//重新对这个数据进行读取
        mAdapter.addData(carts);//对数据又重新sdd进来
        mAdapter.showTotalPrice();

    }

    @Override
    public void onAttach(Context context) {

        //判断一下context是否等于MainActivity
        super.onAttach(context);
        if (context instanceof MainActivity) {

            MainActivity activity = (MainActivity) context;
            mToolbar = (CnToolbar)  activity.findViewById(R.id.toolbar);

            changeToolbar();

        }
    }


    public void changeToolbar(){

        mToolbar.hideSearchView();
        mToolbar.showTitleView();
        mToolbar.setTitle(R.string.cart);
        mToolbar.getRightButton().setVisibility(View.VISIBLE);
        mToolbar.setRightButtonText("编辑");



        mToolbar.getRightButton().setOnClickListener(this);

        mToolbar.getRightButton().setTag(ACTION_ENDIT);

    }

    //点击编辑按钮之后触发的删除编辑
    private void showDelControl(){
        mToolbar.getRightButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);//隐藏掉合计价格
        mBtnOrder.setVisibility(View.GONE);//隐藏掉总计
        mBtnDel.setVisibility(View.VISIBLE);//显示删除按钮
        mToolbar.getRightButton().setTag(ACTION_CAMPLATE);//设置成"完成"状态

        //在删除状态下设置所有的产品都没有选中
        // 从产品的角度来考虑问题是希望客户全部购买商品的
        // 只有站在产品的角度考虑问题，技术才会进步
        mAdapter.checkAll_None(false);
        mCheckBox.setChecked(false);

    }

    private void hideDelControl(){


        mTextTotal.setVisibility(View.VISIBLE);//显示掉合计价格
        mBtnOrder.setVisibility(View.VISIBLE);//显示掉总计价格

        mBtnDel.setVisibility(View.GONE);//隐藏删除按钮
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setTag(ACTION_ENDIT);//设置成"完成"状态

        mAdapter.checkAll_None(true);
        mAdapter.showTotalPrice();

        mCheckBox.setChecked(true);
    }

    //点击编辑按钮触发的监听事件
    @Override
    public void onClick(View v) {

        int action = (int) v.getTag();
        if(ACTION_ENDIT == action){

            showDelControl();
        }
        else if(ACTION_CAMPLATE == action){

            hideDelControl();
        }

    }
}
