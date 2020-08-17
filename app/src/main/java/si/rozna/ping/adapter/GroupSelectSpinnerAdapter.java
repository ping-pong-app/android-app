package si.rozna.ping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import si.rozna.ping.R;
import si.rozna.ping.models.Group;

public class GroupSelectSpinnerAdapter extends BaseAdapter {

    private List<Group> groups;

    public GroupSelectSpinnerAdapter() {
        this.groups = new ArrayList<>();
    }

    public GroupSelectSpinnerAdapter(List<Group> groups){
        this.groups = groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Group getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(view == null){
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.layout_group_select_spinner_item, viewGroup, false);
        }

        // TODO: ImageView groupImage = ...

        Group selectedGroup = getItem(position);
        TextView groupName = view.findViewById(R.id.group_select_name);
        groupName.setText(selectedGroup.getName());

        return view;
    }
}
