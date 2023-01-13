package com.example.mycodescanner.Ads;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class adMob {

    static onDismiss onDismiss; // static variable for dismiss.

    // Dismiss inteface called here (It is used when user dismiss the Ad).
    public adMob(onDismiss onDismiss){
        this.onDismiss = onDismiss;
    }

    // empty function adMob
    public adMob(){

    }

    // for showing the banner Ad in activity
    public static void setBannerAd(LinearLayout adContainerView, Context context){

        if(adUnit.showAds){

            MobileAds.initialize(context, initializationStatus -> {});

            AdView adView = new AdView(context);
            adContainerView.addView(adView);

            adView.setAdUnitId(adUnit.TEST_BANNER_AD_ID); // pass here bannerAd Id (it is for testing or for real use).
            adView.setAdSize(AdSize.BANNER);

            //MobileAds.setRequestConfiguration(new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList(AdRequest.DEVICE_ID_EMULATOR)).build());

            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

        }
    }

    // loadInt function for Interstitial Ad
    public static void loadInt(Context context){

        if(adUnit.showAds) {

            MobileAds.initialize(context, initializationStatus -> {
            });

            AdRequest adRequest = new AdRequest.Builder().build(); // Request for Ad

            // Ad Load method (for loading the add) Always call at first activity of the application
            InterstitialAd.load(context,adUnit.TEST_INTERSTITIAL_AD_ID, adRequest, // pass here InterstitialAd Id (it is for testing or for real use).
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            adUnit.mInterstitialAd = interstitialAd;
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            adUnit.mInterstitialAd = null;
                        }
                    });
        }
    }

    // Interstitial add shower function (It takes always activity as context).
    public void showIntCall(Activity activity, boolean isReload){

        if (adUnit.mInterstitialAd != null) {
            adUnit.mInterstitialAd.show(activity);

            // setting on full screen callback function
            adUnit.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();

                    if(isReload){
                        adUnit.mInterstitialAd = null;
                        adMob.loadInt(activity);
                    }
                    onDismiss.onDissmiss();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    onDismiss.onDissmiss();
                }
            });

        } else {
            onDismiss.onDissmiss();

        }

    }

}
