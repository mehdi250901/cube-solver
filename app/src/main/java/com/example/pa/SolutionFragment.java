package com.example.pa;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SolutionFragment extends Fragment {

    private String finalBucketName = "rubikscube-final";
    private String accessKey = "AKIAR3YEBCAF2DN3VHMF";  // Remplacez par votre clé d'accès
    private String secretKey = "1CWfmp91sS7k9lt4NU8VOBIOgzBP0NjqGAhvccjv";  // Remplacez par votre clé secrète
    private String solutionFilePath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solution, container, false);

        // Set the image to the ImageView
        ImageView imageView = view.findViewById(R.id.imageViewRubikMoves);
        imageView.setImageResource(R.drawable.rubik_moves); // Assurez-vous que le nom de l'image est correct

        downloadSolutionJson(view);

        return view;
    }

    private void downloadSolutionJson(View view) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3Client s3Client = new AmazonS3Client(credentials);
        TransferUtility transferUtility = TransferUtility.builder().context(getContext()).s3Client(s3Client).build();

        File file = new File(getContext().getCacheDir(), "solution.json");
        solutionFilePath = file.getAbsolutePath();

        TransferObserver downloadObserver = transferUtility.download(finalBucketName, "bravo.json", file);

        downloadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state == TransferState.COMPLETED) {
                    Log.d("Download", "Download Complete");
                    displaySolution(view);
                } else if (state == TransferState.FAILED) {
                    Log.e("Download", "Download Failed");
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                Log.d("Download", "Progress: " + bytesCurrent + "/" + bytesTotal);
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("Download", "Error: " + ex.getMessage(), ex);
            }
        });
    }

    private void displaySolution(View view) {
        try {
            FileReader reader = new FileReader(solutionFilePath);
            char[] buffer = new char[(int) new File(solutionFilePath).length()];
            reader.read(buffer);
            reader.close();
            String solution = new String(buffer);
            TextView solutionText = view.findViewById(R.id.solutionText);
            solutionText.setText(solution);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
