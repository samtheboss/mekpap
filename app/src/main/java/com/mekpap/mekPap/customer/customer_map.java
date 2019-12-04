package com.mekpap.mekPap.customer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mekpap.mekPap.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.imperiumlabs.geofirestore.GeoFirestore;

public class customer_map extends FragmentActivity implements OnMapReadyCallback {
    private static final String[] problems = new String[]{"Worn Brake Pads",
            "Excessive Oil Consumption",
            "Uneven Tire Wear",
            "Radiator Leaks",
            "Cracked Windshield"};
    private static final String[] carTypes = new String[]{"Acura","Alfa Romeo",
            "Aston Martin","Audi",
            "Bentley","BMW","Bugatti", "Buick","Cadillac",
            "Chevrolet","Chrysler", "Citroen","Dodge", "Ferrari","Fiat",
            "Ford","Geely", "General Motors","GMC", "Honda","Hyundai", "Infiniti","Jaguar",
            "Jeep","Kia", "Koenigsegg","Lamborghini",
            "Land Rover","Lexus", "Maserati","Mazda",
            "McLaren","Mercedes-Benz", "Mini","Mitsubishi",
            "Nissan","Pagani", "Peugeot","Porsche",
            "Ram","Renault", "Rolls Royce","Saab",
            "Subaru", "Suzuki","TATA Motors", "Tesla","Toyota", "Volkswagen","Volvo",

    };


    private static final String[] carModels = new String[]{"Advan",
            "Atlas", "Cube",
            "Datsum", "Dualis", "juke", "Lafesta", "Latio",
            "Murano", "serena", "LX", "RX", "D-Max", "Ranger",
            "Escape", "Demio", "CX-5",
            "Axela", "Atenza","Avalon", "Camry", "Corolla",
            "Prius", "Yaris", "86",
            "Sienna", "C-HR", "Tacoma", "Tundra", "4Runner",
            "Rav4", "Sequoia", "HighLander", "Land Cruiser", "Mirai",
            "TRITON / L200", "ASX", "ECLIPSE CROSS",
            "OUTLANDER PHEV", "OUTLANDER",
            "MIRAGE / SPACE STAR", "ATTRAGE / MIRAGE G4",
            "PAJERO / MONTERO", "i-MiEV"
    };
    private GoogleMap mMap;
    // GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient fusedLocationProviderClient;

    Location mLastLocation;
    LocationRequest mLocationRequest;
    AutoCompleteTextView prob, carModel, cartypes,caryear;
    private Button mLogout, mRequest, settings;

    private LatLng pickupLocation;

    private Boolean requestBol = false;

    private Marker pickupMarker;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String destination;

    PlacesClient placesClient;
    String apiKey = "AIzaSyBU5qrfwybWfjovFYj4bp9FkzZ4upwZu6o";

