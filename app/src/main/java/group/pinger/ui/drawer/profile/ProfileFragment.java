package group.pinger.ui.drawer.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import group.pinger.R;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        /*FirebaseUser user = AuthService.getCurrentUser().orElseThrow(() -> new RuntimeException("User is not logged in!"));

        UserProfileChangeRequest req = new UserProfileChangeRequest.Builder()
                .setDisplayName("New Display name")
                .setPhotoUri(Uri.parse("url to image - remove if not present"))
                .build();

        user.updateProfile(req).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

            }
        }).addOnFailureListener(e -> {

        });
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}
