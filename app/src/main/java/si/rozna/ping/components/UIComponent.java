package si.rozna.ping.components;

import android.app.Activity;
import android.view.View;

public abstract class UIComponent {

    private Activity parentActivity;
    private View parentView;

    protected abstract void init();
    protected abstract void setUpListeners();
    public abstract void updateUI();

    public UIComponent(Activity parentActivity, View parentView) {
        this.parentView = parentView;
    }

    public Activity getParentActivity() {
        return parentActivity;
    }

    public void setParentActivity(Activity parentActivity) {
        this.parentActivity = parentActivity;
    }

    public View getParentView() {
        return parentView;
    }

    public void setParentView(View parentView) {
        this.parentView = parentView;
    }

}