    SupportMapFragment mapFragment;
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_map);
        checkLocationPermission();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // mLogout = findViewById(R.id.logout);
        mRequest = findViewById(R.id.makeRequest);
        prob = findViewById(R.id.problems);
        carModel = findViewById(R.id.carModel);
        cartypes = findViewById(R.id.carType);

        //settings = findViewById(R.id.profile);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
        placesClient = Places.createClient(this);
        ArrayAdapter<String> problemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, problems);
        ArrayAdapter<String> carTypesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, carTypes);
        ArrayAdapter<String> carModelsAdaper = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, carModels);
        loadProblemsDroFirebase();
        // prob.setAdapter(problemsAdapter);
        prob.setThreshold(1);

        cartypes.setAdapter(carTypesAdapter);
        cartypes.setThreshold(1);

        carModel.setAdapter(carModelsAdaper);
        carModel.setThreshold(1);

        mRequest.setOnClickListener(v -> {
           try {
                if (requestBol) {
                    requestBol = false;
                    geoQuery.removeAllListeners();
                    driverLocationRef.removeEventListener(driverLocationRefListener);
                    if (driverFoundID != null) {
                        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("user_inform").child("customers").child(driverFoundID);
                        driverRef.setValue(true);
                        driverFoundID = null;

                    }
                    driverFound = false;
                    radius = 1;
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customerRequest");
                    GeoFire geoFire = new GeoFire(ref);
                    geoFire.removeLocation(userId);

                    if (pickupMarker != null) {
                        pickupMarker.remove();
                    }
                    mRequest.setText("call Mechanic");

                } else {
                    requestBol = true;

                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customerRequest").child(userId);
                    GeoFire geoFire = new GeoFire(ref);

                    requestmodel model = new requestmodel(prob.getText().toString(),
                            carModel.getText().toString(), cartypes.getText().toString());
                    Map problem = new HashMap();
                    problem.put("problem", model);

                    geoFire.setLocation("Location", new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
                    //  ref.setValue(model);
                    ref.updateChildren(problem);

                    recordCustomerRequest();
                    recordHistory();
                    pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup Here"));

                    mRequest.setText("Getting your Mechanic...");

                    getClosestMechanic();
                }
            } catch (NullPointerException ignored) {

            }
            recordCustomerRequest();
        });


        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                destination = place.getName();
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });

    }

    private void recordHistory() {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customerRequest").child(userId).child("history");
        DatabaseReference history = FirebaseDatabase.getInstance().getReference("history").child(userId);
        String historyKey = history.push().getKey();


        HashMap map = new HashMap();
        map.put("car problem", prob.getText().toString());
        map.put("Date", "");
        map.put("CarType", cartypes.getText().toString());
        map.put("Rating", 0);
        map.put("Time", getCurrentTime());
        map.put("Status", "pending");
       // ref.child(historyKey).updateChildren(map);
        history.child(historyKey).updateChildren(map);


    }

    public void recordCustomerRequest() {
         double lat = mLastLocation.getLatitude();
         double lng = mLastLocation.getLongitude();
        CollectionReference ref = FirebaseFirestore.getInstance().collection("mechanic_requests");
        // GeoFire geoFire = new GeoFire(ref);
      //  GeoFirestore geoFirestore = new GeoFirestore(ref);
        FirebaseFirestore dbrequest = FirebaseFirestore.getInstance();
     //   geoFirestore.setLocation(userId, new GeoPoint(mLastLocation.getLatitude(),  mLastLocation.getLongitude()));
        Map<String, Object> map = new HashMap<>();
        map.put("car problem", prob.getText().toString());

        map.put("CarYear", caryear.getText().toString());
        map.put("carType", cartypes.getText().toString());
        map.put("carModel", carModel.getText().toString());
        map.put("customerRating", 0);
        map.put("customerComment", "");
        map.put("mechanicRating", "");
        map.put("customerId", userId);
        map.put("timestamp", getCurrentTime());
        map.put("status", "pending");
        map.put("lng", lng);
        map.put("lat", lat);
        map.put("carLocation", destination);
        map.put("mekId", "");
        map.put("workingStatus", "busy");

        dbrequest.collection("mechanic_requests").document().set(map)
                .addOnSuccessListener(aVoid -> Toast.makeText(customer_map.this, "Request made successful",
                        Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> {
                    Toast.makeText(customer_map.this, "ERROR" + e.toString(),
                            Toast.LENGTH_SHORT).show();
                    Log.d("TAG", e.toString());
                });
    }


    private Long getCurrentTime() {
        Long timeStamp = System.currentTimeMillis() / 1000;
        return timeStamp;
    }


    public void zoom() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(pickupLocation);
        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int padding = (int) (width * 0.2);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        mMap.animateCamera(cameraUpdate);
    }

    private int radius = 1;
    private Boolean driverFound = false;
    private String driverFoundID;

    GeoQuery geoQuery;


    private void getClosestMechanic() {
        DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference().child("mechanicAvailable");

        GeoFire geoFire = new GeoFire(driverLocation);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!driverFound && requestBol) {
                    driverFound = true;
                    driverFoundID = key;

                    DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("user_inform").child("customers").child(driverFoundID).child("customerRequest");
                    String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    HashMap map = new HashMap();
                    map.put("customerRideId", customerId);
                    map.put("destination", destination);
                    driverRef.updateChildren(map);

                    getMechanicLocation();
                    mRequest.setText("Looking for mechanic Location....");

                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!driverFound) {
                    radius++;
                    getClosestMechanic();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private Marker mDriverMarker;
    private DatabaseReference driverLocationRef;
    private ValueEventListener driverLocationRefListener;

    private void getMechanicLocation() {

        driverLocationRef = FirebaseDatabase.getInstance().getReference().child("MechanicIsWorking").child(driverFoundID).child("l");
        driverLocationRefListener = driverLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && requestBol) {
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;
                    if (map.get(0) != null) {
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1) != null) {
                        locationLng = Double.parseDouble(map.get(1).toString());
                    }
                    LatLng driverLatLng = new LatLng(locationLat, locationLng);
                    if (mDriverMarker != null) {
                        mDriverMarker.remove();
                    }
                    Location loc1 = new Location("");
                    loc1.setLatitude(pickupLocation.latitude);
                    loc1.setLongitude(pickupLocation.longitude);

                    Location loc2 = new Location("");
                    loc2.setLatitude(driverLatLng.latitude);
                    loc2.setLongitude(driverLatLng.longitude);

                    float distance = loc1.distanceTo(loc2);

                    if (distance < 100) {
                        mRequest.setText("Mechanic's Here" + (distance));
                    } else {
                        mRequest.setText("Mechanic Found: " + (distance));
                    }


                    mDriverMarker = mMap.addMarker(new MarkerOptions().position(driverLatLng).title("your mechanic"));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapbackground));

            if (!success) {
                Log.e("", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("", "Can't find style. Error: ", e);
        }
        mMap = googleMap;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            } else {
                checkLocationPermission();
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        mMap.setMyLocationEnabled(true);

    }



    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    new android.app.AlertDialog.Builder(this)
                            .setTitle("give permission")
                            .setMessage("give permission message")
                            .setPositiveButton("OK", (dialogInterface, i) ->
                                    ActivityCompat.requestPermissions(customer_map.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1))
                            .create()
                            .show();
                } else {
                    ActivityCompat.requestPermissions(customer_map.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }
        }

    }


    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    mLastLocation = location;

                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                }
            }
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    }
                    mapFragment.getMapAsync(this);
                } else {
                    Toast.makeText(this, "Please provide the permission", Toast.LENGTH_LONG).show();
                }
                break;
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
    }


    public void loadAppoinment(View view) {
        // addtoFirestore();
        Intent intent = new Intent(customer_map.this, MakeAppointment.class);
        startActivity(intent);
    }

    public void loadProblemsDroFirebase() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        database.child("problems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {

                    String suggestion = suggestionSnapshot.child("suggestion").getValue(String.class);
                    autoComplete.add(suggestion);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        prob.setAdapter(autoComplete);
    }

    public void pullUp(View view) {

    }

    public void pulldownbt(View view) {
    }
}
