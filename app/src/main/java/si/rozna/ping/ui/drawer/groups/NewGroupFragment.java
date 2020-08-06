package si.rozna.ping.ui.drawer.groups;

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

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import si.rozna.ping.Constants;
import si.rozna.ping.R;
import si.rozna.ping.models.Group;
import si.rozna.ping.models.Mapper;
import si.rozna.ping.models.dto.GroupDTO;
import si.rozna.ping.rest.RestAPI;
import si.rozna.ping.rest.ServiceGenerator;
import si.rozna.ping.ui.MainActivity;
import timber.log.Timber;

public class NewGroupFragment extends Fragment {

    private String groupName;
    private TextView groupNameAdditionalInfo;

    /* REST */
    private RestAPI mRestClient = ServiceGenerator.createService(RestAPI.class);


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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if(user == null) {
            // TODO: Log out user here
            Timber.e("User is not logged in. Logout user here!");
            return;
        }

        Group group = new Group();
        group.setOwnerId(user.getUid());
        group.setName(groupName);

        user.getIdToken(true).addOnCompleteListener(task -> {

            String token = task.getResult().getToken();

            mRestClient.createGroup(Constants.BEARER_TOKEN_PREFIX + token, Mapper.mapGroupToDTO(group)).enqueue(new Callback<GroupDTO>() {
                @Override
                public void onResponse(Call<GroupDTO> call, Response<GroupDTO> response) {

                    if(response.isSuccessful()) {

                        ((MainActivity)getActivity()).hideSoftKeyboard();

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, new GroupsFragment());
                        transaction.commit();


                    }else {
                        // TODO: Tell user smth went wrong
                        Timber.e(response.message());
                    }
                }

                @Override
                public void onFailure(Call<GroupDTO> call, Throwable t) {
                    Timber.e(t);
                }
            });
        }).addOnFailureListener(e -> Timber.i("Failed to retrieve token"));

    }

}