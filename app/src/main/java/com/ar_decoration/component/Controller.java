package com.ar_decoration.component;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ar_decoration.activities.OperationActivity;
import com.ar_decoration.activities.R;
import com.pili.pldroid.player.IMediaController;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

public class Controller extends FrameLayout implements IMediaController {

    private IMediaController.MediaPlayerControl m_Player;

    private View m_View;
    private View m_Anchor;
    private SeekBar m_SeekBar;
    private TextView m_TimeNowTV;
    private TextView m_TimeTotalTV;
    private PopupWindow m_PopupWindow;
    private ImageButton m_PauseButton;
    private ImageButton m_CaptureButton;
    private ImageView[] m_ImageViewCacheThree;

    private Context m_Context;
    private Runnable m_LastSeekBarRunnable;
    private Bitmap[] m_BitmapThree;
    private String m_VideoCachePath;
    private MediaMetadataRetriever m_Retriever;

    private static int DEFAULT_TIMEOUT = 3000;
    private static final int SEEK_TO_POST_DELAY_MILLIS = 100;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private static final int UNIT_CONVERSION = 1000;
    private static final int BITMAP_GAP_TIME = 1;

    private long m_Duration;
    private boolean m_InstantSeeking = true;
    private boolean m_Dragging;
    private boolean m_Showing;

    public Controller(Context vContext) {
        super(vContext);
        m_Context = vContext;
        m_PopupWindow = new PopupWindow(m_View);
        m_PopupWindow.setOutsideTouchable(true);
        m_PopupWindow.setBackgroundDrawable(null);
        m_PopupWindow.setFocusable(false);
    }

    public Controller(Context vContext, String vVideoPath, MediaMetadataRetriever vRetriever) {
        super(vContext);
        m_Context = vContext;
        m_VideoCachePath = vVideoPath;
        m_Retriever = vRetriever;
        m_PopupWindow = new PopupWindow(m_View);
        m_PopupWindow.setOutsideTouchable(true);
        m_PopupWindow.setBackgroundDrawable(null);
        m_PopupWindow.setFocusable(false);
    }

