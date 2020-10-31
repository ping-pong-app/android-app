package group.pinger.ui.components;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public abstract class PopupComponent extends UIComponent {


    private final View popupView;
    private final PopupWindow popupWindow;

    public PopupComponent(Activity parentActivity, View parentView, int popupLayoutResource) {
        super(parentActivity, parentView);

        // Inflate popup layout
        LayoutInflater inflater = (LayoutInflater) parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(popupLayoutResource, null);

        // Set up popup window
        popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                true
        );

    }

    public void show(){
        // Show popup window
        // It doesn't matter Which view is used,
        // it is only used for the window token
        popupWindow.showAtLocation(getParentView(), Gravity.NO_GRAVITY, 0, 0);
    }

    public void dismiss(){
        popupWindow.dismiss();
    }

    public View getPopupView() {
        return popupView;
    }

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }
}
