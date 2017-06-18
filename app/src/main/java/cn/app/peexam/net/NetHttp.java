package cn.app.peexam.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetHttp {
    public static boolean isConnection(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }

    public boolean isWifiConnected(Context context) {
        if (context != null) {
            NetworkInfo mWiFiNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(1);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public boolean isMobileConnected(Context context) {
        if (context == null) {
            return false;
        }
        NetworkInfo mMobileNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(0);
        if (mMobileNetworkInfo != null) {
            return mMobileNetworkInfo.isAvailable();
        }
        return false;
    }

    public static int getNetype(Context context) {
        int netType = -1;
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo == null) {
            return -1;
        }
        int nType = networkInfo.getType();
        if (nType == 0) {
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                netType = 3;
            } else {
                netType = 2;
            }
        } else if (nType == 1) {
            netType = 1;
        }
        return netType;
    }

    public static String getLocalIp(Context context) {
        String localIp = "";
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                Enumeration<InetAddress> inet = ((NetworkInterface) en.nextElement()).getInetAddresses();
                while (inet.hasMoreElements()) {
                    InetAddress ip = (InetAddress) inet.nextElement();
                    if (!ip.isLoopbackAddress() && (ip instanceof Inet4Address)) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("feige", "获取本地ip地址失败");
            e.printStackTrace();
        }
        return localIp;
    }
}
