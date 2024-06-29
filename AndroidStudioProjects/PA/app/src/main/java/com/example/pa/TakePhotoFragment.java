package com.example.pa;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePhotoFragment extends Fragment {

    private ImageView imageView;
    private Button buttonValidate;
    private Button buttonRetake;
    private String currentPhotoPath;
    private int photoStep;
    private SharedViewModel sharedViewModel;

    private final ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == requireActivity().RESULT_OK) {
                    setPic();
                    sharedViewModel.addPhotoPath(currentPhotoPath);  // Stocke le chemin de la photo dans le ViewModel
                }
            }
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            photoStep = getArguments().getInt("photoStep", 1);
        }
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_take_photo, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.imageView);
        Button buttonTakePhoto = view.findViewById(R.id.buttonTakePhoto);
        buttonValidate = view.findViewById(R.id.buttonValidate);
        buttonRetake = view.findViewById(R.id.buttonRetake);

        buttonTakePhoto.setOnClickListener(v -> dispatchTakePictureIntent());

        buttonValidate.setOnClickListener(v -> {
            if (photoStep < 6) {
                // Naviguer vers le fragment suivant
                Bundle bundle = new Bundle();
                bundle.putInt("photoStep", photoStep + 1);
                NavHostFragment.findNavController(TakePhotoFragment.this)
                        .navigate(R.id.action_takePhotoFragment_to_next, bundle);
            } else {
                // Naviguer vers le résumé
                NavHostFragment.findNavController(TakePhotoFragment.this)
                        .navigate(R.id.action_takePhotoFragment_to_summaryFragment);
            }
        });

        buttonRetake.setOnClickListener(v -> dispatchTakePictureIntent());
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireContext(), "com.example.pa.provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureLauncher.launch(takePictureIntent);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setPic() {
        Uri photoUri = Uri.fromFile(new File(currentPhotoPath));
        imageView.setImageURI(photoUri);
        imageView.setVisibility(View.VISIBLE);

        buttonValidate.setVisibility(View.VISIBLE);
        buttonRetake.setVisibility(View.VISIBLE);
    }
}
