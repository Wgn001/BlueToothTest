package gn.example.buletoothtest;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;

import gn.example.adapter.BlueListAdapter;
import gn.example.bean.BlueDevice;
import gn.example.utils.BluetoothUtil;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private static final String TAG = "MainActivity";

    private TextView tv_discovery;
    private CheckBox ck_bluetooth;
    private ListView lv_bluetooth;

    private BluetoothAdapter mAdapter;
    private ArrayList<BlueDevice> bluetoothList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothPermissions();
        initView();
        isBlueToothFunction();

    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    /**
     *初始化界面
     */
    private void initView() {
        tv_discovery=findViewById(R.id.tv_discovery);
        ck_bluetooth=findViewById(R.id.ck_bluetooth);
        lv_bluetooth=findViewById(R.id.lv_bluetooth);
    }

    /**
     * 获取基于地理位置的动态权限
     */
    private void bluetoothPermissions() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
    }


    /**
     * 获取动态权限的请求结果,开启蓝牙
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1 && grantResults[0]==1){
            isBlueToothFunction();
        }else{
            Toast.makeText(this,"用户拒绝了权限",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 判断本机是否有蓝牙功能
     */
    public  void isBlueToothFunction(){
        if(BluetoothUtil.getBluetoothStatus(this)){
            ck_bluetooth.setChecked(true);
        }
        ck_bluetooth.setOnCheckedChangeListener(this);
        tv_discovery.setOnClickListener(this);
        mAdapter=BluetoothAdapter.getDefaultAdapter();
        if(mAdapter==null){
            Toast.makeText(this,"本机未找到蓝牙功能",Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId()==R.id.ck_bluetooth){
            if(isChecked){
                beginDiscovery();
                Intent intent=new Intent(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                startActivityForResult(intent,1);
            }
            else {
                cancelDiscovery();
                BluetoothUtil.setBlueToothStatus(this,false);
                bluetoothList.clear();
                BlueListAdapter adapter=new BlueListAdapter(this,bluetoothList);
            }
        }
    }

    /**
     * 开始搜索蓝牙
     */
    private void beginDiscovery() {
        if(mAdapter.isDiscovering()!=true){
            bluetoothList.clear();
            BlueListAdapter blueListAdapter=new BlueListAdapter(this,bluetoothList);
            lv_bluetooth.setAdapter(blueListAdapter);
            tv_discovery.setText("正在搜索蓝牙设备");
            mAdapter.startDiscovery();
        }
    }
    /**
     * 关闭蓝牙搜索
     */
    private void cancelDiscovery() {
        mHandler.removeCallbacks(mRefresh);
        tv_discovery.setText("取消搜索蓝牙设备");
        if(mAdapter.isDiscovering()==true){
            mAdapter.cancelDiscovery();
        }

    }

    /**
     * 刷新搜索蓝牙设备线程
     */
    private Runnable mRefresh=new Runnable() {
        @Override
        public void run() {
            beginDiscovery();
            mHandler.postDelayed(this,2000);
        }
    };


    @Override
    public void onClick(View v) {

    }
}
