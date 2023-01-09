package com.example.mycodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

public class MainActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        textView = findViewById(R.id.text_code);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.CAMERA
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {

            mCodeScanner = new CodeScanner(this, scannerView);
            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull final Result result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(MainActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                            String data = result.getText();
//                            if(URLUtil.isValidUrl(data)){
//                                outputClick(View );
//                            }else {
                                textView.setText(data);
//                            }
                        }
                    });
                }
            });
            scannerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCodeScanner.startPreview();
                }
            });
        }
    }

//    private void runScanner() {
//            mCodeScanner = new CodeScanner(this, scannerView);
//            mCodeScanner.setDecodeCallback(new DecodeCallback() {
//                @Override
//                public void onDecoded(@NonNull final Result result) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            //Toast.makeText(MainActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
//                            String data = result.getText();
//                            textView.setText(data);
//                        }
//                    });
//                }
//            });
//            scannerView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mCodeScanner.startPreview();
//                }
//            });
//        }
//    }

    public static boolean hasPermissions(Context context, String... permissions){
        if(context != null && permissions != null){
            for(String permission:permissions){
                if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }


}