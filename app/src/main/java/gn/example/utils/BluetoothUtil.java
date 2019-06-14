package gn.example.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BluetoothUtil {
    private static final String TAG = "BluetoothUtil";

    /**
     * 获取当前蓝牙的开关状态
     * @param context
     * @return
     */
    public static boolean getBluetoothStatus(Context context){
        BluetoothAdapter adapter=BluetoothAdapter.getDefaultAdapter();
        boolean enable=false;
        switch (adapter.getState()){
            case BluetoothAdapter.STATE_ON:
            case BluetoothAdapter.STATE_TURNING_ON:
                enable=true;
                break;
            case BluetoothAdapter.STATE_OFF:
            case BluetoothAdapter.STATE_TURNING_OFF:
                enable=false;
                break;
        }
        return enable;
    }


    /**
     * 打开/关闭蓝牙
     * @param context
     * @param enable 当前蓝牙的状态
     */
    public static void setBlueToothStatus(Context context,boolean enable){
        BluetoothAdapter adapter=BluetoothAdapter.getDefaultAdapter();
        if(enable){
            adapter.enable();
        }else{
            adapter.disable();
        }
    }

    /**
     * 读取数据
     * @param inputStream
     * @return
     */
    public static String readInputStream(InputStream inputStream){
            String result="";
            ByteArrayOutputStream outputStream=null;
            try {
                outputStream = new ByteArrayOutputStream();
                byte[] bytes=new byte[1024];
                int len=0;
                while ((len=inputStream.read(bytes))!=-1){
                    outputStream.write(bytes,0,len);
                }

                byte[] data =outputStream.toByteArray();

                result=data.toString();
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                try {
                    if (outputStream!=null){
                        outputStream.close();
                    }
                    if (inputStream!=null){
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return result;
    }


    /**
     * 发送数据
     * @param bluetoothSocket
     * @param message
     */
    public static void writeOutputStream(BluetoothSocket bluetoothSocket, String message){
        Log.i(TAG,"发送的数据："+message);
        OutputStream outputStream=null;
        try{
            outputStream=bluetoothSocket.getOutputStream();
            outputStream.write(message.getBytes());

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if(outputStream!=null){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
