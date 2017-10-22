package com.ar_decoration.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ar_decoration.component.Controller;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;

import java.io.File;
import java.io.FileOutputStream;

public class VideoPlayerActivity extends Activity {

    private static String TAG = "VideoPlayerActivity";

    private PLVideoView m_VideoView;
    private Toast m_Toast = null;
    private int m_DisplayAspectRatio = PLVideoView.ASPECT_RATIO_PAVED_PARENT;
    private String m_VideoPath;
    private String m_VideoCachePath;
    private String m_VideoName;
    private ImageButton m_DisplayChangeButton;
    private MediaMetadataRetriever m_Retriever;

    private static final int PREPARE_TIMEOUT = 10 * 1000;
    private static final int BITMAP_GAP_TIME = 1;
    private static final int DISPLAY_RATIO_NUMBERS = 5;
    private static final int UNIT_CONVERSION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_VideoPath = getIntent().getStringExtra("videoPath");
        m_Retriever = new MediaMetadataRetriever();
        m_Retriever.setDataSource(m_VideoPath);
        m_VideoName = m_VideoPath.substring(m_VideoPath.lastIndexOf("/") + 1, m_VideoPath.lastIndexOf("."));
        Thread saveBitmapTask = new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                Log.e("startTime", "" + startTime);
                getBitmapsFromVideo();
                Log.e("finishTime", "" + (System.currentTimeMillis() - startTime));
            }
        });
        saveBitmapTask.start();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_video_player);
        m_VideoView = (PLVideoView) findViewById(R.id.VideoView);

        View loadingView = findViewById(R.id.LoadingView);
        loadingView.setVisibility(View.VISIBLE);
        m_VideoView.setBufferingIndicator(loadingView);

        // 1 -> hardware codec enable, 0 -> disable [recommended]
        int codec = getIntent().getIntExtra("mediaCodec", AVOptions.MEDIA_CODEC_SW_DECODE);
        AVOptions options = new AVOptions();
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, PREPARE_TIMEOUT);
        options.setInteger(AVOptions.KEY_MEDIACODEC, codec);

        m_VideoView.setAVOptions(options);
        m_VideoView.setDebugLoggingEnabled(true);
        m_VideoView.setDisplayAspectRatio(m_DisplayAspectRatio);

        m_VideoView.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
        m_VideoView.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        m_VideoView.setOnCompletionListener(mOnCompletionListener);
        m_VideoView.setOnErrorListener(mOnErrorListener);
        m_VideoView.setOnVideoFrameListener(mOnVideoFrameListener);
        m_VideoView.setOnAudioFrameListener(mOnAudioFrameListener);

        m_DisplayChangeButton = (ImageButton) findViewById(R.id.DisplayChangeButton);
        m_DisplayChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_DisplayAspectRatio = (m_DisplayAspectRatio + 1) % DISPLAY_RATIO_NUMBERS;
                m_VideoView.setDisplayAspectRatio(m_DisplayAspectRatio);
                switch (m_VideoView.getDisplayAspectRatio()) {
                    case PLVideoView.ASPECT_RATIO_ORIGIN:
                        showToastTips("Origin mode");
                        break;
                    case PLVideoView.ASPECT_RATIO_FIT_PARENT:
                        showToastTips("Fit parent !");
                        break;
                    case PLVideoView.ASPECT_RATIO_PAVED_PARENT:
                        showToastTips("Paved parent !");
                        break;
                    case PLVideoView.ASPECT_RATIO_16_9:
                        showToastTips("16 : 9 !");
                        break;
                    case PLVideoView.ASPECT_RATIO_4_3:
                        showToastTips("4 : 3 !");
                        break;
                    default:
                        break;
                }
            }
        });

        m_VideoView.setVideoPath(m_VideoPath);
        m_VideoCachePath = m_VideoPath.substring(0, m_VideoPath.lastIndexOf("/") + 1) + "/videocache/" + m_VideoName;
        Controller mediaController = new Controller(this, m_VideoCachePath, m_Retriever);
        m_VideoView.setMediaController(mediaController);
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_VideoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        m_Toast = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        m_VideoView.stopPlayback();
    }

    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(PLMediaPlayer mp, int errorCode) {
            Log.e(TAG, "Error happened, errorCode = " + errorCode);
            switch (errorCode) {
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                    Log.e(TAG, "IO Error!");
                    return false;
                case PLMediaPlayer.ERROR_CODE_OPEN_FAILED:
                    showToastTips("failed to open player !");
                    break;
                case PLMediaPlayer.ERROR_CODE_SEEK_FAILED:
                    showToastTips("failed to seek !");
                    break;
                default:
                    showToastTips("unknown error !");
                    break;
            }
            finish();
            return true;
        }
    };

    private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(PLMediaPlayer plMediaPlayer) {
            Log.i(TAG, "Play Completed !");
            showToastTips("Play Completed !");
            finish();
        }
    };

    private PLMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new PLMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(PLMediaPlayer plMediaPlayer, int vPercent) {
            Log.i(TAG, "onBufferingUpdate: " + vPercent);
        }
    };

    private PLMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener = new PLMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int width, int height) {
            Log.i(TAG, "onVideoSizeChanged: width = " + width + ", height = " + height);
        }
    };

    private PLMediaPlayer.OnVideoFrameListener mOnVideoFrameListener = new PLMediaPlayer.OnVideoFrameListener() {
        @Override
        public void onVideoFrameAvailable(byte[] data, int size, int width, int height, int format, long ts) {
            Log.i(TAG, "onVideoFrameAvailable: " + size + ", " + width + " x " + height + ", " + format + ", " + ts);
        }
    };

    private PLMediaPlayer.OnAudioFrameListener mOnAudioFrameListener = new PLMediaPlayer.OnAudioFrameListener() {
        @Override
        public void onAudioFrameAvailable(byte[] data, int size, int samplerate, int channels, int datawidth, long ts) {
            Log.i(TAG, "onAudioFrameAvailable: " + size + ", " + samplerate + ", " + channels + ", " + datawidth + ", " + ts);
        }
    };

    private void showToastTips(final String tips) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (m_Toast != null) {
                    m_Toast.cancel();
                }
                m_Toast = Toast.makeText(VideoPlayerActivity.this, tips, Toast.LENGTH_SHORT);
                m_Toast.show();
            }
        });
    }

    public void getBitmapsFromVideo() {
        int totalTime = Integer.valueOf(m_Retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / UNIT_CONVERSION;
        int totalBitmap = totalTime / BITMAP_GAP_TIME;
        long currentTime = 0;

        Bitmap bitmap[] = new Bitmap[totalBitmap];
        FileOutputStream fileOutputStream;
        m_VideoCachePath = m_VideoPath.substring(0, m_VideoPath.lastIndexOf("/") + 1) + "/videocache/" + m_VideoName;
        File makeDir = new File(m_VideoCachePath);
        if (makeDir.exists()) {
            showToastTips("Found cache.");
            return;
        } else {
            if (makeDir.mkdirs()) {
                showToastTips("Begin to cache...");
            } else
                showToastTips("Make directory failed.");
        }
        int i;
        for (i = 0; i < totalBitmap; i++) {
            long t = currentTime * UNIT_CONVERSION * UNIT_CONVERSION;
            bitmap[i] = m_Retriever.getFrameAtTime(t, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            bitmap[i] = Bitmap.createScaledBitmap(bitmap[i], 800, 450, true);
            currentTime += BITMAP_GAP_TIME;
        }
        try {
            for (i = 0; i < totalBitmap; i++) {
                String bitmapPath = m_VideoCachePath + File.separator + i + ".jpg";
                fileOutputStream = new FileOutputStream(bitmapPath);
                bitmap[i].compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            showToastTips("Done!");
        }
    }
}
