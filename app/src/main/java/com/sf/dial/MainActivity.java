package com.sf.dial;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.sf.dial.widget.DialProgressBar;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DialProgressBar bar = (DialProgressBar) findViewById(R.id.progressBar);

        AppCompatSeekBar seekBar = (AppCompatSeekBar) findViewById(R.id.seekBar);
        AppCompatSeekBar seekBar2 = (AppCompatSeekBar) findViewById(R.id.seekBar2);
        bar.setValue(50f);

        bar.setRotationX(20);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float value = (float) seekBar.getProgress() / 100 * bar.getMaxValue();

                bar.setValue(value);
            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bar.setRotationX(seekBar.getProgress());
                bar.setPaintAlpha(255-(int) (seekBar.getProgress() / 100f * 255));
                Toast.makeText(MainActivity.this,"" + seekBar.getProgress() / 100f * 255,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });


    }
}
