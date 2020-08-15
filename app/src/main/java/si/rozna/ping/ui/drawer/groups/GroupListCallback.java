package si.rozna.ping.ui.drawer.groups;

import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import si.rozna.ping.adapter.RecyclerViewAdapter;
import si.rozna.ping.models.Group;
import si.rozna.ping.models.api.GroupApiModel;
import si.rozna.ping.models.mappers.GroupMapper;
import timber.log.Timber;

public class GroupListCallback implements Callback<List<GroupApiModel>> {

    private RecyclerViewAdapter recyclerViewAdapter;
    private LinearLayout progressBar;
    private RecyclerView recyclerView;

    public GroupListCallback(RecyclerViewAdapter recyclerViewAdapter,
                             LinearLayout progressBar,
                             RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.recyclerViewAdapter = recyclerViewAdapter;
        this.progressBar = progressBar;
    }

    @Override
    public void onResponse(@NotNull Call<List<GroupApiModel>> call, Response<List<GroupApiModel>> response) {
        if (response.isSuccessful() && response.body() != null) {

            List<Group> groups = response.body().stream()
                    .map(GroupMapper::fromApiModel)
                    .collect(Collectors.toList());

            recyclerViewAdapter.setGroups(groups);
            showContent();
        } else {
            Timber.e(response.message());
        }
    }

    @Override
    public void onFailure(@NotNull Call<List<GroupApiModel>> call, @NotNull Throwable t) {
        Timber.e(t);
    }

    private void showContent() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
