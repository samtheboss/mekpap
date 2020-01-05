package com.mekpap.mekPap.customer;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mekpap.mekPap.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MakeAppointment extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, appointmentType.SingleChoiceListener {
    private static final String tag = "MakeAppointment";
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
    AutoCompleteTextView carmodel, carProblem, carType;
    Button request;
    com.google.android.material.textfield.TextInputEditText appointMentdate, appointMentTimw;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userId;
    String problems, getCarmodel, getCartype;
    long startTime;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private Boolean requestBol = false;
    private LatLng latlng;
    Location mLastLocation;
    EditText apptype;
    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointment);
        carmodel = findViewById(R.id.carModel);
        carProblem = findViewById(R.id.problems);
        carType = findViewById(R.id.carType);
        appointMentdate = findViewById(R.id.appointmentdate);
        appointMentTimw = findViewById(R.id.appointmentTime);
        request = findViewById(R.id.makeRequestAppointment);
        apptype = findViewById(R.id.typeOfApp);
        userId = FirebaseAuth.getInstance().getUid();
        appointMentdate.setOnClickListener(view -> showDatePickerDialog());
        onDateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = year + "/" + month + "/" + day;
            appointMentdate.setText(date);

        };
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        ArrayAdapter<String> carTypesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, carTypes);
        ArrayAdapter<String> carModelsAdaper = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, carModels);
        carType.setAdapter(carTypesAdapter);
        carType.setThreshold(1);

        carmodel.setAdapter(carModelsAdaper);
        carmodel.setThreshold(1);
        parsedataFrommap();
        latlng = new LatLng(0.0, 0.0);
        appointMentTimw.setOnClickListener(v -> {

            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(MakeAppointment.this, (timePicker, selectedHour, selectedMinute) ->
                    appointMentTimw.setText(selectedHour + ":" + selectedMinute), hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
            startTime = mcurrentTime.getTimeInMillis();

        });

        apptype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new appointmentType();
                fragment.setCancelable(false);
                fragment.show(getSupportFragmentManager(), "s");
            }
        });
    }

    public void makeAppoitment(View view) {
        ValidateInputs();


    }

    public void parsedataFrommap() {
        problems = getIntent().getExtras().getString("carProblem");
        getCarmodel = getIntent().getExtras().getString("carModel");
        getCartype = getIntent().getExtras().getString("carType");

        carProblem.setText(problems);
        carType.setText(getCartype);
        carmodel.setText(getCarmodel);
    }

    public void ValidateInputs() {
        String problem = carProblem.getText().toString().trim();
        String Model = carmodel.getText().toString().trim();
        String type = carType.getText().toString().trim();
        String date = appointMentdate.getText().toString();
        String appointment =apptype.getText().toString();
        if (TextUtils.isEmpty(problem)) {
            carProblem.setError("Car problem required");
            carProblem.requestFocus();
        }
        if (TextUtils.isEmpty(Model)) {
            carmodel.setError("Car model required");
            carmodel.requestFocus();

        }
       if (TextUtils.isEmpty(type)) {
            carType.setError("Car Type required");
            carType.requestFocus();
        }
         if (TextUtils.isEmpty(date)) {
            appointMentdate.setError("Date required");
            appointMentdate.requestFocus();

        }
         if (date.equals("Date must be in future")) {
            appointMentdate.setError("check date");
            appointMentdate.requestFocus();

        }
        if(appointment.equals("incoming")){
            incomingAppointment();
        }
        if (appointment.equals("outGoing")){
            outgoingAppointment();
        }



    }


    public void showDatePickerDialog() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = month + "/" + dayOfMonth + "/" + year;
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        int thisMont = Calendar.getInstance().get(Calendar.MONTH);
        int thisday = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        if (year >= thisYear && month >= thisMont && dayOfMonth >= thisday) {
            appointMentdate.setText(date);

        } else {
            appointMentdate.setText("Date must be in future");
            Toast.makeText(this, "select a future date", Toast.LENGTH_LONG).show();
        }
    }

    public void alertDialog() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.thankyoumessge, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Ok", (dialogBox, id) -> {
                    dialogBox.cancel();
                })

                .setNegativeButton("Cancel",
                        (dialogBox, id) -> {
                            dialogBox.cancel();
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    public void incomingAppointment() {

        double lat = getIntent().getExtras().getDouble("latitude");
        double lng = getIntent().getExtras().getDouble("longitude");
        Map<String, Object> map = new HashMap<>();
        map.put("Date", appointMentdate.getText().toString());
        map.put("Time", startTime);
        map.put("carYear", "");
        map.put("remWaitTime", 0.0);
        map.put("appointmentId", "");
        map.put("garageId", "");
        map.put("passedMeks", 0);
        map.put("carProblem", carProblem.getText().toString());

        map.put("carType", carType.getText().toString());
        map.put("carModel", carmodel.getText().toString());
        map.put("customerRating", 0);
        map.put("customerComment", "");
        map.put("mechanicRating", 0);
        map.put("customerId", userId);
        map.put("timestamp", getCurrentTime());
        map.put("status", "Requesting");
        map.put("lat", lat);
        map.put("lng", lng);
        map.put("carLocation", "");
        map.put("mekId", "");
        map.put("workingStatus", "");
        db.collection("incoming_appointments").document().set(map)
                .addOnSuccessListener(aVoid -> Toast.makeText(MakeAppointment.this, "Request made successful",
                        Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> {
                    Toast.makeText(MakeAppointment.this, "ERROR" + e.toString(),
                            Toast.LENGTH_SHORT).show();
                    Log.d("TAG", e.toString());
                });
    }
    public void outgoingAppointment() {

        double lat = getIntent().getExtras().getDouble("latitude");
        double lng = getIntent().getExtras().getDouble("longitude");
        Map<String, Object> map = new HashMap<>();
        map.put("Date", appointMentdate.getText().toString());
        map.put("Time", startTime);
        map.put("carYear", "");
        map.put("remWaitTime", 0.0);
        map.put("appointmentId", "");
        map.put("garageId", "");
        map.put("passedMeks", 0);
        map.put("carType", carType.getText().toString());
        map.put("carModel", carmodel.getText().toString());
        map.put("carProblem", carProblem.getText().toString());

        map.put("customerRating", 0);
        map.put("customerComment", "");
        map.put("mechanicRating", 0);
        map.put("customerId", userId);
        map.put("timestamp", getCurrentTime());
        map.put("status", "Requesting");
        map.put("lat", lat);
        map.put("lng", lng);
        map.put("carLocation", "");
        map.put("mekId", "");
        map.put("workingStatus", "");
        db.collection("outgoing_appointments").document().set(map)
                .addOnSuccessListener(aVoid -> Toast.makeText(MakeAppointment.this, "Request made successful",
                        Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> {
                    Toast.makeText(MakeAppointment.this, "ERROR" + e.toString(),
                            Toast.LENGTH_SHORT).show();
                    Log.d("TAG", e.toString());
                });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time = hourOfDay + "/" + minute;
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minutes = Calendar.getInstance().get(Calendar.MINUTE);
    }

    public void puchNotificaction() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 5);

        PendingIntent.getBroadcast(this, 100, new Intent(""), PendingIntent.FLAG_UPDATE_CURRENT);
        //alarmManager.setExact(AlarmManager.RTC,calendar.getTimeInMillis(),);
    }

    private Long getCurrentTime() {
        Long timeStamp = System.currentTimeMillis();
        return timeStamp;
    }

    @Override
    public void onPositiveButtonClicked(String[] list, int position) {
        apptype.setText(list[position]);
    }

    @Override
    public void onNegativeButtonClicked() {

    }





}

