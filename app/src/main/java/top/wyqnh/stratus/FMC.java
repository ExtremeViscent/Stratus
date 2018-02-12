package top.wyqnh.stratus;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Callback;
import okio.Okio;


public class FMC extends Service {
    public static HashMap<String, String> queue = new HashMap<String, String>();
    //获得闹钟管理器
    /**
     * 客户端执行轮询的时间间隔，该值由StartQueryInterface接口返回，默认设置为30s
     */
    public static int LOOP_INTERVAL_SECS = 30;
    /**
     * 轮询时间间隔(MLOOP_INTERVAL_SECS 这个时间间隔变量有服务器下发，此时轮询服务的场景与逻辑与定义时发生变化，涉及到IOS端，因此采用自己定义的常量在客户端写死时间间隔)
     */
    public static int MLOOP_INTERVAL_SECS = 30;
    /**
     * 当前服务是否正在执行
     */
    public static boolean isServiceRuning = false;
    /**
     * 定时任务工具类
     */
    public static Timer timer = new Timer();
    public static float pressure;

    private static Context context;

    public void onStartCommand()
    {
startLoop();
        Log.i("Loop","Started");
    }
    private void startLoop() {
            timer = new Timer();
            Log.i("tmr set","chk");

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isServiceRuning = true;
                doReport();
            }
        }, 0, MLOOP_INTERVAL_SECS * 1000);
    }
    public void doReport()
    {

        queue.put("pressure",String.valueOf(pressure));
        Log.i("Fuck","Reported");
         String url="http://http://118.190.203.180:19132/senduseage.php?";
        OkHttpClient client = new OkHttpClient();
        for(Map.Entry<String, String> entry : queue.entrySet() ) {
            url=url+entry.getKey()+"="+entry.getValue()+"&";
        }
 url=url.substring(0,url.length()-1);
        Request request= new Request.Builder()
                .get()
                .url(url)
                .build();
        client.newCall(request).enqueue(
                new Callback()
                {
                    @Override
                    public void onFailure(Call call, IOException e){

                    }
                    @Override
                    public void onResponse(Call call, Response response)throws IOException{
                        if(response.isSuccessful()){

                        }
                    }
                }
        );
    }
    public FMC() {
    }
public void onCreate(){
    startLoop();
    Log.i("FMC","lps");
}
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
