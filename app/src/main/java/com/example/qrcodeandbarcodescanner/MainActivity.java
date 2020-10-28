package com.example.qrcodeandbarcodescanner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView scan, history;
    private static final int REQUEST_CAMERA = 1;
    private final int MY_PERMISSION_REQUEST_CAMERA = 1001;
    private final String TAG = "sjhhdhdsds";
    int permVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scan = (ImageView) findViewById(R.id.btnscan);
        history = (ImageView) findViewById(R.id.btnhistory);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permVal=1;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                            && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                            &&checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {

                        PermissionDialog();
                    }
                    else {
                        startActivity(new Intent(MainActivity.this,FullScanner.class));
                    }





                }
               /* Intent i = new Intent(MainActivity.this, SeconActivity.class);
                startActivity(i);*/


            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, History.class);
                startActivity(i);

            }
        });
    }
    void PermissionDialog(){
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.WAKE_LOCK)
                .check();
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {

        }


    };

}
