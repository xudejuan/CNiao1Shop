package comon.example.administrator.cniao.utils;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import comon.example.administrator.cniao.bean.ShoppingCart;
import comon.example.administrator.cniao.bean.Wares;

/**
 * Created by Administrator on 2018/1/29.
 */

//购物车的数据存储器的类

public class CartProvider {

    public static final String CART_JSON="cart_json";

//    Android提供的更高效的工具类SparseArray
    private SparseArray<ShoppingCart> datas = null;


    private Context mContext;

//    构造方法
    public CartProvider(Context context){

        mContext = context;
        datas = new SparseArray<>(10);//先设置10条数据//默认初始化十条数据
        listToSparse();


    }


    //put、update、delete这三个方法都要同时操作手机内存空间里的数据和SparseArray里的数据,操作两份数据

//    添加数据
    public void put(ShoppingCart cart){

       ShoppingCart temp = datas.get(cart.getId().intValue());//一个临时的对象接收

        if(temp != null){
            temp.setCount(temp.getCount()+1);
        }
        else{
            temp = cart;
            temp.setCount(1);
        }

        datas.put(cart.getId().intValue(),temp);//存储数据

        commit();//存储数据//同步更新

    }

    //两个put方法叫做代码重构，写代码的过程中不可能面面俱到，只能通过代码重构来不断完善代码
    public void put(Wares wares){

        ShoppingCart cart = convertData(wares);
        put(cart);
    }

//    更新数据
    public void update(ShoppingCart cart){

        datas.put(cart.getId().intValue(),cart);//put中默认已经做了update
        commit();//存储数据//同步更新

    }

    public void delete(ShoppingCart cart){

        datas.delete(cart.getId().intValue());
        commit();//存储数据//同步更新

    }

    public List<ShoppingCart> getAll(){
        return getDataFromLocal();
    }

    //将数据进行保存到本地
    public void commit(){

        List<ShoppingCart> carts = sparseToList();
        PreferencesUtils.putString(mContext,CART_JSON,JSONUtil.toJSON(carts));

    }

    private List<ShoppingCart> sparseToList(){

        int size = datas.size();

        List<ShoppingCart> list = new ArrayList<>(size);
        for (int i=0; i<size;i++){
            list.add(datas.valueAt(i));
        }
        return list;

    }

    //把本地的数据放到sparse中
    private void listToSparse(){
        List<ShoppingCart> carts = getDataFromLocal();

        if(carts!=null && carts.size()>0){

            for(ShoppingCart cart:
                    carts){
                datas.put(cart.getId().intValue(),cart);//本地的数据已经放到这里来了
            }
        }
    }

    //从本地读取数据的方法
    public List<ShoppingCart> getDataFromLocal(){

        String json = PreferencesUtils.getString(mContext,CART_JSON);
        List<ShoppingCart> carts = null;
        if(json != null){
            carts = JSONUtil.fromJson(json,new TypeToken<List<ShoppingCart>>(){}.getType());
        }

        return carts;
    }

    public ShoppingCart convertData(Wares item){

        ShoppingCart cart = new ShoppingCart();

        cart.setId(item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());

        return cart;
    }


}
