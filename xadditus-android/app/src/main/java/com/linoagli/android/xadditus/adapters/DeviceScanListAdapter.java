/**
 * Xadditus Android Project.
 * com.linoagli.android.Xadditus.Activities.DeviceScan
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 * @since 3/28/17 2:29 AM
 */
package com.linoagli.android.xadditus.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.linoagli.android.xadditus.R;
import com.linoagli.android.xadditus.DeviceSelectedEvent;
import com.linoagli.android.xadditus.helpers.SystemInfo;
import com.linoagli.android.xadditus.helpers.Utils;
import com.linoagli.java.XadditusCore.Models.Device;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class DeviceScanListAdapter extends RecyclerView.Adapter<DeviceScanListAdapter.ViewHolder> {
    private List<Device> devices = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.partial_scan_device_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Device device = devices.get(position);

        int osThumbnailRes = R.drawable.pc_logo;
        boolean isBluetoothDevice = device.address == null && device.data != null;
        SystemInfo.OperatingSystem os = SystemInfo.INSTANCE.getOperatingSystemFromName(device.osName);

        if (isBluetoothDevice) {
            osThumbnailRes = R.drawable.bluetooth_logo;
        } else if (os == SystemInfo.OperatingSystem.Windows) {
            osThumbnailRes = R.drawable.win_logo;
        } else if (os == SystemInfo.OperatingSystem.Linux) {
            osThumbnailRes = R.drawable.linux_logo;
        } else if (os == SystemInfo.OperatingSystem.Mac) osThumbnailRes = R.drawable.mac_logo;

        holder.iv_thumbnail.setImageResource(osThumbnailRes);
        holder.tv_deviceName.setText(device.name);
        holder.tv_ssid.setText(device.networkSSID);
        holder.tv_osInfo.setText(device.osName + " - " + device.osVersion);

        holder.itemView.setOnClickListener(v -> EventBus.getDefault().post(new DeviceSelectedEvent(device)));
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public void addDevice(Device device) {
        for (Device item : devices) {
            boolean areSameAddress = item.address != null && item.address.equals(device.address);
            boolean areSameBluetoothName = item.data != null && item.name.equals(device.name);

            if (areSameAddress || areSameBluetoothName) {
                return;
            }
        }

        devices.add(device);
        notifyDataSetChanged();
    }

    public void removeAllDevices() {
        devices.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_thumbnail;
        TextView tv_deviceName;
        TextView tv_ssid;
        TextView tv_osInfo;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_thumbnail = (ImageView) itemView.findViewById(R.id.iv_scanDeviceListItem_thumbnail);
            tv_deviceName = (TextView) itemView.findViewById(R.id.tv_scanDeviceListItem_deviceName);
            tv_ssid = (TextView) itemView.findViewById(R.id.tv_scanDeviceListItem_ssid);
            tv_osInfo = (TextView) itemView.findViewById(R.id.tv_scanDeviceListItem_osInfo);
        }
    }
}
