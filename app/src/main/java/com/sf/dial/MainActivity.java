package com.sf.dial;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.widget.SeekBar;

import com.sf.dial.widget.DialProgressBar;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DialProgressBar bar = (DialProgressBar) findViewById(R.id.progressBar);

        AppCompatSeekBar seekBar = (AppCompatSeekBar) findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                bar.setValue(seekBar.getProgress());
            }
        });

    }
}
