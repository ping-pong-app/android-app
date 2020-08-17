package si.rozna.ping.ui.components;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import si.rozna.ping.Constants;
import si.rozna.ping.R;
import si.rozna.ping.auth.AuthService;
import si.rozna.ping.auth.LoginActivity;
import si.rozna.ping.models.Group;
import si.rozna.ping.models.api.EntityIdentifierApiModel;
import si.rozna.ping.models.api.InviteApiModel;
import si.rozna.ping.rest.InvitesApi;
import si.rozna.ping.rest.ServiceGenerator;
import timber.log.Timber;

public class InviteMemberPopupComponent extends PopupComponent {

    /* REST */
    private InvitesApi invitesApi = ServiceGenerator.createService(InvitesApi.class);

    private ConstraintLayout contentLayout;
    private ConstraintLayout successfulLayout;

    private ImageButton inviteButton;
    private ImageButton cancelButton;
    private TextInputEditText emailInput;
    private TextView warningTextView;
    private ProgressBar progressBar;

    private Group group;
    private String warningText;

    public InviteMemberPopupComponent(Activity parentActivity,
                                      View parentView,
                                      Group group) {
        super(parentActivity, parentView, R.layout.popup_invite);

        this.group = group;
        this.warningText = "";

        init();
        setUpListeners();
    }

    @Override
    protected void init() {

        contentLayout = getPopupView().findViewById(R.id.layout_popup_invite_content);
        successfulLayout = getPopupView().findViewById(R.id.layout_popup_invite_successful);

        inviteButton = getPopupView().findViewById(R.id.btn_popup_invite_action_invite);
        cancelButton = getPopupView().findViewById(R.id.btn_popup_invite_action_cancel);
        emailInput = getPopupView().findViewById(R.id.input_popup_invite_email);
        warningTextView = getPopupView().findViewById(R.id.tv_popup_invite_email_warning);
        progressBar = getPopupView().findViewById(R.id.progress_bar_popup_invite);

        // Set visibilities
        contentLayout.setVisibility(View.VISIBLE);
        successfulLayout.setVisibility(View.GONE);
        inviteButton.setVisibility(View.GONE);
        warningTextView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        // Set warning text
        if(warningText != null && !warningText.isEmpty()) {
            warningTextView.setText(warningText);
        }

    }

    @Override
    protected void setUpListeners() {

        // Button listeners
        inviteButton.setOnClickListener(v -> {
            inviteMember(emailInput.getText() != null
                    ? emailInput.getText().toString()
                    : "" );
        });
        cancelButton.setOnClickListener(v -> getPopupWindow().dismiss());

        // Input text listener
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inviteButton.setVisibility(charSequence.length() > 0
                        ? View.VISIBLE
                        : View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                warningTextView.setVisibility(View.GONE);
                warningText = "";
            }
        });

    }

    @Override
    public void updateUI() {}

    private void inviteMember(String email){

        Optional<String> userId = AuthService.getCurrentUserId();
        if(!userId.isPresent()){
            // TODO: Current user is not logged in -> logout
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        InviteApiModel invite = new InviteApiModel();
        invite.setEmail(email);
        invite.setUserId(userId.get());
        invite.setGroupId(group.getId());

        invitesApi.inviteUser(invite).enqueue(new Callback<EntityIdentifierApiModel>() {
            @Override
            public void onResponse(Call<EntityIdentifierApiModel> call, Response<EntityIdentifierApiModel> response) {
                if(response.isSuccessful()){

                    // Show successful invitation screen
                    contentLayout.setVisibility(View.GONE);
                    successfulLayout.setVisibility(View.VISIBLE);

                    // Dismiss popup after some time
                    new Handler().postDelayed(() -> {
                        getPopupWindow().dismiss();
                    }, Constants.DISMISS_SUCCESSFUL_INVITE_AFTER);

                } else {
                    int code = response.code();

                    switch (code) {
                        case 400:
                            // 400 (Bad Request) - There is no user with provided email
                            warningTextView.setText(R.string.invite_member_user_not_found);
                            warningTextView.setVisibility(View.VISIBLE);
                            break;
                        case 409:
                            // 409 (Conflict) - User is already invited in group
                            warningTextView.setText(R.string.invite_member_user_already_invited);
                            warningTextView.setVisibility(View.VISIBLE);
                            break;
                    }

                    Timber.e("Error code: " + response.code() + "\nError message: " + response.message());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<EntityIdentifierApiModel> call, Throwable throwable) {
                Timber.e(throwable);
            }
        });
    }
}
