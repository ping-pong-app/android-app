package group.pinger.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import group.pinger.Constants;
import group.pinger.R;
import group.pinger.models.Group;
import group.pinger.models.api.GroupApiModel;
import group.pinger.models.mappers.GroupMapper;
import group.pinger.rest.GroupsApi;
import group.pinger.rest.ServiceGenerator;
import group.pinger.ui.drawer.groups.GroupsViewModel;
import group.pinger.utils.SharedPreferencesUtil;
import timber.log.Timber;

public class LoadingActivity extends AppCompatActivity {

    private GroupsApi groupsApi = ServiceGenerator.createService(GroupsApi.class);

    private GroupsViewModel groupsViewModel;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        progressBar = findViewById(R.id.loading_screen_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        groupsViewModel = new ViewModelProvider(this).get(GroupsViewModel.class);

        cacheSetup();


    }

    private void cacheSetup(){

        SharedPreferencesUtil spu = SharedPreferencesUtil.getInstance();
        String isCached = spu.getString(Constants.SHARED_PREFERENCES_CACHE_KEY);

        if(isCached == null || !isCached.equals(getString(R.string.yes))){
            progressBar.setVisibility(View.VISIBLE);
            cacheGroups();
        } else {
            continueToApplication();
        }

    }

    private void cacheGroups(){
        groupsApi.getAllGroups().enqueue(new Callback<List<GroupApiModel>>() {
            @Override
            public void onResponse(Call<List<GroupApiModel>> call, Response<List<GroupApiModel>> response) {
                if(response.isSuccessful() && response.body() != null) {

                    List<Group> groups = response.body().stream()
                            .map(GroupMapper::fromApiModel)
                            .collect(Collectors.toList());

                    for(Group group : groups) {
                        groupsViewModel.addGroup(GroupMapper.toDbModel(group));
                    }

                    SharedPreferencesUtil spu = SharedPreferencesUtil.getInstance();
                    spu.putString(Constants.SHARED_PREFERENCES_CACHE_KEY, getString(R.string.yes));


                    continueToApplication();

                }else {

                    //TODO: Show error screen - Decide whether to continue or not

                    Timber.e("Code: %s", response.code());
                    Timber.e("Message: %s", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<GroupApiModel>> call, Throwable t) {
                Timber.e(t);
            }
        });
    }

    private void continueToApplication(){

        progressBar.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }, Constants.SPLASH_SCREEN_DURATION_IN_MILLIS);
    }

}