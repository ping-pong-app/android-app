package si.rozna.ping.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import si.rozna.ping.R;
import si.rozna.ping.fcm.FcmService;
import si.rozna.ping.models.ExtendedInvite;
import si.rozna.ping.models.api.GroupApiModel;
import si.rozna.ping.models.mappers.GroupMapper;
import si.rozna.ping.rest.InvitesApi;
import si.rozna.ping.rest.ServiceGenerator;
import si.rozna.ping.ui.components.GeneralPopupComponent;
import si.rozna.ping.ui.components.PopupComponent;
import si.rozna.ping.ui.drawer.groups.GroupsViewModel;

public class InvitesRecyclerViewAdapter extends RecyclerView.Adapter<InvitesRecyclerViewAdapter.CardViewHolder> {

    /* REST */
    private InvitesApi invitesApi = ServiceGenerator.createService(InvitesApi.class);

    /* DB */
    private GroupsViewModel groupsViewModel;

    private Activity parentActivity;
    private View parentView;
    private List<ExtendedInvite> invites;


    public InvitesRecyclerViewAdapter(Activity parentActivity, View parentView, GroupsViewModel groupsViewModel) {
        this.parentActivity = parentActivity;
        this.parentView = parentView;
        this.groupsViewModel = groupsViewModel;
        this.invites = new ArrayList<>();
    }

    public InvitesRecyclerViewAdapter(Activity parentActivity, View parentView, GroupsViewModel groupsViewModel, List<ExtendedInvite> invites) {
        this.parentActivity = parentActivity;
        this.parentView = parentView;
        this.groupsViewModel = groupsViewModel;
        this.invites = invites;
        notifyDataSetChanged();
    }

    public void setInvites(List<ExtendedInvite> invites){
        this.invites = invites;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_invite_item, parent, false);
        return new InvitesRecyclerViewAdapter.CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {

        ExtendedInvite invite = invites.get(position);

        holder.inviteGroupName.setText(String.format(
                parentActivity.getString(R.string.invite_group_name),
                invite.getGroup().getName()));
        holder.inviteSenderName.setText(String.format(
                parentActivity.getString(R.string.invite_sender_name),
                invite.getUser().getDisplayName())
        );

        holder.acceptInviteBtn.setOnClickListener(view
                -> acceptInvitation(invite.getId(), position, invite.getGroup().getName()));

        holder.rejectInviteBtn.setOnClickListener(view -> {
            GeneralPopupComponent popup = new GeneralPopupComponent(
                    parentActivity,
                    view,
                    GeneralPopupComponent.Action.CUSTOM,
                    String.format(parentActivity.getString(R.string.warning_reject_invitation), invite.getGroup().getName()));

            popup.getOkButton().setOnClickListener(v
                    -> rejectInvitation(invite.getId(), position, popup));

            popup.getCancelButton().setOnClickListener(v -> popup.dismiss());

            popup.show();

        });


    }

    @Override
    public int getItemCount() {
        return invites.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {

        private ImageButton acceptInviteBtn;
        private ImageButton rejectInviteBtn;
        private TextView inviteGroupName;
        private TextView inviteSenderName;

        private CardViewHolder(View itemView) {
            super(itemView);

            acceptInviteBtn = itemView.findViewById(R.id.btn_invite_accept);
            rejectInviteBtn = itemView.findViewById(R.id.btn_invite_reject);
            inviteGroupName = itemView.findViewById(R.id.invite_group_name);
            inviteSenderName = itemView.findViewById(R.id.invite_sender_name);

        }

    }

    private void acceptInvitation(String inviteId, int position, String groupName){
        invitesApi.acceptInvitation(inviteId).enqueue(new Callback<GroupApiModel>() {
            @Override
            public void onResponse(Call<GroupApiModel> call, Response<GroupApiModel> response) {

                if(response.isSuccessful()) {

                    GroupApiModel acceptedGroup = response.body();
                    if(acceptedGroup != null) {
                        invites.remove(position);
                        notifyItemRemoved(position);
                        showSnackBar(String.format(parentActivity.getString(R.string.invitation_accepted), groupName));

                        // Add received group to cache
                        groupsViewModel.addGroup(GroupMapper.toDbModelFromApiModel(acceptedGroup));
                    } else {
                        // TODO: Smth went wrong
                        // TODO: Invitation not accepted and also not deleted
                    }

                } else {
                    // TODO: Smth went wrong
                }
            }

            @Override
            public void onFailure(Call<GroupApiModel> call, Throwable t) {
                showSnackBar(parentActivity.getString(R.string.error_excuse));
            }
        });
    }

    private void rejectInvitation(String inviteId, int position, PopupComponent popup){
        invitesApi.rejectInvitation(inviteId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                invites.remove(position);
                notifyItemRemoved(position);
                popup.dismiss();
                showSnackBar(parentActivity.getString(R.string.invitation_rejected));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                popup.dismiss();
                showSnackBar(parentActivity.getString(R.string.error_excuse));
            }
        });
    }

    private void showSnackBar(String message){
        Snackbar.make(parentView, message, BaseTransientBottomBar.LENGTH_LONG).show();
    }

}
