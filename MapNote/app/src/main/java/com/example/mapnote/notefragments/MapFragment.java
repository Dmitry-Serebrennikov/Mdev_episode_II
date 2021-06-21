package com.example.mapnote.notefragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.mapnote.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapFragment extends Fragment {
    public MapFragment() {
    }

    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    LatLng myCords = null;

    List<Marker> markerList = new ArrayList<>();

    GoogleMap map = null;

    DatabaseReference userRef;
    FirebaseDatabase firebaseDatabase;
    SupportMapFragment mapFragment;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            MapStateManager mgr = new MapStateManager(getActivity().getApplicationContext());
            CameraPosition position = mgr.getSavedCameraPosition();
            if (position != null) {
                CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
                map.moveCamera(update);
                map.setMapType(mgr.getSavedMapType());
            }else {
                if (myCords != null) {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myCords, 18);
                    map.animateCamera(cameraUpdate);
                }
            }
            map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override 
                public void onMapLongClick(LatLng point) {

                    AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_marker, null);
                    AlertDialog dialog = adb.setView(dialogView).show();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogView.findViewById(R.id.addNoteBTN).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            drawNoteMarker(point, ((EditText) dialog.findViewById(R.id.noteTitleET)).getText().toString(), ((EditText) dialog.findViewById(R.id.noteDescriptionET)).getText().toString());
                            saveMarkers();
                            dialog.cancel();
                        }
                    });
                }
            });
            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(@NonNull @NotNull Marker marker) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_marker, null);
                    AlertDialog dialog = adb.setView(dialogView).show();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    ((EditText) dialog.findViewById(R.id.noteTitleET)).setText(marker.getTitle());
                    ((EditText) dialog.findViewById(R.id.noteDescriptionET)).setText(marker.getSnippet());
                    dialogView.findViewById(R.id.editNoteBTN).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            marker.setTitle(((EditText) dialog.findViewById(R.id.noteTitleET)).getText().toString());
                            marker.setSnippet(((EditText) dialog.findViewById(R.id.noteDescriptionET)).getText().toString());
                            marker.showInfoWindow();
                            saveMarkers();
                            dialog.cancel();
                        }
                    });
                    dialogView.findViewById(R.id.deleteNoteBTN).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            marker.remove();
                            markerList.remove(marker);
                            saveMarkers();
                            dialog.cancel();
                        }
                    });
                }
            });
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull @NotNull Marker marker) {

                    return false;
                }
            });
            map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(@NonNull @NotNull Marker marker) {

                }

                @Override
                public void onMarkerDrag(@NonNull @NotNull Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(@NonNull @NotNull Marker marker) {
                    saveMarkers();
                }
            });
            userRef.child("markers").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    markerList.clear();
                    map.clear();
                    drawMarker(myCords);
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.child("snippet").getValue() != null) {
                            drawNoteMarker(new LatLng(ds.child("hor").getValue(Double.class), ds.child("ver").getValue(Double.class)), ds.child("title").getValue(String.class), ds.child("snippet").getValue(String.class));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
    };

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);

    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            myCords = latLng;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
            map.animateCamera(cameraUpdate);
            locationManager.removeUpdates(this);
            drawMarker(myCords);
        }
    };

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }else {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);
        }
        mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        setupMapIfNeeded();

        userRef = firebaseDatabase.getReference().child(FirebaseAuth.getInstance().getUid());

    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { return; }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);
                } else {
                    getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1 );
                    getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                return;
            }
        }
    }*/

    private void saveMarkers(){
        int i = 0;
        userRef.child("markers").removeValue();
        for (Marker marker : markerList){
            userRef.child("markers").child(Integer.toString(i)).child("title").setValue(marker.getTitle());
            userRef.child("markers").child(Integer.toString(i)).child("hor").setValue(marker.getPosition().latitude);
            userRef.child("markers").child(Integer.toString(i)).child("ver").setValue(marker.getPosition().longitude);
            userRef.child("markers").child(Integer.toString(i)).child("snippet").setValue(marker.getSnippet());
            i++;
        }
    }

    private void drawNoteMarker(LatLng latLng, String title, String description) {
        if (this.map != null) {
            if (title.equals("")){
                title = " ";
            }
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(title);
            markerOptions.snippet(description);
            markerOptions.draggable(true);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            markerList.add(map.addMarker(markerOptions));
        }
    }

    public void drawMarker(LatLng latLng){
        if (this.map != null) {
            if (latLng != null) {
                map.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(4)
                        .strokeColor(0x220000FF)
                        .fillColor(0x220000FF));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MapStateManager mgr = new MapStateManager(getActivity().getApplicationContext());
        mgr.saveMapState(map);
    }

    private void setupMapIfNeeded() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (map == null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setupMapIfNeeded();
    }
}
