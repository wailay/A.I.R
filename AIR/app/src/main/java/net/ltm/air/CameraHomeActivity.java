package net.ltm.air;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.graphics.Matrix;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
public class CameraHomeActivity extends AppCompatActivity {
    Button camBtn;
    ImageView imageView;
    RequestQueue queue;
    String predictionRes;
    TextView messageToUser;
    ImageView reclybaleImg;
    String SERVER_URL ="http://10.200.46.211:5000/image";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_home);

        queue = Volley.newRequestQueue(this);
        camBtn = findViewById(R.id.camBtn);
        imageView = findViewById(R.id.imageView2);
        messageToUser = findViewById(R.id.message);
        reclybaleImg = findViewById(R.id.recyclable);
        camBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                openCamera(v);
            }
        });
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    static final int REQUEST_TAKE_PHOTO = 1;
    Uri photoURI;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException ex) {
                System.out.println(ex.toString());
            }

            System.out.println("AFTER EX");

            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "net.ltm.air.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }
        }
    }
    //When camera button is clicked
    public void openCamera(View view){
        dispatchTakePictureIntent();
    }

    public String toStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    //The camera returned the image;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        imageView.setImageURI(photoURI);

        Bitmap bitmap;

        String base64Image;
        JSONObject jsonBody = new JSONObject();
        bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);


        base64Image = toStringImage(rotatedBitmap);
        try {
            jsonBody.put("image", base64Image);
        }catch (Exception e){
            System.out.println(e.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, SERVER_URL, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            predictionRes = response.getString("result");
                            System.out.println(predictionRes);
                            if(predictionRes.equals("plastic bottle")){
                                reclybaleImg.setImageResource(R.drawable.semi_rec);
                                messageToUser.setText("This seems to be a plastic bottle. This item is recyclable, but do not forget " +
                                        "to take off the caps !");
                            }else if (predictionRes.equals("pizza_box")){
                                reclybaleImg.setImageResource(R.drawable.not_rec);
                                messageToUser.setText("This seems to be a pizza box. This item is not recyclable, but it can be compostable !");
                            }else if( predictionRes.equals("Can")){
                                reclybaleImg.setImageResource(R.drawable.recyc);
                                messageToUser.setText("This seems to be a soda can. This item is entirely recyclable !");
                            }else if( predictionRes.equals("carton juice box")){
                                reclybaleImg.setImageResource(R.drawable.semi_rec);
                                messageToUser.setText("This seems to be a juice box. This is item is recyclable, note that you can not recycle the straw !");
                            }else if( predictionRes.equals("chips bag")){
                                reclybaleImg.setImageResource(R.drawable.not_rec);
                                messageToUser.setText("This seems to be a bag of chips. This item is not recyclable !");
                            }
                        }catch (Exception e){
                            System.out.println(e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("EROOOOOOOOOOOOOOR " + error.toString());

                    }
                });

        //send to server
        queue.add(jsonObjectRequest);


        //Delete image from memory
        File fdelete = new File(currentPhotoPath);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                System.out.println("file Deleted :" );
            } else {
                System.out.println("file not Deleted :");
            }
        }

    }


}
