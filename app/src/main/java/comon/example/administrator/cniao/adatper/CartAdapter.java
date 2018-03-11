package comon.example.administrator.cniao.adatper;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Iterator;
import java.util.List;

import comon.example.administrator.cniao.NumberAddSubView;
import comon.example.administrator.cniao.R;
import comon.example.administrator.cniao.bean.ShoppingCart;
import comon.example.administrator.cniao.utils.CartProvider;


/**
 * Created by <a href="http://www.cniao5.com">菜鸟窝</a>
 * 一个专业的Android开发在线教育平台
 */
public class CartAdapter extends SimpleAdapter<ShoppingCart>  implements BaseAdapter.OnItemClickListener{


    public static final String TAG="CartAdapter";


    private CheckBox checkBox;
    private TextView textView;

    private CartProvider cartProvider;


    public CartAdapter(Context context, List<ShoppingCart> datas, final CheckBox checkBox, TextView tv) {
        super(context, R.layout.template_cart, datas);

        this.checkBox = checkBox;
        this.textView = tv;

        checkBox.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                checkAll_None(checkBox.isChecked());
                showTotalPrice();
            }
        });

        cartProvider = new CartProvider(context);

        setOnItemClickListener(this);

        showTotalPrice();

    }


    @Override
    protected void convert(BaseViewHolder viewHoder, final ShoppingCart item) {

        viewHoder.getTextView(R.id.text_title).setText(item.getName());
        viewHoder.getTextView(R.id.text_price).setText("￥"+item.getPrice());
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHoder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));

        CheckBox checkBox = (CheckBox) viewHoder.getView(R.id.checkbox);
        checkBox.setChecked(item.isChecked());



        //给购买数量值进行初始化
        NumberAddSubView numberAddSubView =(NumberAddSubView) viewHoder.getView(R.id.num_control);
        numberAddSubView.setValue(item.getCount());

        numberAddSubView.setmOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {

                item.setCount(value);
                cartProvider.update(item);
                showTotalPrice();
            }

            @Override
            public void onButtonSubClick(View view, int value) {

                item.setCount(value);
                cartProvider.update(item);
                showTotalPrice();
            }
        });


    }

    //计算总价格的方法
    private float getTotalPrice(){

        float sum = 0;
        if( !isNull())
            return sum;

        for (ShoppingCart cart:
                datas){
            if(cart.isChecked())
                sum += cart.getCount()*cart.getPrice();
        }

        return sum;
    }

    public void showTotalPrice(){
        float total = getTotalPrice();

        textView.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>" + total +"</span>"), TextView.BufferType.SPANNABLE);
    }

    private boolean isNull(){

        return (datas != null && datas.size()>0);
    }

    //设置购物车的选择框的选择事件监听
    @Override
    public void onItemClick(View view, int position) {

        ShoppingCart cart = getItem(position);
        cart.setIsChecked(!cart.isChecked());
        notifyItemChanged(position);

        checkListen();
        showTotalPrice();
    }

    //设置购物车商品全选的监听事件
    private void checkListen(){

        int count = 0;
        int checkNum = 0;
        if(datas != null){
            count = datas.size();

            for (ShoppingCart cart : datas){
                if(!cart.isChecked()){
                    checkBox.setChecked(false);
                    break;
                }
                else{
                    checkNum = checkNum+1;
                }
            }

            if(count == checkNum){
                checkBox.setChecked(true);
            }
        }
    }

    public void checkAll_None(boolean isChecked){

        if(!isNull())
            return;

        int i = 0;
        for (ShoppingCart cart :datas){
            cart.setIsChecked(isChecked);
            notifyItemChanged(i);
            i++;
        }
    }

    public void delCart(){

        if(!isNull())
            return;


        //这里需要比较好的java基础才能理解//循环的过程中remove会改变了长度，所以会出错
//        for (ShoppingCart cart : datas){
//
//            if(cart.isChecked()){
//
//                int position = datas.indexOf(cart);
//                cartProvider.delete(cart);
//                datas.remove(cart);
//                notifyItemRemoved(position);//通知更改
//            }
//        }


        //下面这样写才不会出错/不会报错
        for(Iterator iterator = datas.iterator();iterator.hasNext();){

            ShoppingCart cart = (ShoppingCart) iterator.next();//获取cart
            if(cart.isChecked()){
                int position = datas.indexOf(cart);
                cartProvider.delete(cart);
                iterator.remove();
                notifyItemRemoved(position);//通知更改
            }
        }

    }


    public void setTextView(TextView textView) { this.textView = textView;}

    public void setCheckBox(CheckBox ck){
        this.checkBox = ck;

        checkBox.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                checkAll_None(checkBox.isChecked());
                showTotalPrice();
            }
        });
    }


}