package retrofit.mifeng.us.myguge;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import java.io.File;
import java.util.ArrayList;

import static retrofit.mifeng.us.myguge.R.id.map;

public class MainActivity extends AppCompatActivity{
    private boolean value=true;
    private MapView mapView;
    AMap aMap;
    private ArrayList<String> list;
    private LatLng latLng1;
    private LatLng latLng;
    private AlertDialog alertDialog;
    ListView lv;
    private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();
        v = View.inflate(this, R.layout.aaa, null);
        lv = (ListView) v.findViewById(R.id.lv);
        MarkerOptions markerOptions = new MarkerOptions();
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
        latLng = new LatLng(40.03083, 116.446813, true);
        markerOptions.icon(bitmapDescriptor)
                .alpha(0.7f)
                .position(latLng)
                .visible(true)
                .title("red star")
                .snippet("高德地图设置覆盖物 marker样式坐标");
        aMap.addMarker(markerOptions);
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setScaleControlsEnabled(true);
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        AMap.OnInfoWindowClickListener listener = new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker arg0) {
                initLv();
            }
        };
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                latLng1 = new LatLng(latitude, longitude);
            }
        });
        //绑定信息窗点击事件
        aMap.setOnInfoWindowClickListener(listener);
    }

    private void initLv() {
        /*if (!value){
            return;
        }
        if (value){
            this.value=false;
        }*/
        boolean installqq = isInstallByread("com.tencent.map");
        boolean installnav = isInstallByread("com.autonavi.minimap");
        boolean installbaidu = isInstallByread("com.baidu.BaiduMap");
        list = new ArrayList<>();
        list.clear();
        if (installqq) {
            list.add("腾讯地图");
        } if (installbaidu) {
            list.add("百度地图");
        } if (installnav) {
            list.add("高德地图");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).equals("腾讯地图")) {
                    //腾讯地图
                    if (latLng1 != null && latLng != null) {
                        // 腾讯地图
                        Intent naviIntent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("qqmap://map/routeplan?type=drive&from=" +"&fromcoord=" + latLng1.latitude + "," + latLng1.longitude + "&to=" + null + "&tocoord=" + latLng.latitude + "," + latLng.longitude + "&policy=0&referer=appName"));
                        startActivity(naviIntent);
                    }
                } else if (list.get(position).equals("百度地图")) {
                    if (latLng1 != null && latLng != null) {
                        // 百度地图
                        Log.d("e", "onItemClick: aaaaaaaaaa");
                        Intent naviIntent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("baidumap://map/geocoder?location=" + latLng.latitude + "," + latLng.longitude));
                        startActivity(naviIntent);
                    }
                } else if (list.get(position).equals("高德地图")) {
                    if (latLng1 != null && latLng != null) {
                        // 高德地图
                        Intent naviIntent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("androidamap://route?sourceApplication=appName&slat=&slon=&sname=我的位置&dlat=" + latLng.latitude + "&dlon=" + latLng.longitude + "&dname=目的地&dev=0&t=2"));
                        startActivity(naviIntent);
                    }
                }
            }
        });
        if (alertDialog==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setView(v);
            alertDialog = builder.create();
        }
        alertDialog.show();
    }

    /**
     * 判断是否安装目标应用
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}
