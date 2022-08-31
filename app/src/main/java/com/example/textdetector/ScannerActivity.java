package com.example.textdetector;

import static android.Manifest.permission.CAMERA;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptions;




import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import android.content.pm.PackageManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ScannerActivity extends AppCompatActivity {

    private Button btn_snap,btn_detect, btn_camera;
    private TextView txt_detect;
    private Bitmap bitmap;
    private ImageView imageView;
    static final int REQUEST =1;
    public static int REQUEST_CODE = 123;
    CropImageView cropImageView;
    boolean snap=false;
    boolean camera = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        btn_detect = findViewById(R.id.btn_detect);
        btn_snap = findViewById(R.id.btn_snap);
        txt_detect = findViewById(R.id.detected_text);
        imageView = findViewById(R.id.IVcapture);
        btn_camera= findViewById(R.id.btn_camera);

        btn_snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(checkPermissions()){
//                    captureImage();
//                }
//                else{
//                    requestPermissions();
//                }
                {
                    snap = true;
                    camera =false;
                    CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(ScannerActivity.this);
//                    chooseImage();

//                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, REQUEST);


                }

            }
        });
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snap = false;
                camera =true;
                chooseImage();

            }
        });

        btn_detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                detectText();
                String scanned = txt_detect.getText().toString();
                copyToClipboard(scanned);

            }
        });}


//
//    private boolean checkPermissions(){
//        int cameraPermission = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
//        return cameraPermission== PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestPermissions(){
//        int PERMISSION_CODE = 200;
//        ActivityCompat.requestPermissions(this,new String[]{CAMERA},PERMISSION_CODE);
//    }
//
//    private void captureImage() {
//
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if(intent.resolveActivity(getPackageManager())!=null) {
//            startActivityForResult(intent, REQUEST);
//
//        }
//
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(grantResults.length>0){
//            boolean camera_permission = grantResults[0]==PackageManager.PERMISSION_GRANTED;
//            if(camera_permission){
//                Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show();
//            }
//            else{
//                Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
//                captureImage();
//            }
//        }
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==REQUEST && resultCode==RESULT_OK){
//            Bundle extras = data.getExtras();
//            bitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(bitmap);
//
//        }
//
//    }
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (snap) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imageView.setImageURI(resultUri);
//            cropImageView.setImageUriAsync(resultUri);

//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
//                FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
//
//                FirebaseVision firebaseVision = FirebaseVision.getInstance();
//                FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = firebaseVision.getOnDeviceTextRecognizer();
//
//                //Process the Image
//                Task<FirebaseVisionText> task = firebaseVisionTextRecognizer.processImage(firebaseVisionImage);
//
//                task.addOnSuccessListener(firebaseVisionText -> {
//                    //Set recognized text from image in our TextView
//                    String text = firebaseVisionText.getText();
//                    txt_detect.setText(text);
//                });
//                task.addOnFailureListener(e -> {
//                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                });
                blonActivityResult(requestCode, resultCode, data, resultUri);
            }
        }
    }
    if (camera) {
        if (requestCode == REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);
//            getImageUri(this,bitmap);
//            blonActivityResult(requestCode, resultCode, data, getImageUri(this,bitmap));
            FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);

            FirebaseVision firebaseVision = FirebaseVision.getInstance();
            FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = firebaseVision.getOnDeviceTextRecognizer();

            //Process the Image
            Task<FirebaseVisionText> task = firebaseVisionTextRecognizer.processImage(firebaseVisionImage);

            task.addOnSuccessListener(firebaseVisionText -> {
                //Set recognized text from image in our TextView
                String text = firebaseVisionText.getText();
                txt_detect.setText(text);
            });
            task.addOnFailureListener(e -> {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }


    }
}

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public void blonActivityResult(int requestCode, int resultCode, Intent data, Uri resultUri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVision firebaseVision = FirebaseVision.getInstance();
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = firebaseVision.getOnDeviceTextRecognizer();

        //Process the Image
        Task<FirebaseVisionText> task = firebaseVisionTextRecognizer.processImage(firebaseVisionImage);

        task.addOnSuccessListener(firebaseVisionText -> {
            //Set recognized text from image in our TextView
            String text = firebaseVisionText.getText();
            txt_detect.setText(text);
        });
        task.addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void chooseImage() {
        //Check permission


        //Open the camera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST);
    }
//        @Override
//        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
//        @NonNull int[] grantResults)
//        {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//            if (requestCode == REQUEST) {
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
//                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, REQUEST);
//                } else {
//                    Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//
//        @Override
//        protected void onActivityResult( int requestCode, int resultCode, Intent data){
//            super.onActivityResult(requestCode, resultCode, data);
//            if (requestCode == REQUEST && resultCode == RESULT_OK) {
//                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//                imageView.setImageBitmap(bitmap);
//                detectText(bitmap);
//            }
//        }
//
//
//    private void detectText(Bitmap bitmap) {
//
//        try {
//            InputImage image = InputImage.fromBitmap(bitmap, 270);
//            TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
//            Task<Text> result = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
//            @Override
//            public void onSuccess(@NonNull Text text) {
////                if(text==null) {
////                Toast.makeText(ScannerActivity.this,"It is Null", Toast.LENGTH_SHORT).show();}
////                else Toast.makeText(ScannerActivity.this,"It is Null", Toast.LENGTH_SHORT).show();
////                Toast.makeText(ScannerActivity.this, text.getText(), Toast.LENGTH_SHORT).show();
//                StringBuilder result = new StringBuilder();
//                for (Text.TextBlock block : text.getTextBlocks()) {
//                    String blockText = block.getText();
//
//                    Point[] point = block.getCornerPoints();
//                    Rect blockFrame = block.getBoundingBox();
//                    for (Text.Line line : block.getLines()) {
//                        String lineText = line.getText();
//                        Point[] line_Corner_Point = line.getCornerPoints();
//                        Rect lineRect = line.getBoundingBox();
//                        for (Text.Element element : line.getElements()) {
//                            String element_text = element.getText();
//                            result.append(element_text);
//                        }
//                        Toast.makeText(ScannerActivity.this, blockText, Toast.LENGTH_SHORT).show();
//                        txt_detect.setText(blockText);
//                    }
//
//
//                }
//
//            }
//
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(ScannerActivity.this, "Could Not Process Image." + e.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
//        catch (Exception e){
//
//            Toast.makeText(this, "Okay", Toast.LENGTH_SHORT).show();
//        }
//    }
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //Extract the image
//        Bundle bundle = data.getExtras();
//        Bitmap bitmap = (Bitmap) bundle.get("data");
//        imageView.setImageBitmap(bitmap);
//
//        //Create a FirebaseVisionImage object from your image/bitmap.
//        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
//
//        FirebaseVision firebaseVision = FirebaseVision.getInstance();
//        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = firebaseVision.getOnDeviceTextRecognizer();
//
//        //Process the Image
//        Task<FirebaseVisionText> task = firebaseVisionTextRecognizer.processImage(firebaseVisionImage);
//
//        task.addOnSuccessListener(firebaseVisionText -> {
//            //Set recognized text from image in our TextView
//            String text = firebaseVisionText.getText();
//            txt_detect.setText(text);
//        });
//        task.addOnFailureListener(e -> {
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        });
//    }
    private void copyToClipboard(String text){
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Copied", text);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(ScannerActivity.this, "The text has been copied to Clipboard.", Toast.LENGTH_SHORT).show();

    }
}