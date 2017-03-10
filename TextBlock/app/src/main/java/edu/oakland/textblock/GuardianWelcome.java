package edu.oakland.textblock;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class GuardianWelcome extends AppCompatActivity {
    private ImageButton signOutButton;
    private ImageButton NotifiButton;
    private ImageButton settingButton;
    private ImageButton statisButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guardian_welcome);

        NotifiButton  = (ImageButton) findViewById(R.id.notification_button);
        signOutButton = (ImageButton) findViewById(R.id.sign_out_button);
        settingButton = (ImageButton) findViewById(R.id.settings_button);
        statisButton  = (ImageButton) findViewById(R.id.stats_button);
        signOutButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GuardianWelcome.this, MainActivity.class);
                startActivity(intent);
            }
        });

        NotifiButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GuardianWelcome.this, Notifications.class);
                startActivity(intent);
            }
        });

        settingButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GuardianWelcome.this, Settings.class);
                startActivity(intent);
            }
        });

        statisButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GuardianWelcome.this, Statistic.class);
                startActivity(intent);
            }
        });


        }

    }



