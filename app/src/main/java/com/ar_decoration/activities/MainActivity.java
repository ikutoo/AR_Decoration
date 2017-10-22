package com.ar_decoration.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ar_decoration.function.GetPathFromUri;
import com.pili.pldroid.player.AVOptions;

public class MainActivity extends Activity {

    private EditText m_VideoPathEditText;
    private RadioGroup m_DecodeTypeRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        m_VideoPathEditText    = (EditText) findViewById(R.id.VideoPathEditText);
        m_DecodeTypeRadioGroup = (RadioGroup) findViewById(R.id.DecodeTypeRadioGroup);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onClickLocalFileButton(View v) {

        Intent localFileIntent = new Intent();

        if (Build.VERSION.SDK_INT < 19) {
            localFileIntent.setAction(Intent.ACTION_GET_CONTENT);
            localFileIntent.setType("video/*");
        }
        else {
            localFileIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            localFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
            localFileIntent.setType("video/*");
        }
        startActivityForResult(Intent.createChooser(localFileIntent, "选择要导入的视频"), 0);
    }

    public void onClickPlayButton(View v) {
        String videoPath = m_VideoPathEditText.getText().toString();

        if (!"".equals(videoPath)) {
            Intent videoPlayerIntent = new Intent(MainActivity.this, VideoPlayerActivity.class);
            videoPlayerIntent.putExtra("videoPath", videoPath);

            if (m_DecodeTypeRadioGroup.getCheckedRadioButtonId() == R.id.RadioHWDecode) {
                videoPlayerIntent.putExtra("mediaCodec", AVOptions.MEDIA_CODEC_HW_DECODE);
            }
            else if (m_DecodeTypeRadioGroup.getCheckedRadioButtonId() == R.id.RadioSWDecode) {
                videoPlayerIntent.putExtra("mediaCodec", AVOptions.MEDIA_CODEC_SW_DECODE);
            }
            else {
                videoPlayerIntent.putExtra("mediaCodec", AVOptions.MEDIA_CODEC_AUTO);
            }

            startActivity(videoPlayerIntent);
        }
    }

    @Override
    protected void onActivityResult(int vRequestCode, int vResultCode, Intent vIntent) {
        if (vResultCode != Activity.RESULT_OK) {
            return;
        }

        if (vRequestCode == 0) {
            String selectedFilePath = GetPathFromUri.getPath(this, vIntent.getData());
            if (selectedFilePath != null && !"".equals(selectedFilePath)) {
                m_VideoPathEditText.setText(selectedFilePath, TextView.BufferType.EDITABLE);
            }
        }
    }
}
