package si.rozna.ping.auth;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Optional;

import si.rozna.ping.database.CacheManager;
import si.rozna.ping.ui.drawer.groups.GroupsViewModel;

public class AuthService {

    public static Optional<FirebaseUser> getCurrentUser() {
        return Optional.ofNullable(FirebaseAuth.getInstance().getCurrentUser());
    }

    public static Optional<String> getCurrentUserId(){
        return getCurrentUser().flatMap(user -> {
            if (!user.getUid().isEmpty()) {
                return Optional.of(user.getUid());
            }
            return Optional.empty();
        });
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

    public static void logout(Context context){
        AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener(task -> {
                    CacheManager.clearCache();
                    context.startActivity(new Intent(context, LoginActivity.class));
                });
    }

}
