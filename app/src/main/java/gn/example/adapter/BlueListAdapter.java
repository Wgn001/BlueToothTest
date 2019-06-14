package gn.example.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.constraint.solver.widgets.ConstraintWidgetGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import gn.example.bean.BlueDevice;
import gn.example.buletoothtest.R;

public class BlueListAdapter extends BaseAdapter {

    private static final String TAG = "BlueListAdapter";
    private Context mContext;
    private ArrayList<BlueDevice> mBlueList;
    private LayoutInflater mInflater;
    private String[] mStateArray={"未绑定","绑定中","已绑定","已保存"};

    public BlueListAdapter(Context mContext, ArrayList<BlueDevice> mBlueList) {
        this.mContext = mContext;
        this.mBlueList = mBlueList;
        mInflater=LayoutInflater.from(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoler viewHoler=null;
        if(convertView==null){
            viewHoler=new ViewHoler();
            convertView=mInflater.inflate(R.layout.item_buletooth,parent,false);
            viewHoler.tv_blue_name=convertView.findViewById(R.id.tv_blue_name);
            viewHoler.tv_blue_address=convertView.findViewById(R.id.tv_blue_address);
            viewHoler.tv_blue_state=convertView.findViewById(R.id.tv_blue_state);
            convertView.setTag(convertView);
        }
        else{
            viewHoler=(ViewHoler) convertView.getTag();
        }

        BlueDevice blueDevice=mBlueList.get(position);
        viewHoler.tv_blue_name.setText(blueDevice.name);
        viewHoler.tv_blue_address.setText(blueDevice.address);
        viewHoler.tv_blue_state.setText(blueDevice.address);
        return convertView;
    }

    @Override
    public int getCount() {
        return mBlueList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBlueList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHoler{
        private TextView tv_blue_name;
        private TextView tv_blue_address;
        private TextView tv_blue_state;
    }

}
