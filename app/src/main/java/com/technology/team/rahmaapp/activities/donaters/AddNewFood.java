package com.technology.team.rahmaapp.activities.donaters;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.technology.team.rahmaapp.R;
import com.technology.team.rahmaapp.adapters.MyAdapter;
import com.technology.team.rahmaapp.adapters.donaterAdapter.AddressAdapter;
import com.technology.team.rahmaapp.classes.LocaleShared;
import com.technology.team.rahmaapp.classes.Urls;
import com.technology.team.rahmaapp.classes.Util;
import com.technology.team.rahmaapp.models.AdressListModel;
import com.technology.team.rahmaapp.models.SpinnerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddNewFood extends AppCompatActivity {

    TextView marqueeTxt;
    CheckBox chMoreThan, chLessThan;
    CardView liAddImage;
    ImageView ivDonate;
    EditText etDesc;
    Spinner spAddress;
    FrameLayout addNewAddress;
    Button btnPublishAddress;
    private String id;
    ArrayList<SpinnerModel> arrayList;
    int SELECT_FILE = 1;
    private String encodedImage;
    Bitmap bm;
    private String IMAGE;
    EditText etHomeNum, etflatNum;
    EditText etAddress;
    Button btnAdd;
    private String name;
    private String finalName, homeNum, floorNum;
    private Dialog d;
    private String addressId;
    private boolean isClicked;
    int PLACE_PICKER_REQUEST = 2;
    private double longitude, latitude;
    private String substring;
    int moreThan,lessThan;
    LinearLayout liImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food);
        LocaleShared localeShared = new LocaleShared(this);
        id = localeShared.getId();
        arrayList = new ArrayList<>();
        setUpPermissions();
        setUpRefrence();
    }

    private void setUpRefrence() {
        marqueeTxt = findViewById(R.id.mywidget);
        marqueeTxt.setSelected(true);  // Set focus to the textview
        chMoreThan = findViewById(R.id.chMoreThan);
        chLessThan = findViewById(R.id.chLessThan);
        liAddImage = findViewById(R.id.liAddImage);
        ivDonate = findViewById(R.id.ivDonate);
        etDesc = findViewById(R.id.etDesc);
        spAddress = findViewById(R.id.spAddress);
        addNewAddress = findViewById(R.id.addNewAddress);
        btnPublishAddress = findViewById(R.id.btnPublishAddress);
        liImage=findViewById(R.id.liImage);
        getAddressList();

        addNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClicked = true;
                showPopUpAddAddress();
            }
        });

        chLessThan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    lessThan=1;
                    chMoreThan.setChecked(false);
                }else {
                    lessThan=0;
                    chMoreThan.setChecked(true);
                    chLessThan.setChecked(false);
                }
            }
        });

        chMoreThan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    moreThan=1;
                    chLessThan.setChecked(false);
                }else {
                    moreThan=0;
                    chLessThan.setChecked(true);
                    chMoreThan.setChecked(false);
                }
            }
        });

        liAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        btnPublishAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDonate();
            }
        });
    }



    private void getAddressList() {
        AndroidNetworking.initialize(this);
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        AndroidNetworking.post(Urls.getUSerAddress)
                .addBodyParameter("user_id", id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals("1")) {
                                JSONArray user_addresses = response.getJSONArray("user_addresses");
                                for (int i = 0; i < user_addresses.length(); i++) {
                                    JSONObject jsonObject = user_addresses.getJSONObject(i);
                                    SpinnerModel model = new SpinnerModel();
                                    model.setName(jsonObject.getString("title"));
                                    model.setId(jsonObject.getString("id"));
                                    arrayList.add(model);
                                }
                                if (arrayList.size() > 0) {
                                    spAddress.setAdapter(new MyAdapter(AddNewFood.this, arrayList));
                                    spAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            addressId = arrayList.get(i).getId();
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }


    private void showPopUpAddAddress() {
        d = new Dialog(this);
        d.setContentView(R.layout.popup_add_address);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        d.getWindow().setLayout(width, height);
        d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        etAddress = d.findViewById(R.id.etAddress);
        etHomeNum = d.findViewById(R.id.etHomeNum);
        etflatNum = d.findViewById(R.id.etflatNum);
        btnAdd = d.findViewById(R.id.btnAdd);
        etAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlacePicker();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalName = name;
                homeNum = etHomeNum.getText().toString();
                floorNum = etflatNum.getText().toString();
                d.dismiss();
            }
        });
        d.setCanceledOnTouchOutside(false);
        d.show();
    }

    private void showPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    private void setUpPermissions() {
        String per[] = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(this, per, 1);
    }

    private void selectImage() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, "Select File"),
                SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                liImage.setVisibility(View.GONE);
                onSelectFromGalleryResult(data);
            } else if (requestCode == PLACE_PICKER_REQUEST) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format(String.valueOf(place.getAddress()));
                LatLng latLng = place.getLatLng();
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                etAddress.setText(toastMsg);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String selectedImagePath = cursor.getString(column_index);
        BitmapFactory.Options options = new BitmapFactory.Options();
        substring = selectedImagePath.substring(selectedImagePath.lastIndexOf(".") + 1);
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);
        substring = selectedImagePath.substring(selectedImagePath.lastIndexOf(".") + 1);
        IMAGE = Util.mEncodeToBase64(bm, Bitmap.CompressFormat.JPEG, 100);
        Bitmap finalImage = null;
        try {
            ExifInterface ei = new ExifInterface(selectedImagePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            Log.e("orientation", orientation + "");
            //finalImage = rotateImage(bm, 270);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    finalImage = rotateImage(bm, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    finalImage = rotateImage(bm, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    finalImage = rotateImage(bm, 270);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (finalImage != null) {
            ivDonate.setImageBitmap(finalImage);
        } else {
            ivDonate.setImageBitmap(bm);
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }


    private void addDonate() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.uploading));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.makeNewDonation, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", "" + response);
                try {
                    progressDialog.dismiss();
                    JSONObject object=new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("1")){
                        Toast.makeText(AddNewFood.this, getString(R.string.DonationAdded), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("error", "" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (IMAGE.contains("\n")){
                    IMAGE = IMAGE.replace("\n", "");
                }
                Map<String, String> param = new HashMap<>();
                param.put("user_id", id);
                param.put("image", "data:image/"+substring+";base64,"+IMAGE);
                param.put("title", "Food");
                param.put("description", etDesc.getText().toString());
                param.put("food", "1");
                param.put("needs", "0");
                param.put("address_id", addressId);
                param.put("city_id", "0");
                param.put("address_title", "" + finalName);
                param.put("address_house_number", "" + homeNum);
                param.put("address_floor_number", "" + floorNum);
                param.put("address_lat", "" + "" + latitude);
                param.put("address_lng", "" + "" + longitude);
                param.put("for_more_than_10",""+moreThan);
                param.put("for_less_than_10",""+lessThan);
                return param;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);


    }
}
