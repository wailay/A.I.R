package net.ltm.air;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;


import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.ltm.air.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NearestMapActivity extends FragmentActivity implements OnMapReadyCallback,
        OnMyLocationClickListener, OnMyLocationButtonClickListener, OnMarkerClickListener {
    RequestQueue queue;
    JSONObject allTrash = null;
    String SERVER_URL = "http://10.200.46.211:5000/trash";
    private GoogleMap mMap;
    Dialog garbageDialog;
    ImageView garbageImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_map);
        queue = Volley.newRequestQueue(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        garbageImage = new ImageView(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(garbageImage);
        //So we can call dialog.setMessage();
        builder.setMessage("");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        // Create the AlertDialog object and return it
        garbageDialog = builder.create();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, SERVER_URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        allTrash = response;
                        System.out.println("TEEESTT");
                        System.out.println(allTrash);
                        JSONArray all = null;
                        try {
                            all = allTrash.getJSONArray("result");
                        }catch (Exception e){
                            System.out.println(e);
                        }

                        for (int i = 0; i < all.length();  i++){
                            try {
                                String coords = all.getJSONObject(i).getString("coord");
                                String category = all.getJSONObject(i).getString("category");
                                System.out.println(category);
                                String id = all.getJSONObject(i).getJSONObject("_id").getString("$oid");
                                coords = coords.substring(1);
                                coords = coords.substring(0, coords.length() - 1);
                                System.out.println(coords);
                                String[] coord = coords.split(",");
                                double lat = Double.parseDouble(coord[0]);
                                double longi = Double.parseDouble(coord[1]);
                                Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.recycling_ico_background);
                                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(img);

                                if (category.equals("both")){

                                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, longi))
                                            .title(id).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                                }else if(category.equals("garbage")) {
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, longi))
                                            .title(id).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                }else{
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, longi))
                                            .title(id).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                }



                            }catch (Exception e){
                                System.out.println(e);
                            }
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROOOOOOOOOOOOOOOR "+ error.toString());

                    }
                });

        queue.add(jsonObjectRequest);

        System.out.println("TRRAAAAAASH");
        System.out.println(allTrash);
        // Add a marker in Sydney and move the camera


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
        }
    mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        String id = marker.getTitle();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, SERVER_URL+"/"+id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println(response);
                        Bitmap gbBm = b64toimage(response);
                        garbageImage.setImageBitmap(gbBm);
                        garbageDialog.show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("EROOOOOOOOOOOR "+ error.toString());
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);


        return false;
        }
    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, null, Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    mMap.setMyLocationEnabled(true);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public Bitmap b64toimage(String data){
        byte[] decodedString = Base64.decode(data, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
        return decodedByte;

    }


}
