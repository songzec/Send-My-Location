package com.example.chens.sendmylocation.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chens.sendmylocation.exception.MyException;
import com.example.chens.sendmylocation.R;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private TextView longitude, latitude;
    private LocationManager locationManager;
    private Location location;
    private String msg;
    private String phoneNumber = "4122959487";
    private final int REQUEST_PERMISSION_SEND_SMS_CODE = 1;
    private final int REQUEST_PERMISSION_COARSE_LOCATION_CODE = 2;
    private final int REQUEST_PERMISSION_FINE_LOCATION_CODE = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        longitude = (TextView) findViewById(R.id.longitudeTextView);
        latitude = (TextView) findViewById(R.id.latitudeTextView);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
                sendMsg();
            }
        });

    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            File dir = getFilesDir();
            new MyException("location service required", dir);

            requestLocationPermission();

            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);

        String longitudeString = String.valueOf(location.getLongitude());
        String latitudeString = String.valueOf(location.getLatitude());

        longitude.setText("longitude: " + longitudeString);
        latitude.setText("latitude: " + latitudeString);
        msg = "Longitude: " + longitudeString + ", Latitude: " + latitudeString;

    }
    private void sendMsg() {
        SmsManager smsManager = SmsManager.getDefault();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestSMSPermission();
        }

        if (location != null) {
            smsManager.sendTextMessage(phoneNumber, null, msg, null, null);
            Toast.makeText(MainActivity.this, "Sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
        } else {
            smsManager.sendTextMessage(phoneNumber, null, "test", null, null);
            Toast.makeText(MainActivity.this, "Please open your location service", Toast.LENGTH_SHORT).show();
        }
    }
    private void requestSMSPermission() {
        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, REQUEST_PERMISSION_SEND_SMS_CODE);
    }
    private void requestLocationPermission() {
        requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_FINE_LOCATION_CODE);
        requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_COARSE_LOCATION_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_SEND_SMS_CODE
                || requestCode == REQUEST_PERMISSION_FINE_LOCATION_CODE
                ||requestCode == REQUEST_PERMISSION_COARSE_LOCATION_CODE) {
            int grantResult = grantResults[0];
            boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;
            Log.i("", "onRequestPermissionsResult granted=" + granted);
        }
    }

}
