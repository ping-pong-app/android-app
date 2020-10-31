package group.pinger.ui.drawer.groups;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;

import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import group.pinger.Constants;
import group.pinger.R;
import group.pinger.auth.AuthService;
import group.pinger.models.api.EntityIdentifierApiModel;
import group.pinger.models.api.GroupApiModel;
import group.pinger.models.mappers.GroupMapper;
import group.pinger.rest.GroupsApi;
import group.pinger.rest.ServiceGenerator;
import group.pinger.ui.MainActivity;
import timber.log.Timber;

public class NewGroupFragment extends Fragment {

    private String groupName;
    private TextView groupNameAdditionalInfo;

    private GroupsViewModel groupsViewModel;

    /* REST */
    private GroupsApi groupsApi = ServiceGenerator.createService(GroupsApi.class);


    public NewGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_new_group, container, false);

        groupsViewModel = new ViewModelProvider(this).get(GroupsViewModel.class);

        TextInputEditText groupNameInput = view.findViewById(R.id.input_new_group_name);
        Button createGroupBtn = view.findViewById(R.id.btn_create_group);
        groupNameAdditionalInfo = view.findViewById(R.id.tv_new_group_additional_info);
        groupNameAdditionalInfo.setTextColor(Color.RED);

        groupNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence == null)
                    groupName = "";
                else
                    groupName = charSequence.toString();

                if(groupName.length() >= Constants.MIN_GROUP_NAME_LENGTH)
                    groupNameAdditionalInfo.setText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        createGroupBtn.setOnClickListener((v) -> {


            if(groupName == null || groupName.length() < Constants.MIN_GROUP_NAME_LENGTH){

                /* Group name is invalid */
                groupNameAdditionalInfo.setText("Group name cannot be empty");

            } else {

                /* Group name is valid */
                groupNameAdditionalInfo.setText("");
                createGroup();
            }
        });

        return view;
    }

    private void createGroup(){

        Optional<FirebaseUser> user = AuthService.getCurrentUser();

        if(!user.isPresent()) {
            // TODO: Log out user here
            Timber.e("User is not logged in. Logout user here!");
            return;
        }

        GroupApiModel group = new GroupApiModel();
        group.setName(groupName);
        group.setOwnerId(user.get().getUid());

        groupsApi.createGroup(group).enqueue(new Callback<EntityIdentifierApiModel>() {
            @Override
            public void onResponse(Call<EntityIdentifierApiModel> call, Response<EntityIdentifierApiModel> response) {
                if(response.isSuccessful()) {

                    ((MainActivity)getActivity()).hideSoftKeyboard();

                    // Add group to DB
                    EntityIdentifierApiModel entity = response.body();
                    if(entity != null) {
                        String id = entity.getId();
                        group.setId(id);
                    }
                    groupsViewModel.addGroup(GroupMapper.toDbModelFromApiModel(group));

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, new GroupsFragment());
                    transaction.commit();

                }else {
                    // TODO: Tell user smth went wrong
                    Timber.e(response.message());
                }
            }

            @Override
            public void onFailure(Call<EntityIdentifierApiModel> call, Throwable t) {
                Timber.e(t);
            }
        });
    }

}