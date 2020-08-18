package si.rozna.ping.adapter;

import android.app.Activity;
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

import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import si.rozna.ping.R;
import si.rozna.ping.auth.AuthService;
import si.rozna.ping.models.Group;
import si.rozna.ping.rest.GroupsApi;
import si.rozna.ping.rest.ServiceGenerator;
import si.rozna.ping.ui.components.GeneralPopupComponent;
import si.rozna.ping.ui.components.InviteMemberPopupComponent;
import si.rozna.ping.ui.components.PopupComponent;
import si.rozna.ping.ui.drawer.groups.GroupsViewModel;
import timber.log.Timber;

public class GroupsRecyclerViewAdapter extends RecyclerView.Adapter<GroupsRecyclerViewAdapter.CardViewHolder> {

    private Activity parentActivity;
    private List<Group> groups;
    private CardViewHolder expandedGroupHolder;
    private PopupComponent openedPopup;

    /* DB */
    private GroupsViewModel groupsViewModel;

    /* REST */
    private GroupsApi groupsApi = ServiceGenerator.createService(GroupsApi.class);

    public GroupsRecyclerViewAdapter(Activity parentActivity, GroupsViewModel groupsViewModel) {
        this.parentActivity = parentActivity;
        this.groupsViewModel = groupsViewModel;
        this.groups = new ArrayList<>();
    }

    public GroupsRecyclerViewAdapter(Activity parentActivity, GroupsViewModel groupsViewModel, List<Group> groups) {
        this.parentActivity = parentActivity;
        this.groupsViewModel = groupsViewModel;
        this.groups = groups;
        notifyDataSetChanged();
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

        FirebaseUser user = AuthService.getCurrentUser().orElseThrow(() -> new RuntimeException("User is not logged in!"));
        holder.positionInGroup.setText(group.getOwnerId().equals(user.getUid())
                ? "Owner"
                : "Member");

        /* Listeners set up */
        holder.expandViewBtn.setOnClickListener(view -> expandGroup(holder));

        holder.inviteMemberBtn.setOnClickListener(view -> {
            new InviteMemberPopupComponent(
                    parentActivity,
                    view,
                    group
            ).show();
        });

        holder.deleteGroupBtn.setOnClickListener(view -> {

            // Warning message to be shown (leave - member, delete - owner)
            String message = "Do you really want to "
                    + (group.getOwnerId().equals(user.getUid())
                            ? "delete"
                            : "leave")
                    + " '" + group.getName() + "' group?";

            // Popup - Ask user if he really wants to delete/leave group
            GeneralPopupComponent popup = new GeneralPopupComponent(
                    parentActivity,
                    view,
                    GeneralPopupComponent.Action.CUSTOM,
                    message
            );

            // Custom actions on popup
            popup.getOkButton().setOnClickListener(v -> leaveOrDeleteGroup(group, position));
            popup.getCancelButton().setOnClickListener(v -> popup.dismiss());

            popup.show();

            this.openedPopup = popup;

        });

    }

    @Override
    public int getItemCount() {
        return groups.size();
    }


    class CardViewHolder extends RecyclerView.ViewHolder {

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

    private void leaveOrDeleteGroup(Group group, int position){
        Optional<FirebaseUser> user = AuthService.getCurrentUser();
        if(!user.isPresent()) {
            // TODO: Log out user here
            Timber.e("User is not logged in. Logout user here!");
            return;
        }

        if(group.getOwnerId().equals(user.get().getUid())){
            // User is owner so delete group
            deleteGroup(group, position);
        } else {
            // User is member so leave group
            leaveGroup(group, position);
        }
    }

    private void leaveGroup(Group group, int position){

        groupsApi.leaveGroup(group.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if(response.isSuccessful()) {

                    closeLastExpandedGroup();
                    expandedGroupHolder = null;
                    groups.remove(position);
                    notifyItemRemoved(position);
                    closePopup();

                    groupsViewModel.deleteGroup(group.getId());
                }else {
                    // TODO: Tell user smth went wrong
                    Timber.e(response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable throwable) {
                Timber.e(throwable);
            }
        });
    }

    private void deleteGroup(Group group, int position){

        groupsApi.deleteGroup(group.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if(response.isSuccessful()) {
                    closeLastExpandedGroup();
                    expandedGroupHolder = null;
                    groups.remove(position);
                    notifyItemRemoved(position);
                    closePopup();

                    groupsViewModel.deleteGroup(group.getId());
                }else {
                    // TODO: Tell user smth went wrong
                    Timber.e(response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable throwable) {
                Timber.e(throwable);
            }
        });
    }

    private void expandGroup(CardViewHolder holder){
        if(holder.expendableView.getVisibility() == View.GONE){

            closeLastExpandedGroup();

            // Opens currently pressed group
            TransitionManager.beginDelayedTransition(holder.mainView, new AutoTransition());
            holder.expendableView.setVisibility(View.VISIBLE);
            holder.expandViewBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
            this.expandedGroupHolder = holder;
        } else {
            // Closes currently pressed group
            holder.expendableView.setVisibility(View.GONE);
            holder.expandViewBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        }
    }

    private void closeLastExpandedGroup(){
        // Closes last opened group (if any)
        if(expandedGroupHolder != null) {
            expandedGroupHolder.expendableView.setVisibility(View.GONE);
            expandedGroupHolder.expandViewBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        }
    }

    private void closePopup(){
        if(openedPopup != null) {
            openedPopup.dismiss();
            openedPopup = null;
        }
    }

}
