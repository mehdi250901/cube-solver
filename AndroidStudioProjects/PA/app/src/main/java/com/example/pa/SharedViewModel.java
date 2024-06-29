package com.example.pa;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<String>> photoPaths = new MutableLiveData<>(new ArrayList<>());

    public void addPhotoPath(String path) {
        List<String> currentPaths = photoPaths.getValue();
        if (currentPaths != null) {
            currentPaths.add(path);
            photoPaths.setValue(currentPaths);
        }
    }

    public LiveData<List<String>> getPhotoPaths() {
        return photoPaths;
    }
}
