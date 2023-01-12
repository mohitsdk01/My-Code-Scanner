package com.example.mycodescanner.Ads;

import android.content.Context;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class adMob {

    public static void setBannerAd(LinearLayout adContainerView, Context context){

        if(adUnit.showAds){

            MobileAds.initialize(context, initializationStatus -> {});

            AdView adView = new AdView(context);
            adContainerView.addView(adView);

            adView.setAdUnitId(adUnit.BANNER_AD_ID);
            adView.setAdSize(AdSize.BANNER);

            //MobileAds.setRequestConfiguration(new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList(AdRequest.DEVICE_ID_EMULATOR)).build());

            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

        }

    }
}
