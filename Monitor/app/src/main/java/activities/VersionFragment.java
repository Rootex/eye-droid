package activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Server.camerapreview.R;

/**
 * Created by plaix on 3/4/16.
 */
public class VersionFragment extends Fragment {
    public VersionFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_version, container, false);

        String name = getArguments().getString("name");

        TextView textView =  (TextView) rootView.findViewById(R.id.textView);
        textView.setText("Android version " + name + " is selected");
        getActivity().setTitle(name);
        return rootView;
    }
}
