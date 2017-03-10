package edu.oakland.textblock;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
//for permissions
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;


//Service is a component that allows apps to run in the background even if the user switches to
//another app.  Since the app will need to keep tabs continuously, well need our class
//to extend Service.
public class GpsServices extends Service implements LocationListener, GpsStatus.Listener {

    private LocationManager mLocationManager;
    Location lastLocation = new Location("last location");
    GpsDataHandler data;
    //current coordinates
    double cLongitude=0 ;
    double cLatitude=0 ;
    //last coordinates
    double lLongitude = 0;
    double lLatitude = 0;


    PendingIntent contentIntent;

    //Checks for GPS location permissions.  The request, if needed,
    // has to come from the Main Activity.  (TO DO)
    public static boolean checkPermission(final Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;
    }

    //Things that are "Services" requires IBinders, but since we dont use we can return null.
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {

    //This will have to work off of the main activity.  Basically it is an intent to start when the
    //main activity starts
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        contentIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, 0);


        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //actually checks the permissions here
        checkPermission(this);
        mLocationManager.addGpsStatusListener( this);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
    }
    public void onLocationChanged(Location location) {

        //Note that normally this should run from Main activity.  Once we get to the point where our
        //main activity is fleshed out, we'll use the commented part instead of initiating a new object
        //right from the GPSDataHandler class.
        //GpsDataHandler = MainActivity.getGpsDataHandler();


        GpsDataHandler gpsDataHandler = new GpsDataHandler();

        lockListener(location);
        //unlockListener(location);

        //gets coordinates from gps
            cLatitude = location.getLatitude();
            cLongitude = location.getLongitude();

            lastLocation.setLatitude(lLatitude);
            lastLocation.setLongitude(lLongitude);
            double distance = lastLocation.distanceTo(location);

        //checks accuracy.  if accurate, saves coordinates and calculates distance.
            if (location.getAccuracy() < distance){
                gpsDataHandler.distanceFunction(distance);

                lLatitude = cLatitude;
                lLongitude = cLongitude;
            }

        //checks if vehicle is stopped
            if (location.hasSpeed()) {
                gpsDataHandler.currentSpeed(location.getSpeed());
                if(location.getSpeed() == 0){
                    new isStillStopped().execute();
                }
            }
            gpsDataHandler.update();

        }
    // looks at speed, if over 10mph, returns true.  can use this method to activate
    public boolean lockListener (Location location){
        double speedNow = (location.getSpeed()) * 2.24;


        if(speedNow >= 15) {
            return true;
        }
        else {
            return false;
        }


    }
    // looks at speed.  if under 10mph for 10secs, returns true.  can be used to deactivate
    public boolean unlockTimer (Location location){
        double speedNow = (location.getSpeed()) * 2.24;
        int timer = 0;


        while(speedNow < 15) {
            try {

                timer++;
                Thread.sleep(1000);
            }
            catch (InterruptedException ie)
            {
                break;
            }



        }



        return false;
    }

    @Override
    public void onGpsStatusChanged(int event) {}

    @Override
    public void onProviderDisabled(String provider) {}
   
    @Override
    public void onProviderEnabled(String provider) {}
   
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    class isStillStopped extends AsyncTask<Void, Integer, String> {
        int timer = 0;
        @Override
        protected String doInBackground(Void... unused) {
            try {
                while (data.getSpeed() == 0) {
                    Thread.sleep(1000);
                    timer++;
                }
            } catch (InterruptedException t) {
                return ("The sleep operation failed");
            }
            return ("return object when task is finished");
        }

        @Override
        protected void onPostExecute(String message) {
            data.setTimeStopped(timer);
        }
    }
}
