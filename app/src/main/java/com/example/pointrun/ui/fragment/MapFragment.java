package com.example.pointrun.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.pointrun.R;
import com.example.pointrun.databinding.MapFragmentBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Random;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapViewModel mViewModel;
    MapFragmentBinding binding;
    GoogleMap googleMap;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback  locationCallback;

    Location loc;
    LatLng oldLocation=null;
    Circle circle=null;
    Polyline polyline=null;
    Circle []mission;

    Projection projection;
    Bitmap mBitmap;



    float totalDistance=0;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.map_fragment, container, false);
        locationRequest=LocationRequest.create();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        locationCallback=new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                markOnMap(locationResult.getLastLocation());
            }
        };

        binding.mapView.onCreate(null);
        binding.mapView.onResume();
        binding.mapView.getMapAsync(this);

        getMyLocation();

        return binding.getRoot();

    }



    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap=googleMap;
        //this.googleMap.getUiSettings().setAllGesturesEnabled(false);
    }


    public void getMyLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
        }
        else {
            Toast.makeText(getActivity(),"no per",Toast.LENGTH_SHORT).show();
        }
    }

    private void markOnMap(Location location) {
        if (location != null){
            LatLng melbourneLocation = new LatLng(location.getLatitude(),location.getLongitude());

            //Run for the first time
            if(circle== null){
                CircleOptions circleOptions=new CircleOptions().center(melbourneLocation).radius(1.5).strokeColor(Color.rgb(20,83,116)).fillColor(Color.rgb(20,83,116));
                circle=googleMap.addCircle(circleOptions);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(melbourneLocation,17f));
                projection=this.googleMap.getProjection();


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    getPublicPoints();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


            circle.setCenter(melbourneLocation);
            drawPath(melbourneLocation);
            oldLocation=melbourneLocation;
        }else{
            Toast.makeText(getActivity(),"please enable GPS",Toast.LENGTH_SHORT).show();
        }
    }


    public void drawPath (LatLng melbourneLocation){
        if (oldLocation != null){
            polyline=googleMap.addPolyline(new PolylineOptions().add(melbourneLocation,oldLocation).width(5).color(Color.rgb(85,136,163)));
            float[] results = new float[1];
            Location.distanceBetween(oldLocation.latitude, oldLocation.longitude,
                    melbourneLocation.latitude, melbourneLocation.longitude, results);
            totalDistance=totalDistance+results[0];
            binding.textView.setText(String.valueOf(totalDistance));
        }
    }

    public void setMissions(ArrayList<LatLng> available){
        mission= new Circle[10];
        for (int i=0; i<mission.length; i++){
            int ran=new Random().nextInt(available.size());
            LatLng missionLocation=new LatLng(available.get(ran).latitude,available.get(ran).longitude);
            CircleOptions missionOptions=new CircleOptions().center(missionLocation).radius(1.5).strokeColor(Color.rgb(255,150,150)).fillColor(Color.rgb(255,50,50));
            mission[i]=googleMap.addCircle(missionOptions);
        }
    }

    public void getPublicPoints(){
        googleMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                mBitmap=bitmap;
                ArrayList<LatLng> availableLatLng = new ArrayList<>();
                for(int i=0;i<mBitmap.getHeight();i=i+50){
                    for(int j=0;j<mBitmap.getWidth();j=j+50){
                        Color pixelColor = mBitmap.getColor(j,i);
                        if (pixelColor.red()==pixelColor.green()&&pixelColor.green()==pixelColor.blue()&&pixelColor.blue()==1){
                            availableLatLng.add(projection.fromScreenLocation(new Point(j,i)));

                        }

                    }
                }
                setMissions(availableLatLng);
            }
        });

    }




}