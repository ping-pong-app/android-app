package si.rozna.ping.ui.drawer.groups;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import si.rozna.ping.R;
import si.rozna.ping.adapter.RecyclerViewAdapter;
import si.rozna.ping.auth.AuthService;
import si.rozna.ping.models.Group;
import si.rozna.ping.rest.GroupsApi;
import si.rozna.ping.rest.ServiceGenerator;
import si.rozna.ping.ui.MainActivity;
import timber.log.Timber;

public class GroupsFragment extends Fragment {

    private static int TIME_BETWEEN_REFRESH_IN_MS = 10000;

    private View view;

    /* Components */
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout progressBar;
    private FloatingActionButton fabNewGroup;

    /* REST */
    private GroupsApi groupsApi = ServiceGenerator.createService(GroupsApi.class);

    /* Variables */
    private boolean canRefresh;


    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

        recyclerViewAdapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        canRefresh = true;

        queryGroups();

        listenerSetup();

        showLoadingScreen();

    }

    private void queryGroups(){

        Optional<FirebaseUser> user = AuthService.getCurrentUser();
        if (!user.isPresent()) {
            // TODO: Log out user here
            Timber.e("User is not logged in. Logout user here!");
            return;
        }

        groupsApi.getAllGroups().enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    recyclerViewAdapter.setGroups(response.body());
                    showContent();
                }else {
                    Timber.e(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
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
                }, TIME_BETWEEN_REFRESH_IN_MS);
            } else {
                Snackbar.make(view, R.string.no_need_to_refresh, Snackbar.LENGTH_SHORT).show();
            }
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

}