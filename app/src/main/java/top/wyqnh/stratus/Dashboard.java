package top.wyqnh.stratus;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class Dashboard extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        SensorManager mSensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List SensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        LinearLayout mSensorOut =(LinearLayout) findViewById(R.id.SensorOut);
        for (Object att :SensorList)
        {
            TextView tv =new TextView(this);
            tv.setText(att.toString());
            tv.setTextSize(20);
            mSensorOut.addView(tv);

        }
        FloatingActionButton FMC_EngageBtn=(FloatingActionButton) findViewById(R.id.FMC_EngageBtn);
        FMC_EngageBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FMC_Engage();
                Log.i("Fuck","Intent delivered");
            }
        }) ;



    }

    public void FMC_Engage(){
        Intent intent= new Intent(Dashboard.this,FMC.class);
        Intent intent1 = new Intent(Dashboard.this,DataRecorder.class);
        startService(intent);
        startService(intent1);
        Log.i("Fuck","Engaged");
    }
}
