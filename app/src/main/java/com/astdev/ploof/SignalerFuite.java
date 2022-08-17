package com.astdev.ploof;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignalerFuite extends AppCompatActivity {

    private ImageView imgView, addImgF;
    private TextView txtViewLieuF;
    private Button btnPhoto, contactP;
    private TextInputEditText descFuite, location;
    private TextInputLayout test;

    private String photoPaths; //Chemin de la photo
    Uri photo;
    byte[] imgCompressed;  //L'image après l'avoir compresser

    MapsFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signaler_fuite);

        this.imgView = findViewById(R.id.imgViewF);
        this.addImgF =findViewById(R.id.addPhotoFuite);
        this.txtViewLieuF = findViewById(R.id.lieuFuite);
        this.btnPhoto = findViewById(R.id.btnPrendrePhoto);
        this.contactP = findViewById(R.id.btnContactP);
        this.descFuite = findViewById(R.id.descFuite);
        this.location = findViewById(R.id.Localisation);

        btnPhoto.setOnClickListener(view -> prendreUnePhoto());





        location.setOnClickListener(view ->{
            Toast.makeText(getApplicationContext(), "Localisation", Toast.LENGTH_SHORT).show();
            mFragment = new MapsFragment();
            mFragment.show(getSupportFragmentManager(), "MapsFragment");
        });



        if (Home.atHome){
            imgView.setImageDrawable(getDrawable(R.drawable.maison));
            txtViewLieuF.setText("À domicile");
        }
        if (Home.outsideHome){
            imgView.setImageDrawable(getDrawable(R.drawable.rue));
            txtViewLieuF.setText("Dans la rue");
        }
    }


    /*************************************Prendre une photo****************************************/

    @SuppressLint("QueryPermissionsNeeded")
    public void prendreUnePhoto(){

        Intent prendrePhoto = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (prendrePhoto.resolveActivity(getPackageManager())!= null){
            //création du fichier temporaire
            @SuppressLint("SimpleDateFormat")
            String Temp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File photoDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File photoFile = File.createTempFile("photo"+Temp,".png",photoDir);
                //Enregistrer le chemin complet
                photoPaths = photoFile.getAbsolutePath();

                //création de l'uri qui permet d'obtenir l'accès au fichier
                Uri photoUri = FileProvider.getUriForFile(SignalerFuite.this,
                        SignalerFuite.this.getApplicationContext().getPackageName()+
                                ".provider", photoFile);
                photo=photoUri;

                //Enregistrement de la photo dans le fichier temporaire
                prendrePhoto.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityIfNeeded(prendrePhoto,0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0 && resultCode==RESULT_OK){
            Bitmap image = BitmapFactory.decodeFile(photoPaths);
            //Pour compresser l'image
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            imgCompressed = baos.toByteArray();
            //...
            //image = Bitmap.createScaledBitmap(image,550,350,true);
            addImgF.setImageBitmap(image);
            //addImgF.setScaleType(ImageView.ScaleType.FIT_XY);
           /* try {
                uploadImage();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    }

    // UploadImage method
    /*private void uploadImage() {

        ProgressDialog progressDialog = new ProgressDialog(PrendrePhoto.this, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Enregistrement de la photo...!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(photo != null) {
            StorageReference childRef = storageRef.child("Images/"+ System.currentTimeMillis()+".jpeg");
            //uploading the image
            UploadTask uploadTask = childRef.putBytes(imgCompressed);
            uploadTask.addOnSuccessListener(taskSnapshot ->{
                Toast.makeText(PrendrePhoto.this, "La photo a bien été enregistrée!", Toast.LENGTH_SHORT).show();
                //Obtenir l'url de l'image
                childRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    urlImage = uri.toString();
                    url.setImage(urlImage);
                });
                progressDialog.dismiss();

            });
            uploadTask.addOnFailureListener(e ->
                    Toast.makeText(PrendrePhoto.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show());
        }
        else
            Toast.makeText(PrendrePhoto.this, "Select an image", Toast.LENGTH_SHORT).show();
    }*/
}