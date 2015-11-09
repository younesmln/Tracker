package com.pfe.projet.tracker;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;


public class MainFragment extends Fragment {
    private final String TAG = MainFragment.class.getSimpleName();
    public FragmentWorker fragmentWorker;
    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        return v;
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
//        if (a instanceof FragmentWorker) {
//            fragmentWorker = (FragmentWorker) a;
//        }else{
//            throw new ClassCastException(a.toString() + "must implement FragmentWorker");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentWorker = null;
    }

    public interface FragmentWorker {
        public void doStuff();
    }
}
