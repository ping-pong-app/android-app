package si.rozna.ping.ui.drawer.home.components;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import si.rozna.ping.R;
import si.rozna.ping.components.UIComponent;

public class PingButtonUIComponent extends UIComponent {

    private ProgressBar progressBar;
    private FloatingActionButton button;

    // Minimum time that ping button is disabled (so that animation is not done too quickly)
    private static final int TIME_BEFORE_REVERSE_ANIMATION_IN_MILLIS = 3000;

    // Progress bar change to be made per update (in %)
    private static final int PROGRESS_BAR_CHANGE_PER_UPDATE = 4;
    // Progress bar MIN (in %)
    private static final int PROGRESS_BAR_MIN = 0;
    // Progress bar MAX (in %)
    private static final int PROGRESS_BAR_MAX = 100;


    // Is button pressed by user
    private boolean isPingButtonPressed;
    // Is ping action done
    // (when ping action is done button animation has to be reset, before being usable again)
    private boolean isPingButtonReadyToBeReset;
    // Time when ping button was successfully pressed
    private long pingButtonPressedCompletelyAt;

    private int progress;

    // When true ping button has been pressed for long enough to actually ping group
    private boolean isTimeToPing;

    public PingButtonUIComponent(Activity parentActivity, View parentView, int buttonId, int progressBarId) {
        super(parentActivity, parentView);

        button = getParentView().findViewById(buttonId);
        progressBar = getParentView().findViewById(progressBarId);

        init();
    }

    @Override
    protected void init(){

        isTimeToPing = false;
        isPingButtonPressed = false;
        isPingButtonReadyToBeReset = true;

        progress = 0;
        progressBar.setMin(PROGRESS_BAR_MIN);
        progressBar.setMax(PROGRESS_BAR_MAX);
        progressBar.setProgress(progress);

        setUpListeners();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void setUpListeners(){

        button.setOnTouchListener((view1, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(button.isClickable())
                        isPingButtonPressed = true;
                    break;
                case MotionEvent.ACTION_UP:
                    isPingButtonPressed = false;
                    break;
            }
            return true;
        });
    }

    @Override
    public void updateUI(){

        // If ping is being processed, disable button UI updates
        if(isTimeToPing)
            return;

        if (isPingButtonPressed) {

            // Button is pressed & ready to be pressed (in reverse animation button is disabled)...

            if (progress < PROGRESS_BAR_MAX) {

                // Increasing circle to full ring
                progress += PROGRESS_BAR_CHANGE_PER_UPDATE;

            } else {

                // Ping button is pressed for long enough and ping action should be triggered
                // before button is usable again
                isTimeToPing = true;
                isPingButtonReadyToBeReset = false;
                pingButtonPressedCompletelyAt = System.currentTimeMillis();

                // Disable button until ping action is done
                button.setClickable(false);

                // Set ping button color to green
                setButtonColor(R.color.colorGreen);
            }

        } else {

            // Button is not pressed

            if (progress > 0 && isPingButtonReadyToBeReset) {

                // Decreasing circle to empty ring
                progress -= PROGRESS_BAR_CHANGE_PER_UPDATE;

            } else if (progress <= PROGRESS_BAR_MIN) {

                // When progress drops to 0 (after ping), button can be pressed again
                button.setClickable(true);

                // Update UI colors
                setButtonColor(R.color.colorBlue);
            }
        }

        // Holds ping button green (ping successful state) for at least few seconds
        if(System.currentTimeMillis() - pingButtonPressedCompletelyAt > TIME_BEFORE_REVERSE_ANIMATION_IN_MILLIS) {
            isPingButtonReadyToBeReset = true;
        }

        // Sets progress bar's (circle around PING button) progress
        progressBar.setProgress(progress);
    }

    private void  setButtonColor(int color){
        if(getParentActivity() != null)
            button.setBackgroundTintList(ContextCompat.getColorStateList(getParentActivity(), color));
    }

    /* Getters & setters */

    public boolean isTimeToPing() {
        return isTimeToPing;
    }

    public void setTimeToPing(boolean timeToPing) {
        isTimeToPing = timeToPing;
    }


}
