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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chens.sendmylocation.exception.MyException;
import com.example.chens.sendmylocation.R;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private TextView longitude, latitude, altitude;
    private LocationManager locationManager;
    private Location location;
    private String msg;
    private String phoneNumber = "4122959487";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        longitude = (TextView) findViewById(R.id.longitudeTextView);
        latitude = (TextView) findViewById(R.id.latitudeTextView);
        altitude = (TextView) findViewById(R.id.altitudeTextView);
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
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            location = null;
            File dir = getFilesDir();
            new MyException("location service required", dir);
            return;
        }
        location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        String longitudeString = String.valueOf(location.getLongitude());
        String latitudeString = String.valueOf(location.getLatitude());
        String altitudeString = String.valueOf(location.getAltitude());
        longitude.setText(longitudeString);
        latitude.setText(latitudeString);
        altitude.setText(altitudeString);
        msg = "Longitude: " + longitudeString + ", Latitude: " + latitudeString + ", Altitude: " + altitudeString;

    }
    private void sendMsg() {
        SmsManager smsManager = SmsManager.getDefault();
        if (location != null) {
            smsManager.sendTextMessage(phoneNumber, null, msg, null, null);
            Toast.makeText(MainActivity.this, "Sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
        } else {
            smsManager.sendTextMessage(phoneNumber, null, "test", null, null);
            Toast.makeText(MainActivity.this, "Please open your location service", Toast.LENGTH_SHORT).show();
        }
    }
}
