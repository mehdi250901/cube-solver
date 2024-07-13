package com.example.pa;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;
import com.google.gson.Gson;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class CubeInterfaceFragment extends Fragment {

    private char[][][] inputColors;
    private char selectedColor = 'w';
    private String localFilePath;
    private final String finalBucketName = "rubikscube-final";
    private final String accessKey = "AKIAR3YEBCAF2DN3VHMF";
    private final String secretKey = "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";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cube_interface, container, false);

        if (getArguments() != null) {
            localFilePath = getArguments().getString("jsonFilePath");
        }

        loadJsonData();

        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        CubeFacePagerAdapter adapter = new CubeFacePagerAdapter(getContext(), inputColors);
        viewPager.setAdapter(adapter);

        view.findViewById(R.id.buttonWhite).setOnClickListener(v -> {
            selectedColor = 'w';
            adapter.setSelectedColor(selectedColor);
        });
        view.findViewById(R.id.buttonRed).setOnClickListener(v -> {
            selectedColor = 'r';
            adapter.setSelectedColor(selectedColor);
        });
        view.findViewById(R.id.buttonGreen).setOnClickListener(v -> {
            selectedColor = 'g';
            adapter.setSelectedColor(selectedColor);
        });
        view.findViewById(R.id.buttonBlue).setOnClickListener(v -> {
            selectedColor = 'b';
            adapter.setSelectedColor(selectedColor);
        });
        view.findViewById(R.id.buttonOrange).setOnClickListener(v -> {
            selectedColor = 'o';
            adapter.setSelectedColor(selectedColor);
        });
        view.findViewById(R.id.buttonYellow).setOnClickListener(v -> {
            selectedColor = 'y';
            adapter.setSelectedColor(selectedColor);
        });

        Button buttonValidate = view.findViewById(R.id.buttonValidate);
        buttonValidate.setOnClickListener(v -> saveAndUploadJson());

        Button buttonSolution = view.findViewById(R.id.buttonSolution);
        buttonSolution.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("jsonFilePath", localFilePath);
            Navigation.findNavController(v).navigate(R.id.action_cubeInterfaceFragment_to_solutionFragment, bundle);
        });

        return view;
    }

    private void loadJsonData() {
        try {
            FileReader reader = new FileReader(localFilePath);
            inputColors = new Gson().fromJson(reader, char[][][].class);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveAndUploadJson() {
        try {
            // Convert the inputColors array to JSON and save it to a temporary file
            File file = new File(getContext().getCacheDir(), "user_input_colors.json");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(new Gson().toJson(inputColors).getBytes());
            fos.close();

            // Upload the temporary file to S3
            uploadToS3(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadToS3(String filePath) {
        Log.d("CubeInterfaceFragment", "Uploading file: " + filePath);

        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3Client s3Client = new AmazonS3Client(credentials);
        TransferUtility transferUtility = TransferUtility.builder()
                .context(getContext())
                .s3Client(s3Client)
                .build();

        File file = new File(filePath);
        if (!file.exists()) {
            Log.e("CubeInterfaceFragment", "File does not exist: " + filePath);
            return;
        }

        TransferObserver uploadObserver = transferUtility.upload(
                finalBucketName,
                "user_input_colors.json",
                file
        );

        uploadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.d("CubeInterfaceFragment", "TransferState: " + state);
                if (state == TransferState.COMPLETED) {
                    Log.d("CubeInterfaceFragment", "Upload Complete");
                    Toast.makeText(getContext(), "Upload Complete", Toast.LENGTH_SHORT).show();
                } else if (state == TransferState.FAILED) {
                    Log.e("CubeInterfaceFragment", "Upload Failed");
                    Toast.makeText(getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                Log.d("CubeInterfaceFragment", "Upload progress: " + bytesCurrent + "/" + bytesTotal);
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("CubeInterfaceFragment", "Error: " + ex.getMessage(), ex);
                Toast.makeText(getContext(), "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
