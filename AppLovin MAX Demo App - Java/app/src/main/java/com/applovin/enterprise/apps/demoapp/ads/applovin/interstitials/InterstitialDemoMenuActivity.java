package com.applovin.enterprise.apps.demoapp.ads.applovin.interstitials;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.adview.AppLovinInterstitialAdDialog;
import com.applovin.enterprise.apps.demoapp.ads.applovin.AdStatusActivity;
import com.applovin.enterprise.apps.demoapp.ads.applovin.DemoMenuActivity;
import com.applovin.enterprise.apps.demoapp.ads.applovin.DemoMenuItem;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdClickListener;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinAdVideoPlaybackListener;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.enterprise.apps.demoapp.R;

public class InterstitialDemoMenuActivity
        extends DemoMenuActivity
{
    @Override
    protected DemoMenuItem[] getListViewContents()
    {
        DemoMenuItem[] result = {
                new DemoMenuItem( "Basic Integration", "Quick interstitial ads integration", new Intent( this, InterstitialBasicIntegrationActivity.class ) ),
                new DemoMenuItem( "Manually loading ad", "Use this for greater control over the ad load process", new Intent( this, InterstitialManualLoadingActivity.class ) ),
                new DemoMenuItem( "Zone Integration", "Create different user experiences of the same ad type", new Intent( this, InterstitialZoneActivity.class ) )
        };
        return result;
    }

    /**
     * Created by thomasso on 10/5/15.
     */

    public static class InterstitialManualLoadingActivity
            extends AdStatusActivity
    {
        private AppLovinAd currentAd;
        private AppLovinInterstitialAdDialog interstitialAd;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate( savedInstanceState );
            setContentView( R.layout.activity_interstitial_manual_loading );

            adStatusTextView = findViewById( R.id.status_label );

            interstitialAd = AppLovinInterstitialAd.create( AppLovinSdk.getInstance( this ), this );

            final Button loadButton = findViewById( R.id.loadButton );
            final Button showButton = findViewById( R.id.showButton );

            loadButton.setOnClickListener( new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    log( "Interstitial loading..." );
                    showButton.setEnabled( false );

                    AppLovinSdk.getInstance( getApplicationContext() ).getAdService().loadNextAd( AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener()
                    {
                        @Override
                        public void adReceived(AppLovinAd ad)
                        {
                            log( "Interstitial Loaded" );

                            currentAd = ad;

                            runOnUiThread( new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    showButton.setEnabled( true );
                                }
                            } );
                        }

                        @Override
                        public void failedToReceiveAd(int errorCode)
                        {
                            // Look at AppLovinErrorCodes.java for list of error codes
                            log( "Interstitial failed to load with error code " + errorCode );
                        }
                    } );
                }
            } );

            showButton.setOnClickListener( new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if ( currentAd != null )
                    {
                        interstitialAd.showAndRender( currentAd );
                    }
                }
            } );

            //
            // Optional: Set ad display, ad click, and ad video playback callback listeners
            //
            interstitialAd.setAdDisplayListener( new AppLovinAdDisplayListener()
            {
                @Override
                public void adDisplayed(AppLovinAd appLovinAd)
                {
                    log( "Interstitial Displayed" );
                }

                @Override
                public void adHidden(AppLovinAd appLovinAd)
                {
                    log( "Interstitial Hidden" );
                }
            } );

            interstitialAd.setAdClickListener( new AppLovinAdClickListener()
            {
                @Override
                public void adClicked(AppLovinAd appLovinAd)
                {
                    log( "Interstitial Clicked" );
                }
            } );

            // This will only ever be used if you have video ads enabled.
            interstitialAd.setAdVideoPlaybackListener( new AppLovinAdVideoPlaybackListener()
            {
                @Override
                public void videoPlaybackBegan(AppLovinAd appLovinAd)
                {
                    log( "Video Started" );
                }

                @Override
                public void videoPlaybackEnded(AppLovinAd appLovinAd, double percentViewed, boolean wasFullyViewed)
                {
                    log( "Video Ended" );
                }
            } );
        }
    }
}