package com.example.streamingvideoapp

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import kotlinx.android.synthetic.main.activity_playing.*

class PlayingActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var player: SimpleExoPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playing)
        start.setOnClickListener(this)
        stop.setOnClickListener(this)
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)
        playerView.player = player
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.start -> {
                val addressText = address.editText?.text.toString()
                val appText = app.editText?.text.toString()
                val keyText = key.editText?.text.toString()
                if (addressText.isBlank()) address.error = getString(R.string.address_error)
                if (appText.isBlank()) app.error = getString(R.string.app_error)
                val uri = Uri.parse(String.format(getString(R.string.uri_format), addressText, appText, keyText))
                startStreaming(uri)
            }
            R.id.stop -> {
                player.stop()
            }
        }
    }

    private fun startStreaming(uri: Uri?) {
        val rtmpDataSourceFactory = RtmpDataSourceFactory()
        val mediaSource = ExtractorMediaSource.Factory(rtmpDataSourceFactory)
            .createMediaSource(uri)
        player.prepare(mediaSource)
        player.playWhenReady = true
    }
}
