package group.pinger.ui.components;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import group.pinger.R;
import group.pinger.auth.AuthService;

public class GeneralPopupComponent extends PopupComponent {

    public enum Action {
        CUSTOM, // Listeners must be set manually than
        LEAVE_APPLICATION,
        LOGOUT
    }

    private Action action;
    private String message;

    private ImageButton okButton;
    private ImageButton cancelButton;

    public GeneralPopupComponent(Activity parentActivity,
                                 View parentView,
                                 Action action,
                                 String message) {
        super(parentActivity, parentView, R.layout.popup_general);

        this.okButton = getPopupView().findViewById(R.id.btn_popup_general_action_ok);
        this.cancelButton = getPopupView().findViewById(R.id.btn_popup_general_action_cancel);

        this.action = action;
        this.message = message;

        init();
        setUpListeners();
    }

    @Override
    protected void init() {
        // Set popup message
        if(message != null) {
            TextView messageTextView = getPopupView().findViewById(R.id.tv_popup_general_info);
            messageTextView.setText(message);
        }
    }

    @Override
    protected void setUpListeners() {

        if(action == Action.CUSTOM){
            // Listeners should be manually set if CUSTOM action is selected
            return;
        }

        // Cancel button listeners
        cancelButton.setOnClickListener(view1 -> getPopupWindow().dismiss());

        // Ok button listeners
        okButton.setOnClickListener((view1) -> {
            getPopupWindow().dismiss();
            switch (action){
                case LEAVE_APPLICATION:
                    Activity activity = getParentActivity();
                    if(activity != null)
                        activity.finish();
                    break;
                case LOGOUT:
                    AuthService.logout(getParentActivity());
                    break;
            }
        });

    }

    @Override
    public void updateUI() {}

    /* Getters & setters */

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ImageButton getOkButton() {
        return okButton;
    }

    public void setOkButton(ImageButton okButton) {
        this.okButton = okButton;
    }

    public ImageButton getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(ImageButton cancelButton) {
        this.cancelButton = cancelButton;
    }

}
