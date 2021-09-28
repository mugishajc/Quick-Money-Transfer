package auca.didine.quickmoneytransfer.interfaces;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RESTApiInterface {

    // create user
    @POST("register")
    Call<ResponseBody> createUser(@Body Map<String,String> obj);

    // login user
    @POST("login")
    Call<ResponseBody> loginUser(@Body Map<String,String> obj);

    // get client balance
    @POST("getClientBalance")
    Call<ResponseBody> getclientbalance(@Body Map<String,String> obj);

    // send money
    @POST("clientTransferMoney")
    Call<ResponseBody> transfermoney(@Body Map<String,String> obj);


    // get transaction
    @POST("getClientTransaction")
    Call<ResponseBody> gettransaction(@Body Map<String,String> obj);

    // withdraw money
    @POST("clientWithdrawMoney")
    Call<ResponseBody> withdrawmoney(@Body Map<String,String> obj);

    // send otp
    @POST("checkuser")
    Call<ResponseBody> sendotp(@Body Map<String,String> obj);


    // check otp
    @POST("checkotp")
    Call<ResponseBody> checkotp(@Body Map<String,String> obj);




}
