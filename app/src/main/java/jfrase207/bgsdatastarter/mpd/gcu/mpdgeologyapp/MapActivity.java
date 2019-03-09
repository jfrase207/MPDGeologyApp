package jfrase207.bgsdatastarter.mpd.gcu.mpdgeologyapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static jfrase207.bgsdatastarter.mpd.gcu.mpdgeologyapp.MyAdapter.mDataset;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{
    int position;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Intent intent = getIntent();
        position = intent.getIntExtra(MainActivity.EXTRA_MESSAGE,0);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        TextView title = findViewById(R.id.textView3);
        TextView description = findViewById(R.id.textView4);
        title.setText(mDataset[position].title);
        description.setText(mDataset[position].summary);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        final float latit = mDataset[position].lat;
        final float longt = mDataset[position].lon;

        LatLng location = new LatLng(latit, longt);
        googleMap.addMarker(new MarkerOptions().position(location).title(mDataset[position].title));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,8f));

    }
}
