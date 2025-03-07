package com.applovin.enterprise.apps.demoapp.ads.max.banner

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustAdRevenue
import com.adjust.sdk.AdjustConfig
import com.applovin.enterprise.apps.demoapp.R

import com.applovin.enterprise.apps.demoapp.ui.BaseAdActivity
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdRevenueListener
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.applovin.sdk.AppLovinSdkUtils
import kotlinx.android.synthetic.main.activity_layout_editor_banner_ad.*

/**
 * A [android.app.Activity] to show AppLovin MAX banner ads.
 * <p>
 * Created by Harry Arakkal on 9/17/2019
 */
class ProgrammaticBannerAdActivity : BaseAdActivity(),
        MaxAdViewAdListener, MaxAdRevenueListener {
    private lateinit var adView: MaxAdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_programmatic_banner_ad)
        setTitle(R.string.activity_programmatic_banners)

        setupCallbacksRecyclerView()

        adView = MaxAdView("YOUR_AD_UNIT_ID", this)

        adView.setListener(this)
        adView.setRevenueListener(this)

        val isTablet = AppLovinSdkUtils.isTablet(this)
        val heightPx = AppLovinSdkUtils.dpToPx(this, if (isTablet) 90 else 50)

        adView.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx)
        adView.setBackgroundColor(Color.BLACK)

        val rootView = findViewById<ViewGroup>(android.R.id.content)
        rootView.addView(adView)

        adView.loadAd()
    }

    override fun onDestroy() {
        super.onDestroy()

        bannerAdView.destroy()
    }

    //region MAX Ad Listener

    override fun onAdLoaded(ad: MaxAd?) {
        logCallback()
    }

    override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
        logCallback()
    }

    override fun onAdHidden(ad: MaxAd?) {
        logCallback()
    }

    override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
        logCallback()
    }

    override fun onAdDisplayed(ad: MaxAd?) {
        logCallback()
    }

    override fun onAdClicked(ad: MaxAd?) {
        logCallback()
    }

    override fun onAdExpanded(ad: MaxAd?) {
        logCallback()
    }

    override fun onAdCollapsed(ad: MaxAd?) {
        logCallback()
    }

    //endregion

    //region MAX Ad Revenue Listener

    override fun onAdRevenuePaid(ad: MaxAd?)
    {
        logCallback()

        val adjustAdRevenue = AdjustAdRevenue(AdjustConfig.AD_REVENUE_APPLOVIN_MAX)
        adjustAdRevenue.setRevenue(ad?.revenue, "USD")
        adjustAdRevenue.setAdRevenueNetwork(ad?.networkName)
        adjustAdRevenue.setAdRevenueUnit(ad?.adUnitId)
        adjustAdRevenue.setAdRevenuePlacement(ad?.placement)

        Adjust.trackAdRevenue(adjustAdRevenue)
    }

    //endregion
}
