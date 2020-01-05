package com.mekpap.mekPap.navigation;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mekpap.mekPap.R;
import com.mekpap.mekPap.customer.MakeAppointment;
import com.mekpap.mekPap.customer.customer_map;
import com.mekpap.mekPap.history.historyActivity;
import com.mekpap.mekPap.login;
import com.mekpap.mekPap.mpesa.mpesaAcitivity;
import com.mekpap.mekPap.profile;
import com.mekpap.mekPap.support.Users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class menu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private static final String[] problems = new String[]{"Worn Brake Pads",
            "Excessive Oil Consumption",
            "Uneven Tire Wear",
            "Radiator Leaks",
            "Cracked Windshield"};
    private static final String[] carTypes = new String[]{"Acura", "Alfa Romeo",
            "Aston Martin", "Audi", "Bentley", "BMW", "Bugatti",
            "Buick", "Cadillac", "Chevrolet", "Chrysler",
            "Citroen", "Dodge", "Ferrari", "Fiat",
            "Ford", "Geely", "General Motors", "GMC",
            "Honda", "Hyundai", "Infiniti", "Jaguar", "Jeep", "Kia",
            "Koenigsegg", "Lamborghini", "Land Rover", "Lexus",
            "Maserati", "Mazda", "McLaren", "Mercedes-Benz",
            "Mini", "Mitsubishi", "Nissan", "Pagani",
            "Peugeot", "Porsche", "Ram", "Renault",
            "Rolls Royce", "Saab", "Subaru", "Suzuki", "TATA Motors",
            "Tesla", "Toyota", "Volkswagen", "Volvo",

    };


    private static final String[] carModels = new String[]{"4-runner", "Allex", "Allion", "Alphard",
            "Aqua", "Auris", "Avensis", "Axio", "Axion", "Belta",
            "Caldina", "Camry", "Carina", "Corolla",
            "Crown", "Advan", "Atlas", "Bluebird", "Caravan",
            "Cube", "Datsun", "Dualis", "Fuga", "Hardbody",
            "Juke", "Forester", "Impreza", "Legacy",
            "Outback", "Trezia", "WRX STI",
            "Atenza", "Axela", "Bongo", "CX-5", "Demio", "Premacy", "Verisa",
            "Golf", "Jetta", "Passat", "Polo", "Tiguan", "Touareg",
            "D-MAX", "Fielder", "FJ Cruiser", "Fortuner", "Fun Cargo", "Harrier",
            "Hiace", "Hilux", "Ipsum", "Isis", "Kluger", "Land cruiser", "Land cruiser prado",
            "Mark II", "Mark X", "Noah", "Lafesta", "Latio", "March",
            "Murano", "Navara", "Note", "Primera", "Patrol", "serena",
            "Skyline", "Passo", "Platz", "Porte", "Premio", "Prius", "Probox",
            "Ractis", "Rav4", "Rumion", "Rush", "Sienta",
            "Spacio", "Succeed", "Surf", "Townance", "Sunny",
            "Sylphy", "Teana", "Tiida", "Wingroad", "X-trail", "Airwave", "Civic", "CRV", "Fit",
            "Freed", "Insight", "Odyssey", "Shuttle", "Stream", "ASX", "Canter",
            "Colt", "Delica", "Galant", "Lancer", "Lancer Evo", "Mirage", "Outlander", "Pajero",
            "Pajero IO", "Pajero Mini", "RVR", "Shogun",
            "LX", "RX", "Escape", "Ranger"
    };
    private GoogleMap mMap;
    // GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient fusedLocationProviderClient;

    Location mLastLocation;
    LocationRequest mLocationRequest;
    AutoCompleteTextView prob, carModel, cartypes, caryear;
    private Button mLogout, mRequest, settings, pulldown, pullUp;
    LinearLayout pullUpbutton;
    private LatLng pickupLocation;

    private Boolean requestBol = false;

    private Marker pickupMarker;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String destination;
    TextView requestStatuesinfo;
    PlacesClient placesClient;
    String apiKey = String.valueOf(R.string.google_maps_key);
    SupportMapFragment mapFragment;
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    LinearLayout requestComponet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        checkLocationPermission();
        pullUp = findViewById(R.id.pullUp);
        requestStatuesinfo = findViewById(R.id.requestStatus);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        chechGpsStatas();

        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // mLogout = findViewById(R.id.logout);
        mRequest = findViewById(R.id.makeRequest);
        prob = findViewById(R.id.problems);
        carModel = findViewById(R.id.carModel);
        cartypes = findViewById(R.id.carType);
        requestComponet = findViewById(R.id.component);
        // pullUpbutton = findViewById(R.id.menu);
        //settings = findViewById(R.id.profile);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
        placesClient = Places.createClient(this);
        ArrayAdapter<String> problemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, problems);
        ArrayAdapter<String> carTypesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, carTypes);
        ArrayAdapter<String> carModelsAdaper = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, carModels);
        //loadProblemsDroFirebase();
        // prob.setAdapter(problemsAdapter);
        prob.setThreshold(1);

        cartypes.setAdapter(carTypesAdapter);
        cartypes.setThreshold(1);
        carModel.setAdapter(carModelsAdaper);
        carModel.setThreshold(1);
        getGaragesAround();
        //  checkRequestStatus();


        mRequest.setOnClickListener(v -> {
            recordHistory();
            recordCustomerRequest();
            // confirmationdialog();
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            System.exit(0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.profile) {
            startActivity(new Intent(getApplicationContext(), profile.class));
        } else if (id == R.id.loqout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(menu.this, login.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            Intent intent = new Intent(getApplicationContext(), menu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.profile) {
            Intent intent = new Intent(getApplicationContext(), profile.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (id == R.id.support) {
            Intent intent = new Intent(getApplicationContext(), Users.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "link comming soon";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Thank you ");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } else if (id == R.id.history) {
            Intent intent = new Intent(getApplicationContext(), historyActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void bookAppointment(View view) {
        Intent intent = new Intent(menu.this, MakeAppointment.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void makeRequest(View view) {
        startActivity(new Intent(getApplicationContext(), customer_map.class));
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
                fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            } else {
                checkLocationPermission();
            }
        }

    }

    void checkRequestStatus() {
        ProgressDialog loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("checking pending  Requests");
        loadingBar.setMessage("Please wait, while we are checking the request.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        CollectionReference userRequestData = db.collection("users").document(userId).collection("orders");

        userRequestData.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    String status = Objects.requireNonNull(document.get("status")).toString();
                    if (status.equals("Requesting") || status.equals("Pending")) {

                        mRequest.setText("you Have a pending request");
                        requestStatuesinfo.setVisibility(View.VISIBLE);
                        requestComponet.setVisibility(View.VISIBLE);
                        Toast.makeText(menu.this, status, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    } else {

                        prob.setVisibility(View.VISIBLE);
                        carModel.setVisibility(View.VISIBLE);
                        cartypes.setVisibility(View.VISIBLE);
                        requestStatuesinfo.setVisibility(View.GONE);
                        loadingBar.dismiss();
                        Toast.makeText(menu.this, "no pending request", Toast.LENGTH_SHORT).show();
                    }


                }
            } else {

                prob.setVisibility(View.VISIBLE);
                carModel.setVisibility(View.VISIBLE);
                cartypes.setVisibility(View.VISIBLE);
                requestStatuesinfo.setVisibility(View.GONE);
                loadingBar.dismiss();
                Toast.makeText(menu.this, "no pending request", Toast.LENGTH_SHORT).show();
            }
      /*  if(!task.isSuccessful()){
            prob.setVisibility(View.VISIBLE);
            carModel.setVisibility(View.VISIBLE);
            cartypes.setVisibility(View.VISIBLE);
            requestStatuesinfo.setVisibility(View.GONE);
            loadingBar.dismiss();
        }*/
        });

    }

    //save history
    private void recordHistory() {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customerRequest").child(userId).child("history");
        DatabaseReference history = FirebaseDatabase.getInstance().getReference("history").child(userId);
        String historyKey = history.push().getKey();


        HashMap map = new HashMap();
        map.put("car problem", prob.getText().toString());
        map.put("Date", "");
        map.put("CarType", cartypes.getText().toString());
        map.put("carModel", carModel.getText().toString());
        map.put("Rating", 0);
        map.put("Time", getCurrentTime());
        map.put("Status", "pending");
        //  ref.child(historyKey).updateChildren(map);
        history.child(historyKey).updateChildren(map);


    }


    //check if gps is enabled
    public void chechGpsStatas() {
        LocationManager locationManager = null;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        } catch (Exception e) {
        }
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
        }
        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(menu.this);
            dialog.setMessage("Gps  not Enabled");
            dialog.setPositiveButton("Ok", (dialog1, which) -> {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            });
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        }
    }

    // save the customer request to database
    public void recordCustomerRequest() {
        double lat = mLastLocation.getLatitude();
        double lng = mLastLocation.getLongitude();
        CollectionReference ref = FirebaseFirestore.getInstance().collection("mechanic_requests");
        //GeoFire geoFire = new GeoFire(ref);
        //GeoFirestore geoFirestore = new GeoFirestore(ref);
        FirebaseFirestore dbrequest = FirebaseFirestore.getInstance();

        Map<String, Object> map = new HashMap<>();
        map.put("problem", prob.getText().toString());

        map.put("passedMeks", 0);
        map.put("carType", cartypes.getText().toString());
        map.put("carModel", carModel.getText().toString());
        map.put("customerRating", 0);
        map.put("customerComment", "");
        map.put("mechanicRating", 0);
        map.put("customerId", userId);
        map.put("timestamp", getCurrentTime());
        map.put("status", "Requesting");
        map.put("lng", lng);
        map.put("lat", lat);
        map.put("carLocation", destination);
        map.put("mekId", "");
        map.put("workingStatus", "");
        //String requestKey = dbrequest.document("").toString();
        dbrequest.collection("mechanic_requests").document().set(map)
                .addOnSuccessListener(e -> {
                    Toast.makeText(menu.this, "Request made successful",
                            Toast.LENGTH_SHORT).show();
                    mRequest.setText("Getting your Mechanic...");
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(menu.this, "ERROR" + e.toString(),
                            Toast.LENGTH_SHORT).show();
                    Log.d("TAG", e.toString());
                });
        pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup Here"));
        //2222222z   geoFirestore.setLocation(userId, new GeoPoint(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
    }


    private Long getCurrentTime() {
        Long timeStamp = System.currentTimeMillis() / 1000;
        return timeStamp;
    }

    private int radius = 1;
    private Boolean driverFound = false;
    private String driverFoundID;

    GeoQuery geoQuery;

    //confirmation dialog
    public void confirmationdialog() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.confirm_request, null);
        android.app.AlertDialog.Builder alertDialogBuilderUserInput = new android.app.AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Proceed", (dialogBox, id) -> {
                    Intent intent = new Intent(getApplicationContext(), mpesaAcitivity.class);
                    startActivity(intent);
                })

                .setNegativeButton("Cancel",
                        (dialogBox, id) -> {
                            dialogBox.cancel();
                        });

        android.app.AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    // get closest mechanic
    private void getClosestMechanic() {
        DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference().child("availableMechanics");

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

    //get the mechanic location and mechanic information
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

    //check permission
    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    new android.app.AlertDialog.Builder(this)
                            .setTitle("give permission")
                            .setMessage("allow mekpap to use location")
                            .setPositiveButton("OK", (dialogInterface, i) ->
                                    ActivityCompat.requestPermissions(menu.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1))
                            .create()
                            .show();
                } else {
                    ActivityCompat.requestPermissions(menu.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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

    Animation uptodown, downtoup;

    //load appointment interface
    public void loadAppoinment(View view) {
        double lat = mLastLocation.getLatitude();
        double lng = mLastLocation.getLongitude();
        Intent intent = new Intent(menu.this, MakeAppointment.class);
        Bundle b = new Bundle();
        b.putString("carModel", carModel.getText().toString());
        b.putString("carType", cartypes.getText().toString());
        b.putString("carProblem", prob.getText().toString());
        b.putDouble("latitude", lat);
        b.putDouble("longitude", lng);
        intent.putExtras(b);
        startActivity(intent);

    }

    public void pullUp(View view) {
        pullUpbutton.setAnimation(downtoup);
        pullUpbutton.setVisibility(View.VISIBLE);
        pullUp.setVisibility(View.GONE);
        pulldown.setVisibility(View.VISIBLE);

    }

    public void pulldownbt(View view) {
        pullUpbutton.setAnimation(uptodown);
        pullUpbutton.setVisibility(View.GONE);
        pullUp.setVisibility(View.VISIBLE);
        pulldown.setVisibility(View.GONE);
    }


    boolean getDriversAroundStarted = false;
    List<Marker> markers = new ArrayList<Marker>();

    private void getGaragesAround() {
        getDriversAroundStarted = true;
        DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference().child("availableMechanics");
        try {
            GeoFire geoFire = new GeoFire(driverLocation);
            GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(mLastLocation.getLongitude(), mLastLocation.getLatitude()), 15);
            if (geoQuery == null) {
                Toast.makeText(this, "we are sorry no garage around here", Toast.LENGTH_SHORT).show();
            } else {

                geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                    @Override
                    public void onKeyEntered(String key, GeoLocation location) {

                        for (Marker markerIt : markers) {
                            if (markerIt.getTag().equals(key))
                                return;
                        }

                        LatLng driverLocation = new LatLng(location.latitude, location.longitude);

                        Marker mDriverMarker = mMap.addMarker(new MarkerOptions().position(driverLocation).title(key).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car)));
                        mDriverMarker.setTag(key);

                        markers.add(mDriverMarker);


                    }

                    @Override
                    public void onKeyExited(String key) {
                        for (Marker markerIt : markers) {
                            if (markerIt.getTag().equals(key)) {
                                markerIt.remove();
                            }
                        }
                    }

                    @Override
                    public void onKeyMoved(String key, GeoLocation location) {
                        for (Marker markerIt : markers) {
                            if (markerIt.getTag().equals(key)) {
                                markerIt.setPosition(new LatLng(location.latitude, location.longitude));
                            }
                        }
                    }

                    @Override
                    public void onGeoQueryReady() {
                    }

                    @Override
                    public void onGeoQueryError(DatabaseError error) {

                    }
                });

            }
        } catch (NullPointerException e) {
            Toast.makeText(this, "we are sorry no garage around here", Toast.LENGTH_LONG).show();

        }

    }
}
