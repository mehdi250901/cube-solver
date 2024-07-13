package com.example.pa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonTakePhotos = view.findViewById(R.id.buttonTakePhotos);
        buttonTakePhotos.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_takePhotoFragment)
        );

        Button buttonManualColors = view.findViewById(R.id.buttonManualColors);
        buttonManualColors.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_jsonRetrievalFragment)
        );
    }
}
