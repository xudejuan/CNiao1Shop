
package comon.example.administrator.cniao.msg;


public class LoginRespMsg<T> extends BaseRespMsg {


    private String token;

    //泛型//赶紧把Java学习的尾巴结束
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
