package com.mekpap.mekPap.customer;

public class requestmodel {
    String request;
    String carModels;
    String carTypes;

    public requestmodel(String request, String carModels, String carTypes) {
        this.request = request;
        this.carModels = carModels;
        this.carTypes = carTypes;
    }

    public String getCarModels() {
        return carModels;
    }

    public void setCarModels(String carModels) {
        this.carModels = carModels;
    }
    /* try {
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

               }*/
    public String getCarTypes() {
        return carTypes;
    }

    public void setCarTypes(String carTypes) {
        this.carTypes = carTypes;
    }

    public requestmodel() {
    }

    public requestmodel(String request) {
        this.request = request;

    }

    public String getRequest() {
        return request;
    }


    public void setRequest(String request) {
        this.request = request;
    }


}
