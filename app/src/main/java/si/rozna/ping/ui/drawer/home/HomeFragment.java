package si.rozna.ping.ui.drawer.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import si.rozna.ping.R;
import si.rozna.ping.adapter.GroupSelectSpinnerAdapter;
import si.rozna.ping.auth.AuthService;
import si.rozna.ping.fcm.FcmService;
import si.rozna.ping.models.Group;
import si.rozna.ping.models.api.PingApiModel;
import si.rozna.ping.models.mappers.GroupMapper;
import si.rozna.ping.rest.GroupsApi;
import si.rozna.ping.rest.PingApi;
import si.rozna.ping.rest.ServiceGenerator;
import si.rozna.ping.ui.components.PingButtonUIComponent;
import si.rozna.ping.ui.drawer.groups.GroupsViewModel;
import timber.log.Timber;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private GroupsViewModel groupsViewModel;

    /* REST */
    private PingApi pingApi = ServiceGenerator.createService(PingApi.class);
    private GroupsApi groupsApi = ServiceGenerator.createService(GroupsApi.class);

    /* UI components*/
    private PingButtonUIComponent pingButtonComponent;
    private Spinner groupSelectSpinner;
    private GroupSelectSpinnerAdapter groupSelectSpinnerAdapter;

    /* UI handler */
    private final Handler pingButtonHandler = new UIHandler(this);
    private final static int MSG_UPDATE_PING_BUTTON = 1234567;
    private final static int UPDATE_RATE_MS_PING_BUTTON = 10;

    /* Variables */
    private Group selectedGroup;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Bind ViewModel to fragment
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        mViewModel.getTest().observe(this, test -> { /* code goes here */ });

        groupsViewModel = new ViewModelProvider(this).get(GroupsViewModel.class);

    }

    @Override
    public void onStart() {
        super.onStart();
        pingButtonHandler.sendEmptyMessage(MSG_UPDATE_PING_BUTTON);
    }

    @Override
    public void onStop() {
        super.onStop();
        pingButtonHandler.removeMessages(MSG_UPDATE_PING_BUTTON);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        groupSelectSpinner = view.findViewById(R.id.group_select_spinner);

        pingButtonComponent = new PingButtonUIComponent(getActivity(), view, R.id.ping_button, R.id.ping_progress_bar);
        groupSelectSpinnerAdapter = new GroupSelectSpinnerAdapter();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        pingButtonComponent.setParentActivity(getActivity());

        listenerSetup();
        observerSetup();
    }

    private void listenerSetup(){
        groupSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedGroup = (Group) groupSelectSpinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void observerSetup(){

        groupsViewModel.getGroups().observe(getViewLifecycleOwner(), groupDbModels -> {
            List<Group> groups = groupDbModels.stream()
                    .map(GroupMapper::fromDbModel)
                    .collect(Collectors.toList());

            groupSelectSpinnerAdapter.setGroups(groups);
            groupSelectSpinner.setAdapter(groupSelectSpinnerAdapter);
        });

    }

    /* UI */

    @UiThread
    static class UIHandler extends Handler {

        private final WeakReference<HomeFragment> fragment;

        UIHandler(HomeFragment service) {
            this.fragment = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message message) {
            if (MSG_UPDATE_PING_BUTTON == message.what) {
                fragment.get().updatePingButtonUI();
                sendEmptyMessageDelayed(MSG_UPDATE_PING_BUTTON, UPDATE_RATE_MS_PING_BUTTON);
            }
        }
    }

    @UiThread
    public void updatePingButtonUI(){

        if(pingButtonComponent.isTimeToPing()){
            pingButtonComponent.setTimeToPing(false);

            // Execute ping in background
            new Handler().post(this::ping);
        }

        pingButtonComponent.updateUI();
    }

    @WorkerThread
    private void ping(){

        if(selectedGroup == null){
            return;
        }

        Optional<String> currentUserId = AuthService.getCurrentUserId();
        if(!currentUserId.isPresent()) {
            // TODO: Ping failed - logout
            Snackbar.make(Objects.requireNonNull(getView()),
                    "User not logged in!", Snackbar.LENGTH_LONG)
                    .show();
            Timber.e("User is not logged in!");
            return;
        }

        PingApiModel pingModel = new PingApiModel();
        pingModel.setGroupId(selectedGroup.getId()); /* TODO: Set group id*/
        pingModel.setPingerId(currentUserId.get());


        pingApi.pingGroup(pingModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.isSuccessful()) {
                    FcmService.subscribeToPong(selectedGroup.getId());
                    Snackbar.make(Objects.requireNonNull(getView()),
                            "Ping successful", Snackbar.LENGTH_LONG)
                            .show();

                } else {
                    Snackbar.make(Objects.requireNonNull(getView()),
                            "Ping failed", Snackbar.LENGTH_LONG)
                            .show();

                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Timber.e(t);
            }
        });
    }

}
