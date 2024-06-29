package com.example.pa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class SummaryFragment extends Fragment {

    private ImageView[] imageViews = new ImageView[6];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        GridLayout gridLayout = view.findViewById(R.id.gridLayout);
        imageViews[0] = view.findViewById(R.id.imageFace1);
        imageViews[1] = view.findViewById(R.id.imageFace2);
        imageViews[2] = view.findViewById(R.id.imageFace3);
        imageViews[3] = view.findViewById(R.id.imageFace4);
        imageViews[4] = view.findViewById(R.id.imageFace5);
        imageViews[5] = view.findViewById(R.id.imageFace6);

        Button buttonUpload = view.findViewById(R.id.buttonUpload);
        Button buttonPrevious = view.findViewById(R.id.buttonPrevious);

        buttonUpload.setOnClickListener(v -> {
            // Code pour envoyer les photos sur AWS
        });

        buttonPrevious.setOnClickListener(v -> {
            NavHostFragment.findNavController(SummaryFragment.this)
                    .navigateUp();
        });

        return view;
    }
}
