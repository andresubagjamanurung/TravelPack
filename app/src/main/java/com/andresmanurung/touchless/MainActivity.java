package com.andresmanurung.touchless;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

//Using Firebase as Realtime Database
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    //Set reference of Firebase tree
    private DatabaseReference refFirebase = FirebaseDatabase.getInstance().getReference();
    private CardView cardBatt;
    private TextView textBatt, textSani1, textSani2, textPro;
    private ProgressBar progSani1, progSani2, progPro;
    private ImageView imgBatt;

    private String batt, sani1, sani2, pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardBatt = findViewById(R.id.cardBatt);
        textBatt = findViewById(R.id.textBattPercentage);
        textSani1 = findViewById(R.id.textSanitizer1);
        textSani2 = findViewById(R.id.textSanitizer2);
        textPro = findViewById(R.id.textProcess);
        progSani1 = findViewById(R.id.progSanitizer1);
        progSani2 = findViewById(R.id.progSanitizer2);
        progPro = findViewById(R.id.progProcess);
        imgBatt = findViewById(R.id.imgBatt);

        //Set status bar and main button to be transparent
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void ubahImageBatt(String batts){
        //Set battery level image appropriate to battery percentage
        float fBatt = Float.parseFloat(batts);
        if(fBatt<=10) {
            imgBatt.setImageResource(R.drawable.ic_10percent);
        }else if(fBatt<=20){
            imgBatt.setImageResource(R.drawable.ic_20percent);
        }else if(fBatt<=30){
            imgBatt.setImageResource(R.drawable.ic_30percent);
        }else if(fBatt<=40){
            imgBatt.setImageResource(R.drawable.ic_40percent);
        }else if(fBatt<=50){
            imgBatt.setImageResource(R.drawable.ic_50percent);
        }else if(fBatt<=60){
            imgBatt.setImageResource(R.drawable.ic_60percent);
        }else if(fBatt<=70){
            imgBatt.setImageResource(R.drawable.ic_70percent);
        }else if(fBatt<=80){
            imgBatt.setImageResource(R.drawable.ic_80percent);
        }else if(fBatt<=90){
            imgBatt.setImageResource(R.drawable.ic_90percent);
        }else{
            imgBatt.setImageResource(R.drawable.ic_100percent);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        //Listening to battery percentage value from "batt" subtree
        refFirebase.child("batt").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                batt = snapshot.getValue().toString();
                String persen = batt + "%";
                textBatt.setText(persen);
                ubahImageBatt(batt);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){}
        });

        //Listening to sanitizer volume 1 from "sanitizer1" subtree
        refFirebase.child("sanitizer1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sani1 = snapshot.getValue().toString();
                String persen = sani1 + "%";
                int prog = Integer.parseInt(sani1);
                textSani1.setText(persen);
                progSani1.setProgress(prog);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){}
        });

        //Listening to sanitizer volume 2 from "sanitizer2" subtree
        refFirebase.child("sanitizer2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sani2 = snapshot.getValue().toString();
                String persen = sani2 + "%";
                int prog = Integer.parseInt(sani2);
                textSani2.setText(persen);
                progSani2.setProgress(prog);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){}
        });

        //Listening to sanitizing process from "process" subtree
        refFirebase.child("process").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pro = snapshot.getValue().toString();
                String persen = pro + "%";
                int prog = Integer.parseInt(pro);
                textPro.setText(persen);
                progPro.setProgress(prog);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){}
        });
    }
}