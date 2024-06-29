package com.example.pa;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SummaryFragment extends Fragment {

    private static final String TAG = "SummaryFragment";
    private static final int REQUEST_PERMISSIONS = 123;

    private SharedViewModel sharedViewModel;
    private RecyclerView recyclerView;
    private PhotoAdapter photoAdapter;
    private String bucketName = "rubikscube";
    private String accessKey = "AKIAR3YEBCAF2DN3VHMF";  // Remplacez par votre clé d'accès
    private String secretKey = "1CWfmp91sS7k9lt4NU8VOBIOgzBP0NjqGAhvccjv";  // Remplacez par votre clé secrète
    private String zipFilePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Vérifiez les permissions
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        sharedViewModel.getPhotoPaths().observe(getViewLifecycleOwner(), photoPaths -> {
            photoAdapter = new PhotoAdapter(photoPaths);
            recyclerView.setAdapter(photoAdapter);
        });

        Button buttonBack = view.findViewById(R.id.buttonBack);
        Button buttonZip = view.findViewById(R.id.buttonZip);
        Button buttonUpload = view.findViewById(R.id.buttonUpload);

        buttonBack.setOnClickListener(v -> {
            // Utilisez le NavController pour naviguer vers le fragment précédent
            NavHostFragment.findNavController(SummaryFragment.this).navigateUp();
        });

        buttonZip.setOnClickListener(v -> {
            List<String> photoPaths = sharedViewModel.getPhotoPaths().getValue();
            if (photoPaths != null && !photoPaths.isEmpty()) {
                try {
                    File zipFile = FileUtils.createZipFile(photoPaths, getContext().getCacheDir() + "/photos.zip");
                    zipFilePath = zipFile.getAbsolutePath();
                    Log.d(TAG, "ZIP file created at: " + zipFilePath);
                    Toast.makeText(getContext(), "ZIP file created at: " + zipFilePath, Toast.LENGTH_LONG).show();
                    buttonUpload.setEnabled(true); // Enable the upload button
                } catch (IOException e) {
                    Log.e(TAG, "Error creating zip file: " + e.getMessage(), e);
                    Toast.makeText(getContext(), "Error creating zip file", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "No photos to zip", Toast.LENGTH_SHORT).show();
            }
        });

        buttonUpload.setOnClickListener(v -> {
            if (zipFilePath != null) {
                Log.d(TAG, "Attempting to upload file: " + zipFilePath);
                uploadToS3(zipFilePath);
            } else {
                Toast.makeText(getContext(), "No ZIP file to upload", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permissions granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permissions denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadToS3(String filePath) {
        Log.d(TAG, "Uploading file: " + filePath);

        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3Client s3Client = new AmazonS3Client(credentials);
        TransferUtility transferUtility = TransferUtility.builder()
                .context(getContext())
                .s3Client(s3Client)
                .build();

        File file = new File(filePath);
        if (!file.exists()) {
            Log.e(TAG, "File does not exist: " + filePath);
            Toast.makeText(getContext(), "File does not exist", Toast.LENGTH_SHORT).show();
            return;
        }

        TransferObserver uploadObserver = transferUtility.upload(
                bucketName,
                file.getName(),
                file
        );

        uploadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.d(TAG, "TransferState: " + state);
                if (state == TransferState.COMPLETED) {
                    Log.d(TAG, "Upload Complete");
                    Toast.makeText(getContext(), "Upload Complete", Toast.LENGTH_SHORT).show();
                } else if (state == TransferState.FAILED) {
                    Log.e(TAG, "Upload Failed");
                    Toast.makeText(getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                Log.d(TAG, "Upload progress: " + bytesCurrent + "/" + bytesTotal);
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e(TAG, "Error: " + ex.getMessage(), ex);
                Toast.makeText(getContext(), "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
