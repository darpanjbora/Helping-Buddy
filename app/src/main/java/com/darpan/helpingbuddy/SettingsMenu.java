package com.darpan.helpingbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by sony on 6/18/2016.
 */
public class SettingsMenu extends AppCompatActivity {

    EditText editText;
    Button button;
    public static String changedmailID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        Intent intent = getIntent();
        editText = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button5);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changedmailID = editText.getText().toString();
                Toast.makeText(getApplicationContext(),"Email changed.",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
