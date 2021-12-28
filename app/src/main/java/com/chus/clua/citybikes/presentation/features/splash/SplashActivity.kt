package com.chus.clua.citybikes.presentation.features.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chus.clua.citybikes.databinding.ActivitySplashBinding
import com.chus.clua.citybikes.presentation.features.networkslist.NetWorksListActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimation()

    }

    private fun startAnimation() {
        binding.splashView.animate()
            .alpha(0f)
            .alphaBy(1f)
            .setDuration(3000)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    NetWorksListActivity.start(this@SplashActivity)
                }
            })
    }
}
