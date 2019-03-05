package in.zippr.samplehere;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.arpaul.utilitieslib.PermissionUtils;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.SupportMapFragment;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // map embedded in the map fragment
    private Map map = null;
    // map fragment embedded in this activity
    private SupportMapFragment mapFragment = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (new PermissionUtils().checkPermission(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}) != PackageManager.PERMISSION_GRANTED)
                new PermissionUtils().requestPermission(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
        }

        initialize();
//         Search for the Map Fragment
//        final SupportMapFragment mapFragment = (SupportMapFragment)
//                getSupportFragmentManager().findFragmentById(R.id.mapfragment);
        // initialize the Map Fragment and
        // retrieve the map that is associated to the fragment
//        mapFragment.init(new OnEngineInitListener() {
//            @Override
//            public void onEngineInitializationCompleted(
//                    OnEngineInitListener.Error error) {
//                if (error == OnEngineInitListener.Error.NONE) {
//                    // now the map is ready to be used
//                    Map map = mapFragment.getMap();
//                    List<String> schemes = map.getMapSchemes();
//// Assume to select the 2nd map scheme in the available list
//                    map.setMapScheme(schemes.get(1));
//                    map.setCenter(new GeoCoordinate(17.4401, 78.3489, 0.0),
//                            Map.Animation.NONE);
//                    // Set the zoom level to the average between min and max
//                    map.setZoomLevel(17);
//
//                } else {
//                    System.out.println("ERROR: Cannot initialize SupportMapFragment");
//                }
//            }
//        });
    }

    private void initialize() {
        setContentView(R.layout.activity_main);
        // Search for the map fragment to finish setup by calling init().
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment);
        // Set up disk cache path for the map service for this application
        // It is recommended to use a path under your application folder for storing the disk cache
        boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(
                getApplicationContext().getExternalFilesDir(null) + File.separator + ".here-maps",
                getString(R.string.INTENT_NAME)); /* ATTENTION! Do not forget to update {YOUR_INTENT_NAME} */
        if (!success) {
            Toast.makeText(getApplicationContext(), "Unable to set isolated disk cache path.",
                    Toast.LENGTH_LONG);
        } else {
            mapFragment.init(new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                    if (error == OnEngineInitListener.Error.NONE) {
                        // retrieve a reference of the map from the map fragment
                        map = mapFragment.getMap();
                        // Set the map center to the Vancouver region (no animation)
                        map.setCenter(new GeoCoordinate(17.4401, 78.3489, 0.0), Map.Animation.NONE);
                        // Set the zoom level to the average between min and max
                        map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);
                    } else {
                        System.out.println("ERROR: Cannot initialize Map Fragment");
                    }
                }
            });
        }
    }
}
