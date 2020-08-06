package si.rozna.ping.rest;

import java.util.Timer;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import si.rozna.ping.Constants;
import timber.log.Timber;

public class ServiceGenerator {

    private static Retrofit.Builder retrofitBuilder;
    private static OkHttpClient.Builder httpClient;
    private static Retrofit retrofit;

    static {
        init();
    }

    private static void init(){
        httpClient = new OkHttpClient.Builder();
        retrofitBuilder = new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create());

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        retrofit = retrofitBuilder.client(httpClient.build()).build();
        Timber.d("Retrofit built with base url: %s", retrofit.baseUrl().url().toString());
    }

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
