package com.example.mycodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.mycodescanner.Ads.adMob;
import com.google.zxing.Result;
import com.google.zxing.client.result.EmailAddressParsedResult;

public class MainActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private TextView textView;
    RelativeLayout icon_buttons_relative_layout;
    ImageView copyBtn;
    ImageView shareBtn;
    ImageView openExteranalLink;
    TextView open_external_link_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toast toast = Toast.makeText(getApplicationContext(), "Note: Click on scanner camera for new scan when once scan done.", Toast.LENGTH_LONG);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(toast::cancel, 10000); // Toast Handler


        // ActionBar settings
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>QR Scanner</font>"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.yellow)));

        // finding ids from xml activity for connection
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        icon_buttons_relative_layout = findViewById(R.id.icon_buttons_relative_layout);
        textView = findViewById(R.id.text_code);
        copyBtn= findViewById(R.id.copy_button);
        shareBtn = findViewById(R.id.share_text_button);
        openExteranalLink = findViewById(R.id.open_external_link);
        open_external_link_text = findViewById(R.id.open_external_link_text);

        // for making object copying the result text on clipboard
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.CAMERA
        };

        // Calling advertisement banner...
        adMob.setBannerAd(findViewById(R.id.banner),this);

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
//                          Toast.makeText(MainActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                            String data = result.getText();
                            icon_buttons_relative_layout.setVisibility(View.VISIBLE);
                            openExteranalLink.setVisibility(View.INVISIBLE);
                            open_external_link_text.setVisibility(View.INVISIBLE);


                            if(Patterns.WEB_URL.matcher(data).matches()){
                                textView.setText(data);
                                openExteranalLink.setVisibility(View.VISIBLE);
                                open_external_link_text.setVisibility(View.VISIBLE);
                                openExteranalLink.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent browserIntent =new Intent(Intent.ACTION_VIEW, Uri.parse(data));
                                        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        //browserIntent.setPackage("com.android.chrome");
                                        startActivity(browserIntent);
                                    }
                                });
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

                            // for copying the result text on clipboard.
                            copyBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ClipData clipdata = ClipData.newPlainText("label", data);
                                    clipboardManager.setPrimaryClip(clipdata);
                                    Toast.makeText(MainActivity.this, "Text Copied", Toast.LENGTH_SHORT).show();
                                }
                            });

                            // Share button implementation
                            shareBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent shareText = new Intent(Intent.ACTION_SEND);
                                    String shareResultText = data;
                                    shareText.setType("text/plain");
                                    shareText.putExtra(Intent.EXTRA_TEXT, shareResultText);
                                    startActivity(Intent.createChooser(shareText, "Share"));
                                }
                            });

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





    // All external functions (Non Main)
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