package auca.didine.quickmoneytransfer.singleton;


import auca.didine.quickmoneytransfer.interfaces.RESTApiInterface;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RESTApiClient {

    private static final String BASE_URL = "http://transfer.developerbab.xyz/api/client/";
    private static RESTApiClient apiClient;
    private static Retrofit retrofit;

    public RESTApiClient() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static synchronized RESTApiClient getInstance() {
        if (apiClient == null) {
            apiClient = new RESTApiClient();
        }
        return apiClient;
    }

    public RESTApiInterface getApi() {
        return retrofit.create(RESTApiInterface.class);
    }
}