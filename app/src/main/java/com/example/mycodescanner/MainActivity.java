package com.example.mycodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;
import com.google.zxing.client.result.EmailAddressParsedResult;

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
//                            Toast.makeText(MainActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                            String data = result.getText();
                            if(Patterns.WEB_URL.matcher(data).matches()){
                                Intent browserIntent =new Intent(Intent.ACTION_VIEW, Uri.parse(data));
                                startActivity(browserIntent);
                            }

                            else if(Patterns.EMAIL_ADDRESS.matcher(data).matches()){

                                textView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //Uri emailAddress = Uri.parse(data);
                                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                                        emailIntent.setData(Uri.parse("mailto:"));
                                        startActivity(emailIntent);
                                    }
                                });
                            }

//                            else if(Patterns.PHONE.matcher(data).matches()){
//                                Uri phone = Uri.parse(data);
//                                Intent phone = new Intent(Intent.EXTRA_PHONE_NUMBER)
//                            }

                            else {
                                textView.setText(data);
                            }
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