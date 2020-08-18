package si.rozna.ping.ui.drawer.groups;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import si.rozna.ping.Constants;
import si.rozna.ping.R;
import si.rozna.ping.adapter.GroupsRecyclerViewAdapter;
import si.rozna.ping.auth.AuthService;
import si.rozna.ping.models.Group;
import si.rozna.ping.models.api.GroupApiModel;
import si.rozna.ping.models.db.GroupDbModel;
import si.rozna.ping.models.mappers.GroupMapper;
import si.rozna.ping.rest.GroupsApi;
import si.rozna.ping.rest.ServiceGenerator;
import si.rozna.ping.ui.MainActivity;
import timber.log.Timber;

public class GroupsFragment extends Fragment {


    /* Fragment's view */
    private View view;

    /* Components */
    private RecyclerView recyclerView;
    private GroupsRecyclerViewAdapter groupsRecyclerViewAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout progressBar;
    private FloatingActionButton fabNewGroup;

    /* REST */
    private GroupsApi groupsApi = ServiceGenerator.createService(GroupsApi.class);

    /* View model*/
    private GroupsViewModel groupsViewModel;

    /* Variables */
    private boolean canRefresh;


    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_groups, container, false);

        recyclerView = view.findViewById(R.id.groups_recycler_view);
        fabNewGroup = view.findViewById(R.id.fab_new_group);
        progressBar = view.findViewById(R.id.groups_progress_bar);
        swipeRefreshLayout = view.findViewById(R.id.groups_swipe_refresh_spinner);

        fabNewGroup.setOnClickListener((v) -> {

            if (getActivity() instanceof MainActivity)
                ((MainActivity) getActivity()).switchFragment(new NewGroupFragment());
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        groupsViewModel = new ViewModelProvider(this).get(GroupsViewModel.class);

        groupsRecyclerViewAdapter = new GroupsRecyclerViewAdapter(getActivity(), groupsViewModel);
        recyclerView.setAdapter(groupsRecyclerViewAdapter);

        canRefresh = true;

//        queryGroups();
        listenerSetup();
        observerSetup();
        showLoadingScreen();
    }

    private void queryGroups(){

        Optional<FirebaseUser> user = AuthService.getCurrentUser();
        if (!user.isPresent()) {
            // TODO: Log out user here
            Timber.e("User is not logged in. Logout user here!");
            return;
        }

        groupsApi.getAllGroups().enqueue(new Callback<List<GroupApiModel>>() {
            @Override
            public void onResponse(Call<List<GroupApiModel>> call, Response<List<GroupApiModel>> response) {
                if(response.isSuccessful() && response.body() != null) {

                    List<Group> groups = response.body().stream()
                            .map(GroupMapper::fromApiModel)
                            .collect(Collectors.toList());

                    // TODO: Cache groups (update existing -> if not exist add group)
                    //groups.forEach(group -> groupsViewModel.addGroup(GroupMapper.toDbModel(group)));

                    groupsRecyclerViewAdapter.setGroups(groups);
                    showContent();
                }else {
                    Timber.e(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<GroupApiModel>> call, Throwable t) {
                Timber.e(t);
            }
        });
    }


    private void listenerSetup(){
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);

            if(canRefresh) {
                canRefresh = false;
                showLoadingScreen();
                queryGroups();

                new Handler().postDelayed(() -> {
                    canRefresh = true;
                }, Constants.TIME_BETWEEN_REFRESH_IN_MS);
            } else {
                Snackbar.make(view, R.string.no_need_to_refresh, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void observerSetup(){
        groupsViewModel.getGroups().observe(getViewLifecycleOwner(), groupDbModels -> {

            List<Group> groups = groupDbModels.stream()
                    .map(GroupMapper::fromDbModel)
                    .collect(Collectors.toList());

            groupsRecyclerViewAdapter.setGroups(groups);
            showContent();
        });
    }

    private void showContent(){
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoadingScreen(){
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showNoContent(){
        // TODO: TBD
    }

}