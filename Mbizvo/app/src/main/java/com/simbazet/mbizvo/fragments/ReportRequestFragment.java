package com.simbazet.mbizvo.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.simbazet.mbizvo.Constants;
import com.simbazet.mbizvo.MainActivity;
import com.simbazet.mbizvo.R;
import com.simbazet.mbizvo.SharedPrefManager;
import com.simbazet.mbizvo.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class ReportRequestFragment extends Fragment implements View.OnClickListener{

    EditText txtDesc, txtTitle, txtLocation;
    Button btnSendImage, btnCamera;
    ImageView imageViewReport;
    ProgressDialog progressDialog;
    private static Context context;
    private Context mCtx;
    private static final int PERMISSIONS_REQUEST = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private Double lat, lon;
    private String imagePath = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_fault, container, false);
        //return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        imageViewReport = view.findViewById(R.id.imageView);
        txtDesc = view.findViewById(R.id.txtCaption);
        txtTitle = view.findViewById(R.id.txtTitle);
        txtLocation = view.findViewById(R.id.txtLocation);
        btnSendImage = view.findViewById(R.id.btnSend);
        btnCamera = view.findViewById(R.id.btnCamera);

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Sending report");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + context.getPackageName()));
            //finish();
            startActivity(intent);
            return;
        }

        //if everything is ok we will open image chooser
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 100);

        btnCamera.setOnClickListener(this);
        btnSendImage.setOnClickListener(this);
    }

    Bitmap bitmappp = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            //imagePath = imageUri.toString();
            imagePath = getPathFromURI(imageUri );
            try {
                //getting bitmap object from uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);

                //displaying selected image to imageview
                imageViewReport.setImageBitmap(bitmap);

                //calling the method uploadBitmap to upload image
                bitmappp = bitmap;
                //uploadBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageViewReport.setImageBitmap(imageBitmap);
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    //image compression
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadImage() {

        //getting the tag from the edittext
        final String Desc = txtDesc.getText().toString();
        final String Title = txtTitle.getText().toString();
        final String Location = txtLocation.getText().toString();
        final String userid = SharedPrefManager.getInstance(context).getUserID().toString();
        final String usertype = SharedPrefManager.getInstance(context).getUserType();

        if (txtDesc.length() < 5){
            Toast.makeText(getContext(), "Describe you fault with at least 5 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        if (txtTitle.length() < 5){
            Toast.makeText(getContext(), "Name you fault with at least 5 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Location.length() < 3){
            Toast.makeText(getContext(), "Name you surburb with at least 3 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if("0.0".equals(String.valueOf(lat)) && "0.0".equals(String.valueOf(lon))){
            int permission = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (permission == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location loc) {
                                // Got last known location. In some rare situations this can be null.
                                if (loc != null) {
                                    lat = loc.getLatitude();
                                    lon = loc.getLongitude();
                                    // Logic to handle location object
                                }
                            }
                        });
            }
            else{
                Toast.makeText(context, "Please grand location permission", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST);
                return;
            }
        }


        progressDialog.show();

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constants.insertRequest,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            if(!obj.getBoolean("error")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Success");
                                builder.setIcon(R.drawable.icons_happy);
                                builder.setMessage(obj.getString("message"));
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        MainActivity.selectedAction.setText("Requests");
                                        MainActivity.fragmentManager.beginTransaction().replace(R.id.containerView, new RequestsFragment()).commit();
                                    }
                                });
                                builder.create();
                                builder.show();
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Error");
                                builder.setIcon(R.drawable.ico_crying);
                                builder.setMessage("Could not upload");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                builder.create();
                                builder.show();
                            }

                            //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userID", userid);
                params.put("userType", usertype);
                params.put("requestTitle", Title);
                params.put("requestDesc", Desc);
                params.put("requestLocation", Location);
                params.put("requestLat", lat.toString());
                params.put("requestLong", lon.toString());
                return params;
            }

            /*
                    * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart("user_"+userid+"_"+imagename + ".jpg", getFileDataFromDrawable(bitmappp))); //png
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(context).add(volleyMultipartRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted
            //Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
            uploadImage();
        } else {
            Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {
        if (view == btnCamera){
            dispatchTakePictureIntent();
            //readGeoTagImage();
        }

        if (view == btnSendImage){
            if (imageViewReport.getDrawable() == null){
                Toast.makeText(getContext(), "Select an image of capture one", Toast.LENGTH_SHORT).show();
                return;
            }
            readGeoTagImage();
            if("0.0".equals(String.valueOf(lat)) && "0.0".equals(String.valueOf(lon))){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Invalid Image");
                builder.setIcon(R.drawable.faults_ico);
                builder.setMessage("This image does not have a location tag, " +
                        "it can not be used. This is because when the image was captured, GPS was off on the camera\n\n" +
                        "Image Data : +\n" +
                        "Latitude : " + lat +" +\n" +
                        "Longitude :" + lon + "\n Do you want to use your current location?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        uploadImage();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                builder.create();
                builder.show();
            }
            else {uploadImage();}
        }

    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    public void readGeoTagImage(){
        Location loc = new Location("");
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            float [] latlong = new float[2] ;
            if(exif.getLatLong(latlong)){
                loc.setLatitude(latlong[0]);
                loc.setLongitude(latlong[1]);
                lat = Double.valueOf(latlong[0]);
                lon = Double.valueOf(latlong[1]);
            }
            else {
                Toast.makeText(context, "Image dose not contain location data", Toast.LENGTH_SHORT).show();
                //return;
            }
            String date = exif.getAttribute(ExifInterface.TAG_DATETIME);
            SimpleDateFormat fmt_Exif = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            loc.setTime(fmt_Exif.parse(date).getTime());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //return loc;
    }

}

