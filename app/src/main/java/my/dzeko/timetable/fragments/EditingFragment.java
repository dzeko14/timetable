package my.dzeko.timetable.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import my.dzeko.timetable.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditingFragment extends Fragment {


    public EditingFragment() {
        // Required empty public constructor
    }

    public static  EditingFragment getInstance() {
        return new EditingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MeLog", "EditingFragment onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("MeLog", "EditingFragment onCreateView");
        return inflater.inflate(R.layout.fragment_editing, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("MeLog", "EditingFragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("MeLog", "EditingFragment onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("MeLog", "EditingFragment onStop");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("MeLog", "EditingFragment onDestroy");
    }
}
