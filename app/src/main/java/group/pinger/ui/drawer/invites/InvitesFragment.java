package group.pinger.ui.drawer.invites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import group.pinger.R;
import group.pinger.adapter.InvitesRecyclerViewAdapter;
import group.pinger.auth.AuthService;
import group.pinger.models.ExtendedInvite;
import group.pinger.models.api.ExtendedInviteApiModel;
import group.pinger.models.mappers.InviteMapper;
import group.pinger.rest.InvitesApi;
import group.pinger.rest.ServiceGenerator;
import group.pinger.ui.drawer.groups.GroupsViewModel;
import timber.log.Timber;

public class InvitesFragment extends Fragment {

    /* REST */
    private InvitesApi invitesApi = ServiceGenerator.createService(InvitesApi.class);

    /* DB */
    private GroupsViewModel groupsViewModel;

    /* Fragment's view */
    private View view;

    /* Components */
    private RecyclerView recyclerView;
    private InvitesRecyclerViewAdapter invitesRecyclerViewAdapter;
    private LinearLayout progressBar;

    public InvitesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_invites, container, false);

        recyclerView = view.findViewById(R.id.invites_recycler_view);
        progressBar = view.findViewById(R.id.invites_progress_bar);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        groupsViewModel = new ViewModelProvider(this).get(GroupsViewModel.class);

        invitesRecyclerViewAdapter = new InvitesRecyclerViewAdapter(getActivity(), view, groupsViewModel);
        recyclerView.setAdapter(invitesRecyclerViewAdapter);

        queryInvites();
        showLoadingScreen();
    }

    private void queryInvites(){

        Optional<FirebaseUser> user = AuthService.getCurrentUser();
        if (!user.isPresent()) {
            // TODO: Log out user here
            Timber.e("User is not logged in. Logout user here!");
            return;
        }

        invitesApi.getUserInvites().enqueue(new Callback<List<ExtendedInviteApiModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<ExtendedInviteApiModel>> call, @NotNull Response<List<ExtendedInviteApiModel>> response) {
                if(response.isSuccessful() && response.body() != null) {

                    List<ExtendedInvite> invites = response.body().stream()
                            .map(InviteMapper::fromExtendedApiModel)
                            .collect(Collectors.toList());

                    invitesRecyclerViewAdapter.setInvites(invites);
                    showContent();
                }else {
                    Timber.e(response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<ExtendedInviteApiModel>> call, @NotNull Throwable t) {
                Timber.e(t);
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