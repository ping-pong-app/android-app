package si.rozna.ping.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Optional;

public class FirebaseService {

    public static Optional<FirebaseUser> getCurrentUser() {
        return Optional.ofNullable(FirebaseAuth.getInstance().getCurrentUser());
    }

    public static Optional<String> getCurrentUserEmail() {
        return getCurrentUser().flatMap(user -> {
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                return Optional.of(user.getEmail());
            }
            return Optional.empty();
        });
    }

    public static Optional<String> getCurrentUserDisplayName() {
        return getCurrentUser().flatMap(user -> {
            if (user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
                return Optional.of(user.getDisplayName());
            }
            return Optional.empty();
        });
    }

}
