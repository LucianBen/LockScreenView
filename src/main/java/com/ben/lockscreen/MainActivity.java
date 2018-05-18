package com.ben.lockscreen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LockScreenViewGroup lockScreen = findViewById(R.id.lockScreen);
        int[] answers = {1, 2, 3, 6, 9};
        lockScreen.setAnswer(answers);
    }
}
