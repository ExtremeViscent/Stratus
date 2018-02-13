package top.wyqnh.stratus;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

public class DataRecorder extends Service implements SensorEventListener {
    private SensorManager mSensorManager;
    public Sensor mPressure;
    public Sensor mTemperature;
    private LocationManager mLocationManager;
    public String TAG="DR";
    @Override
    public void onCreate() {

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE) ;
        mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
        Log.i("DR","Created");

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
