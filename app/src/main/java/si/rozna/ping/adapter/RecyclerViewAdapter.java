package si.rozna.ping.adapter;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import si.rozna.ping.Constants;
import si.rozna.ping.R;
import si.rozna.ping.models.Group;
import si.rozna.ping.models.dto.GroupDTO;
import si.rozna.ping.rest.RestAPI;
import si.rozna.ping.rest.ServiceGenerator;
import timber.log.Timber;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CardViewHolder> {

    private List<Group> groups;
    private CardViewHolder expandedGroupHolder;

    /* REST */
    private RestAPI mRestClient = ServiceGenerator.createService(RestAPI.class);

    public RecyclerViewAdapter() {
        this.groups = new ArrayList<>();
    }

    public RecyclerViewAdapter(List<Group> groups) {
        this.groups = groups;
    }

    public void setGroups(List<Group> groups){
        this.groups = groups;
        closeLastExpandedGroup();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_group_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {

        Group group = groups.get(position);

        holder.groupName.setText(group.getName());
        holder.positionInGroup.setText(group.getOwnerId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())
                ? "Owner"
                : "Member");

        holder.expandViewBtn.setOnClickListener((view) -> {
            if(holder.expendableView.getVisibility() == View.GONE){

                closeLastExpandedGroup();

                // Opens currently pressed group
                TransitionManager.beginDelayedTransition(holder.mainView, new AutoTransition());
                holder.expendableView.setVisibility(View.VISIBLE);
                holder.expandViewBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);

                this.expandedGroupHolder = holder;
            } else {
                // Closes currently pressed group
//                TransitionManager.beginDelayedTransition(holder.tests, new AutoTransition());
                holder.expendableView.setVisibility(View.GONE);
                holder.expandViewBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
            }
        });

        holder.deleteGroupBtn.setOnClickListener((view) -> {

            // TODO: ASK USER IF HE'S SURE ABOUT DELETING

            // TODO: IF OWNER DELETE -> IF MEMBER LEAVE

            deleteGroup(group, position);
        });

        holder.inviteMemberBtn.setOnClickListener((view) -> {
            Timber.d("Member invited");
        });

    }

    @Override
    public int getItemCount() {
        return groups.size();
    }


    class CardViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout mainView;
        private ConstraintLayout expendableView;
        private Button expandViewBtn;
        private Button inviteMemberBtn;
        private Button deleteGroupBtn;
        private TextView groupName;
        private TextView positionInGroup;

        private CardViewHolder(View itemView) {
            super(itemView);

            mainView = itemView.findViewById(R.id.group_item_main_layout);
            expendableView = itemView.findViewById(R.id.expendable_view);
            expandViewBtn = itemView.findViewById(R.id.btn_expand_card_view);
            groupName = itemView.findViewById(R.id.group_name);
            positionInGroup = itemView.findViewById(R.id.position_in_group);
            inviteMemberBtn = itemView.findViewById(R.id.btn_invite_member);
            deleteGroupBtn = itemView.findViewById(R.id.btn_delete_group);

        }

    }

    private void deleteGroup(Group group, int position){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null) {
            // TODO: Log out user here
            Timber.e("User is not logged in. Logout user here!");
            return;
        }

        user.getIdToken(true).addOnCompleteListener(task -> {

            String token = task.getResult().getToken();

            mRestClient.deleteGroup(Constants.BEARER_TOKEN_PREFIX + token, group.getId()).enqueue(new Callback<GroupDTO>() {
                @Override
                public void onResponse(Call<GroupDTO> call, Response<GroupDTO> response) {

                    if(response.isSuccessful()) {

                        expandedGroupHolder = null;
                        groups.remove(position);
                        notifyDataSetChanged();

                    }else {
                        // TODO: Tell user smth went wrong
                        Timber.e(response.message());
                    }
                }

                @Override
                public void onFailure(Call<GroupDTO> call, Throwable t) {
                    Timber.e(t);
                }
            });
        }).addOnFailureListener(e -> Timber.i("Failed to retrieve token"));
    }

    private void closeLastExpandedGroup(){
        // Closes last opened group (if any)
        if(expandedGroupHolder != null) {
            expandedGroupHolder.expendableView.setVisibility(View.GONE);
            expandedGroupHolder.expandViewBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        }

    }


}
