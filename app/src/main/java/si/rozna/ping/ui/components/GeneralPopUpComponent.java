package si.rozna.ping.ui.components;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import si.rozna.ping.R;
import si.rozna.ping.auth.AuthService;

public class GeneralPopUpComponent extends UIComponent {

    public enum Action {
        CUSTOM, // Listeners must be set manually than
        LEAVE_APPLICATION,
        LOGOUT
    }

    private final View popupView;
    private final PopupWindow popupWindow;

    private Action action;
    private String message;

    private ImageButton okButton;
    private ImageButton cancelButton;

    public GeneralPopUpComponent(Activity parentActivity,
                                 View parentView,
                                 Action action,
                                 String message) {
        super(parentActivity, parentView);

        // Inflate popup layout
        LayoutInflater inflater = (LayoutInflater) parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.popup_general, null);

        // Set up popup window
        popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                true
        );

        okButton = popupView.findViewById(R.id.btn_popup_general_action_ok);
        cancelButton = popupView.findViewById(R.id.btn_popup_general_action_cancel);

        this.action = action;
        this.message = message;

        init();
        setUpListeners();
    }

    @Override
    protected void init() {
        // Set popup message
        if(message != null) {
            TextView messageTextView = popupView.findViewById(R.id.tv_popup_general_info);
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
        cancelButton.setOnClickListener(view1 -> popupWindow.dismiss());

        // Ok button listeners
        okButton.setOnClickListener((view1) -> {
            popupWindow.dismiss();
            switch (action){
                case LEAVE_APPLICATION:
                    getParentActivity().finish();
                    break;
                case LOGOUT:
                    AuthService.logout(getParentActivity());
                    break;
            }
        });

    }

    @Override
    public void updateUI() {
    }

    public void show(){
        // Show popup window
        // It doesn't matter Which view is used,
        // it is only used for the window token
        popupWindow.showAtLocation(getParentView(), Gravity.NO_GRAVITY, 0, 0);
    }

    /* Getters & setters */

    public View getPopupView() {
        return popupView;
    }

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }

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