    private Handler m_Handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            long progressBarPosition;
            switch (msg.what) {
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_PROGRESS:
                    progressBarPosition = setProgress();
                    if (!m_Dragging && m_Showing) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (progressBarPosition % 1000));
                        if (m_View == null || m_PauseButton == null)
                            return;
                        if (m_Player.isPlaying())
                            m_PauseButton.setImageResource(R.drawable.pause);
                        else
                            m_PauseButton.setImageResource(R.drawable.play);
                    }
                    break;
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        show(DEFAULT_TIMEOUT);
        return true;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(DEFAULT_TIMEOUT);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (event.getRepeatCount() == 0
                && (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                || keyCode == KeyEvent.KEYCODE_SPACE)) {
            if (m_Player.isPlaying()) {
                m_Player.pause();
                m_PauseButton.setImageResource(R.drawable.pause);
            } else if (!m_Player.isPlaying()) {
                m_Player.start();
                m_PauseButton.setImageResource(R.drawable.play);
            }
            show(DEFAULT_TIMEOUT);
            if (m_PauseButton != null)
                m_PauseButton.requestFocus();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP) {
            if (m_Player.isPlaying()) {
                m_Player.pause();
                m_PauseButton.setImageResource(R.drawable.pause);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_MENU) {
            hide();
            return true;
        } else {
            show(DEFAULT_TIMEOUT);
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onFinishInflate() {
        if (m_View != null)
            initControllerView(m_View);
        super.onFinishInflate();
    }

    private void initControllerView(View v) {

        m_PauseButton = (ImageButton) findViewById(R.id.PauseImageButton);
        m_CaptureButton = (ImageButton) findViewById(R.id.CaptureImageButton);
        m_TimeNowTV = (TextView) findViewById(R.id.CurrentTimeTextView);
        m_TimeTotalTV = (TextView) findViewById(R.id.TotalTimeTextView);
        m_SeekBar = (SeekBar) findViewById(R.id.SeekBar);

        m_ImageViewCacheThree = new ImageView[3];
        m_ImageViewCacheThree[0] = (ImageView) findViewById(R.id.CacheImageView0);
        m_ImageViewCacheThree[1] = (ImageView) findViewById(R.id.CacheImageView1);
        m_ImageViewCacheThree[2] = (ImageView) findViewById(R.id.CacheImageView2);

        if (m_PauseButton != null) {
            m_PauseButton.requestFocus();
            m_PauseButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (m_Player.isPlaying()) {
                        m_Player.pause();
                        m_PauseButton.setImageResource(R.drawable.pause);
                    } else {
                        m_Player.start();
                        m_PauseButton.setImageResource(R.drawable.play);
                    }
                    show(DEFAULT_TIMEOUT);
                }
            });
        }

        if (m_CaptureButton != null) {
            m_CaptureButton.requestFocus();
            m_CaptureButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    m_Player.pause();
                    m_PauseButton.setImageResource(R.drawable.pause);
                    AlertDialog.Builder confirmDialog = new AlertDialog.Builder(getContext());
                    confirmDialog.setTitle("截图")
                            .setMessage("是否选取该图像?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Thread thread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            int progress = m_SeekBar.getProgress();
                                            long currentTime = m_Duration * progress;
                                            FileOutputStream fileOutputStream;
                                            String filePath = m_VideoCachePath + File.separator + "frame";
                                            File dirMaker = new File(filePath);
                                            if (!dirMaker.exists())
                                                dirMaker.mkdirs();
                                            try {
                                                String path = filePath + File.separator + currentTime + ".jpg";
                                                fileOutputStream = new FileOutputStream(path);
                                                Bitmap captureBitmap = m_Retriever.getFrameAtTime(currentTime, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                                                captureBitmap = Bitmap.createScaledBitmap(captureBitmap, 1920, 1080, true);
                                                captureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                                                fileOutputStream.close();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    thread.start();
                                    Intent OperateItemIntent = new Intent(getContext(), OperationActivity.class);
                                    getContext().startActivity(OperateItemIntent);
                                }
                            })
                            .setNegativeButton("取消", null);
                    confirmDialog.create();
                    confirmDialog.show();

                    show(DEFAULT_TIMEOUT);
                }
            });
        }


        m_SeekBar.setEnabled(true);
        m_SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser)
                    return;

                final long newPosition = (m_Duration * progress) / UNIT_CONVERSION;
                String time = generateTime(newPosition);
                setBitmaps(newPosition);
                if (m_InstantSeeking) {
                    m_Handler.removeCallbacks(m_LastSeekBarRunnable);
                    m_LastSeekBarRunnable = new Runnable() {
                        @Override
                        public void run() {
                            m_Player.seekTo(newPosition);
                        }
                    };
                    m_Handler.postDelayed(m_LastSeekBarRunnable, SEEK_TO_POST_DELAY_MILLIS);
                }
                if (m_TimeNowTV != null)
                    m_TimeNowTV.setText(time);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                m_Dragging = true;
                show(3600000);
                m_Handler.removeMessages(SHOW_PROGRESS);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!m_InstantSeeking)
                    m_Player.seekTo(m_Duration * seekBar.getProgress() / UNIT_CONVERSION);

                show(DEFAULT_TIMEOUT);
                m_Handler.removeMessages(SHOW_PROGRESS);
                m_Dragging = false;
                m_Handler.sendEmptyMessageDelayed(SHOW_PROGRESS, 500);
            }
        });
    }

    private void setBitmaps(long vNewPosition) {
        long currentTime = vNewPosition / 1000;
        int bitmapNumber;
        if ((currentTime % BITMAP_GAP_TIME) < (BITMAP_GAP_TIME / 2))
            bitmapNumber = (int) (currentTime / BITMAP_GAP_TIME);
        else
            bitmapNumber = ((int) (currentTime / BITMAP_GAP_TIME)) + 1;

        m_BitmapThree = new Bitmap[3];
        m_BitmapThree[0] = BitmapFactory.decodeFile(m_VideoCachePath + File.separator + bitmapNumber + ".jpg");
        m_BitmapThree[1] = BitmapFactory.decodeFile(m_VideoCachePath + File.separator + (bitmapNumber - 1) + ".jpg");
        m_BitmapThree[2] = BitmapFactory.decodeFile(m_VideoCachePath + File.separator + (bitmapNumber + 1) + ".jpg");
        m_ImageViewCacheThree[0].setImageBitmap(m_BitmapThree[0]);
        m_ImageViewCacheThree[1].setImageBitmap(m_BitmapThree[1]);
        m_ImageViewCacheThree[2].setImageBitmap(m_BitmapThree[2]);
    }

    private static String generateTime(long vPosition) {
        int totalSeconds = (int) (vPosition / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds).toString();
        }
    }


    private long setProgress() {
        if (m_Player == null || m_Dragging)
            return 0;

        long currentPosition = m_Player.getCurrentPosition();
        long duration = m_Player.getDuration();
        if (m_SeekBar != null) {
            if (duration > 0) {
                long progress = currentPosition * 1000 / duration;
                m_SeekBar.setProgress((int) progress);
            }
            int percent = m_Player.getBufferPercentage();
            m_SeekBar.setSecondaryProgress(percent * 10);
        }

        m_Duration = duration;

        if (m_TimeTotalTV != null)
            m_TimeTotalTV.setText(generateTime(m_Duration));
        if (m_TimeNowTV != null)
            m_TimeNowTV.setText(generateTime(currentPosition));

        return currentPosition;
    }

    @Override
    public void setMediaPlayer(MediaPlayerControl vMediaPlayerControl) {
        m_Player = vMediaPlayerControl;
        if (m_View == null || m_PauseButton == null) return;
        if (m_Player.isPlaying())
            m_PauseButton.setImageResource(R.drawable.pause);
        else
            m_PauseButton.setImageResource(R.drawable.play);
    }

    @Override
    public void show() {
        show(DEFAULT_TIMEOUT);
    }

    @Override
    public void show(int vTimeout) {
        if (!m_Showing) {
            if (m_Anchor != null && m_Anchor.getWindowToken() != null) {
                m_Anchor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
            if (m_PauseButton != null)
                m_PauseButton.requestFocus();
            try {
                if (m_PauseButton != null && !m_Player.canPause())
                    m_PauseButton.setEnabled(false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            int[] location = new int[2];

            if (m_Anchor != null) {
                m_Anchor.getLocationOnScreen(location);
                Rect anchorRect = new Rect(location[0], location[1],
                        location[0] + m_Anchor.getWidth(), location[1]
                        + m_Anchor.getHeight());

                m_PopupWindow.showAtLocation(m_Anchor, Gravity.BOTTOM, anchorRect.left, 0);
            }
            m_Showing = true;
        }
        if (m_View == null || m_PauseButton == null)
            return;
        if (m_Player.isPlaying())
            m_PauseButton.setImageResource(R.drawable.pause);
        else
            m_PauseButton.setImageResource(R.drawable.play);

        m_Handler.sendEmptyMessage(SHOW_PROGRESS);

        if (vTimeout != 0) {
            m_Handler.removeMessages(FADE_OUT);
            m_Handler.sendMessageDelayed(m_Handler.obtainMessage(FADE_OUT), vTimeout);
        }

        m_ImageViewCacheThree[0].setImageBitmap(null);
        m_ImageViewCacheThree[1].setImageBitmap(null);
        m_ImageViewCacheThree[2].setImageBitmap(null);
    }

    @Override
    public void hide() {
        if (m_Showing) {
            try {
                m_Handler.removeMessages(SHOW_PROGRESS);
                m_PopupWindow.dismiss();
            } catch (IllegalArgumentException ex) {
            }
            m_Showing = false;
        }
    }

    @Override
    public boolean isShowing() {
        return m_Showing;
    }

    @Override
    public void setAnchorView(View view) {
        m_Anchor = view;
        if (m_Anchor == null) {
            DEFAULT_TIMEOUT = 0;
        }
        removeAllViews();
        m_View = ((LayoutInflater) m_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.window_preview, this);
        m_PopupWindow.setContentView(m_View);
        m_PopupWindow.setWidth(LayoutParams.MATCH_PARENT);
        m_PopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
        initControllerView(m_View);
    }
}