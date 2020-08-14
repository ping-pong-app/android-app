package si.rozna.ping.ui.drawer.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;

import si.rozna.ping.R;
import si.rozna.ping.ui.components.PingButtonUIComponent;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;


    /* UI components*/
    public PingButtonUIComponent pingButtonComponent;

    /* UI handler */
    private final Handler pingButtonHandler = new UIHandler(this);
    private final static int MSG_UPDATE_PING_BUTTON = 1234567;
    private final static int UPDATE_RATE_MS_PING_BUTTON = 10;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Bind ViewModel to fragment
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        mViewModel.getTest().observe(this, test -> { /* code goes here */ });

    }

    @Override
    public void onStart() {
        super.onStart();
        pingButtonHandler.sendEmptyMessage(MSG_UPDATE_PING_BUTTON);
    }

    @Override
    public void onStop() {
        super.onStop();
        pingButtonHandler.removeMessages(MSG_UPDATE_PING_BUTTON);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        pingButtonComponent = new PingButtonUIComponent(getActivity(), view, R.id.ping_button, R.id.ping_progress_bar);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        pingButtonComponent.setParentActivity(getActivity());
    }

    /* UI */

    static class UIHandler extends Handler {

        private final WeakReference<HomeFragment> fragment;

        UIHandler(HomeFragment service) {
            this.fragment = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message message) {
            if (MSG_UPDATE_PING_BUTTON == message.what) {
                fragment.get().updatePingButtonUI();
                sendEmptyMessageDelayed(MSG_UPDATE_PING_BUTTON, UPDATE_RATE_MS_PING_BUTTON);
            }
        }
    }

    public void updatePingButtonUI(){

        // TODO: Ping action triggered more than once!!

        if(pingButtonComponent.isTimeToPing()){
            pingButtonComponent.setTimeToPing(false);
            Snackbar.make(getView(), "do ping in background", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        }

        pingButtonComponent.updateUI();
    }

}
