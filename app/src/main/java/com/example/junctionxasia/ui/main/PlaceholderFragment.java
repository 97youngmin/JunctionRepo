package com.example.junctionxasia.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.junctionxasia.R;
import com.example.junctionxasia.SupplierMainActivity;
import com.example.junctionxasia.UploadFood;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

//        View root = inflater.inflate(R.layout.activity_supplier_main, container, false);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        if (index == 1) {
            View frag = inflater.inflate(R.layout.supplier_dashboard_fragment, container, false);
            Button upload = frag.findViewById(R.id.button);
            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadFood();
                }
            });
            return frag;
        }
        else if (index ==2){
            View frag = inflater.inflate(R.layout.uploadfood, container, false);
            return frag;
        }
        else if (index ==3){
            View frag = inflater.inflate(R.layout.activity_supplier_main, container, false);
            return frag;
        }
        else{
            View root = inflater.inflate(R.layout.activity_supplier_main, container, false);
            return root;
        }

//        final TextView textView = root.findViewById(R.id.section_label);
//        pageViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
    }
    public void uploadFood()
    {
        Intent intent = new Intent(getActivity(), UploadFood.class);
        startActivity(intent);
    }
}