package comon.example.administrator.cniao;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/1/11.
 */

public class NumberAddSubView extends LinearLayout implements View.OnClickListener{


    private LayoutInflater mInflater;

    //初始化三个控件
    private Button mBtnAdd;
    private Button mBtnSub;
    private TextView mTextNum;

    private int value;//定义一个values值去限制商品数量的范围大小
    private int minValue;//定义最小值
    private int maxValue;//定义最大值

    private OnButtonClickListener mOnButtonClickListener;//定义一下变量


    //三个构造方法
    public NumberAddSubView(Context context) {
        this(context,null);
    }

    public NumberAddSubView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    @SuppressLint("RestrictedApi")
    public NumberAddSubView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化mInflater
        mInflater = LayoutInflater.from(context);

        initView();//进行调用,控件就可以显示了。

        if(attrs != null){

            //用TintTypeArray来读取我们的自定义属性
            @SuppressLint("RestrictedApi") TintTypedArray a = TintTypedArray.obtainStyledAttributes(context,attrs,R.styleable.NumberAddSubView, defStyleAttr,0);

            a.getInt(R.styleable.NumberAddSubView_value,0);

            int val = a.getInt(R.styleable.NumberAddSubView_value,0);
            setValue(val);

            int minVal =  a.getInt(R.styleable.NumberAddSubView_minValue,0);
            setMinValue(minVal);

            int maxVal =  a.getInt(R.styleable.NumberAddSubView_maxValue,0);
            setMaxValue(maxVal);

            Drawable drawableBtnAdd =a.getDrawable(R.styleable.NumberAddSubView_btnAddBackground);
            Drawable drawableBtnSub =a.getDrawable(R.styleable.NumberAddSubView_btnSubBackground);
            Drawable drawableTextView =a.getDrawable(R.styleable.NumberAddSubView_textViewBackground);

            setButtonAddBackgroud(drawableBtnAdd);
            setButtonSubBackgroud(drawableBtnSub);
            setTexViewtBackground(drawableTextView);

            a.recycle();//回收
        }


    }


    //initView方法读取布局
    private void initView(){
        //传入布局的Id
        View view = mInflater.inflate(R.layout.wieght_number_add_sub,this,true);

        //读取控件地址
        mBtnAdd = view.findViewById(R.id.btn_add);
        mBtnSub = view.findViewById(R.id.btn_sub);
        mTextNum = view.findViewById(R.id.txt_num);

        mBtnAdd.setOnClickListener(this);
        mBtnSub.setOnClickListener(this);
    }


    public int getValue() {

        // 取出TextView的数值
        String val = mTextNum.getText().toString();//getText()方法是sdk封装好的了，只要定义mTextNum为TextView

        //判断TextView的数值是否为空
        if(val != null && !"".equals(val))
             this.value = Integer.parseInt(val);

        return value;
    }

    @SuppressLint("SetTextI18n")
    public void setValue(int value) {
        mTextNum.setText(value+"");//设置初始值
        this.value = value;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    //设置它的监听器
    public void setmOnButtonClickListener(OnButtonClickListener onButtonClickListener){
        this.mOnButtonClickListener = onButtonClickListener;
    }

    @Override
    public void onClick(View v) {


        //判断一下是加按钮还是减按钮
        if(v.getId() == R.id.btn_add){

            numAdd();

            //如果mOnButtonClickListener为空则没有必要监听了
            // 放在里面的原因是不管有没有监听器,都要对数字进行加减，这是必须的
            if (mOnButtonClickListener != null){
                mOnButtonClickListener.onButtonAddClick(v,value);
            }

        }
        else if(v.getId() == R.id.btn_sub){

            numSub();

            //如果mOnButtonClickListener为空则没有必要监听了
            if (mOnButtonClickListener != null){
                mOnButtonClickListener.onButtonSubClick(v,value);
            }

        }
    }

    //数字的加方法
    private void numAdd(){

        if(value < maxValue)
            value = value+1;

        //+""的作用是将value强制转换为字符串，因为value是数值，没有转换的会认为value是个ID,从而抛一个找不到的异常
        mTextNum.setText(value+"");//android的Sdk本身封装好的方法，只要mTextNum定义为TextView变量就可以调用，该方法用于设置TextView的值

    }

    private void numSub(){

        if(value>minValue)
            value=value-1;
        mTextNum.setText(value+"");
    }


    public void setTexViewtBackground(Drawable drawable){

        mTextNum.setBackgroundDrawable(drawable);

    }


    public void setTextViewBackground(int drawableId){

        setTexViewtBackground(getResources().getDrawable(drawableId));

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setButtonAddBackgroud(Drawable backgroud){
        this.mBtnAdd.setBackground(backgroud);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setButtonSubBackgroud(Drawable backgroud){
        this.mBtnSub.setBackground(backgroud);
    }


    //加减事件的监听器//进行操控数据
    public interface OnButtonClickListener{

        void onButtonAddClick(View view,int value);
        void onButtonSubClick(View view,int value);
    }
}
