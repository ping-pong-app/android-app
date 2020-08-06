package si.rozna.ping.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Authentication {

    private static FirebaseUser getLoggedInUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private static void getLoggedInUserToken() {
        getLoggedInUser().getIdToken(true).addOnCompleteListener(task -> {
            String token = task.getResult().getToken();
        });
    }




}
