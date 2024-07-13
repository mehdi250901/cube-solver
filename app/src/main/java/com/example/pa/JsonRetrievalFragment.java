package com.example.pa;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import java.io.File;

public class JsonRetrievalFragment extends Fragment {

    private static final String TAG = "JsonRetrievalFragment";
    private String bucketName = "rubikscube-results";
    private String accessKey = "AKIAR3YEBCAF2DN3VHMF";
    private String secretKey = "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";
    private String fileName = "resultats_photos_bania.json";
    private String localFilePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize local file path
        localFilePath = getContext().getCacheDir() + "/" + fileName;

        // Download JSON from S3
        downloadJsonFromS3();
    }

    private void downloadJsonFromS3() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3Client s3Client = new AmazonS3Client(credentials);
        TransferUtility transferUtility = TransferUtility.builder()
                .context(getContext())
                .s3Client(s3Client)
                .build();

        File localFile = new File(localFilePath);
        TransferObserver downloadObserver = transferUtility.download(bucketName, fileName, localFile);

        downloadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state == TransferState.COMPLETED) {
                    Log.d(TAG, "Download Complete");
                    Toast.makeText(getContext(), "Download Complete", Toast.LENGTH_SHORT).show();
                    // Proceed to load the JSON into the interface
                    Bundle bundle = new Bundle();
                    bundle.putString("jsonFilePath", localFile.getAbsolutePath());
                    NavHostFragment.findNavController(JsonRetrievalFragment.this)
                            .navigate(R.id.action_jsonRetrievalFragment_to_cubeInterfaceFragment, bundle);
                } else if (state == TransferState.FAILED) {
                    Log.e(TAG, "Download Failed");
                    Toast.makeText(getContext(), "Download Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                Log.d(TAG, "Download progress: " + bytesCurrent + "/" + bytesTotal);
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e(TAG, "Error: " + ex.getMessage(), ex);
                Toast.makeText(getContext(), "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
