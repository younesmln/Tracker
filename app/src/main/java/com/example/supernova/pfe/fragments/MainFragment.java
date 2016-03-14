package com.example.supernova.pfe.fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.supernova.pfe.R;


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
    public void onAttach(Context a) {
        super.onAttach(a);
        if (a instanceof FragmentWorker) {
            fragmentWorker = (FragmentWorker) a;
        }else{
            throw new ClassCastException(a.toString() + "must implement FragmentWorker");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentWorker = null;
    }

    public interface FragmentWorker {
        void doStuff();
    }
}
