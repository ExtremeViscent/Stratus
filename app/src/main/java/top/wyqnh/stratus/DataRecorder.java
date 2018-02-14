package top.wyqnh.stratus;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class DataRecorder extends Service implements SensorEventListener {
    private SensorManager mSensorManager;
    public Sensor mPressure;
    public Sensor mTemperature;
    private LocationManager mLocationManager;
    private String locateType;
    private static final String GPS_LOCATION_NAME = android.location.LocationManager.GPS_PROVIDER;

    @Override
    public void onCreate() {

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
        Log.i("DR","Created");
LocationListener locationListener =new LocationListener() {
    @Override
    public void onLocationChanged(Location location) {
        FMC.queue.put("Longitude",String.valueOf(location.getLongitude() ));
        FMC.queue.put("Latitude",String.valueOf(location.getLatitude()));
        FMC.queue.put("Speed",String.valueOf(location.getSpeed()));
        FMC.queue.put("Bearing",String.valueOf(location.getBearing()));
        FMC.queue.put("Altitude",String.valueOf(location.getAltitude()));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
};
        mLocationManager.requestLocationUpdates(locateType, 100,0,locationListener);
        SensorEventListener TemperatureListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                FMC.queue.put("temperature",String.valueOf(sensorEvent.values [0]));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        mSensorManager.registerListener(TemperatureListener,mTemperature,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        FMC.queue.put("pressure",String.valueOf(event.values [0]));

        // Do something with this sensor data.
    }

    public DataRecorder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
