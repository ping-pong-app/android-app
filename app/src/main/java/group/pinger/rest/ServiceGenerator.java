package group.pinger.rest;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import group.pinger.Constants;
import timber.log.Timber;

public class ServiceGenerator {

    private static Retrofit retrofit;

    static {
        init();
    }

    private static void init() {
        AuthInterceptor authInterceptor = new AuthInterceptor();
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(authInterceptor).build();

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        Timber.d("Retrofit built with base url: %s", retrofit.baseUrl().url().toString());
    }

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
