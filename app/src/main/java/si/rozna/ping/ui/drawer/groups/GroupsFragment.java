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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import si.rozna.ping.Constants;
import si.rozna.ping.R;
import si.rozna.ping.adapter.RecyclerViewAdapter;
import si.rozna.ping.models.Group;
import si.rozna.ping.models.Mapper;
import si.rozna.ping.models.dto.GroupDTO;
import si.rozna.ping.rest.RestAPI;
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
    private RestAPI mRestClient = ServiceGenerator.createService(RestAPI.class);

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null) {
            // TODO: Log out user here
            Timber.e("User is not logged in. Logout user here!");
            return;
        }


        user.getIdToken(true).addOnCompleteListener(task -> {

            String token = task.getResult().getToken();

            mRestClient.getAllGroups(Constants.BEARER_TOKEN_PREFIX + token).enqueue(new Callback<List<GroupDTO>>() {
                @Override
                public void onResponse(Call<List<GroupDTO>> call, Response<List<GroupDTO>> response) {

                    if(response.isSuccessful()) {

                        List<GroupDTO> groupDTOs = response.body();

                        if(groupDTOs != null && groupDTOs.size() > 0) {

                            List<Group> groups = new ArrayList<>();

                            for (GroupDTO groupDTO : groupDTOs) {
                                groups.add(Mapper.mapGroupFromDTO(groupDTO));
                            }
                            recyclerViewAdapter.setGroups(groups);
                            showContent();
                        }

                    }else {
                        Timber.e(response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<GroupDTO>> call, Throwable t) {
                    Timber.e(t);
                }
            });
        }).addOnFailureListener(e -> Timber.i("Failed to retrieve token"));
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