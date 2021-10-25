package please.hire.me;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import please.hire.me.model.StoreModel;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface Retropow {

    String BASE_URL = "https://fakestoreapi.com/";

    @GET("products")
    Call<StoreModel> getProducts();

    class Factory {
        public static Retropow create(){

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            return retrofit.create(Retropow.class);
        }

    }
}
