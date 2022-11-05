package com.astdev.ploof;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.astdev.ploof.databinding.ActivityInscriptionPageBinding;
import com.astdev.ploof.models.UsersModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Objects;

public class InscriptionPage extends AppCompatActivity {

    private FirebaseAuth mAuth;

    ActivityInscriptionPageBinding binding;
    ProgressDialog progressDialog;

    private String nomPrenoms,mail,passWrd,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityInscriptionPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(InscriptionPage.this, R.style.MyAlertDialogStyle);
        progressDialog.setCanceledOnTouchOutside(false);

        inscriptionMail();
    }

    private void inscriptionMail(){
        binding.btnSuivant.setOnClickListener(view -> {
            if (TextUtils.isEmpty(binding.InscriptionNomPrenom.getText())){
                binding.InscriptionNomPrenom.setError("Le nom et le prénom sont requis!");
                binding.InscriptionNomPrenom.requestFocus();
            }
            else if (TextUtils.isEmpty(binding.mailInscription.getText())){
                binding.mailInscription.setError("Votre e-mail est requis!");
                binding.mailInscription.requestFocus();
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(binding.mailInscription.getText()).
                    toString().trim()).matches()){
                binding.mailInscription.setError("Merci de fournir un email valide!");
                binding.mailInscription.requestFocus();
            }
            else if (TextUtils.isEmpty(binding.inscriptionPhoneNumber.getText())){
                binding.inscriptionPhoneNumber.setError("Votre numéro de téléphone est requis!");
                binding.inscriptionPhoneNumber.requestFocus();
            }
            else {
                binding.mailLayout.setVisibility(View.GONE);
                binding.passWrdLayout.setVisibility(View.VISIBLE);
                mail = Objects.requireNonNull(binding.mailInscription.getText()).toString().trim();
                nomPrenoms = Objects.requireNonNull(binding.InscriptionNomPrenom.getText()).toString().trim();
                phone = Objects.requireNonNull(binding.inscriptionPhoneNumber.getText()).toString().trim();
            }
        });

       binding.btnInscrire.setOnClickListener(view -> {
           if (TextUtils.isEmpty(binding.passWrdInscription.getText())){
               binding.passWrdInscription.setError("Veuillez saisir un mot de passe!");
               binding.passWrdInscription.requestFocus();
           }
           else if(binding.passWrdInscription.length()<5){
               binding.passWrdInscription.setError("Votre mot de passe doit contenir au minimum 5 caractères");
               binding.passWrdInscription.requestFocus();
           }
           else if (TextUtils.isEmpty(binding.passWrdInscriptionConfirme.getText())){
               binding.passWrdInscriptionConfirme.setError("Confirmez votre mot de passe!");
               binding.passWrdInscriptionConfirme.requestFocus();
           }
           else{
               passWrd = Objects.requireNonNull(binding.passWrdInscription.getText()).toString().trim();
               createUserWithMail(mail,phone,passWrd,nomPrenoms);
           }
        });
    }

    /******************************Inscription avec le mail****************************************/
    //=>m: mail, p: mot de passe, n: nom/prénom
    private void createUserWithMail(String m, String tel, String p, String n){
        try {
            ProgressDialog progressDialog = new ProgressDialog(InscriptionPage.this, R.style.MyAlertDialogStyle);
            progressDialog.setMessage("Inscription en cours...!");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(m,p).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    UsersModel user = new UsersModel(n, m, tel,p,"","",
                            "","","","0","");
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).
                                    getUid()).setValue(user).addOnCompleteListener(task1 -> {
                                if(task1.isSuccessful()){
                                    Toast.makeText(InscriptionPage.this,"Vous compte a bien été enregistré !",
                                            Toast.LENGTH_LONG).show();
                                    mAuth.getCurrentUser();
                                    updateUI();
                                    startActivity(new Intent(getApplicationContext(),ConnexionPage.class));
                                    progressDialog.dismiss();
                                }
                                else {
                                    Toast.makeText(InscriptionPage.this,"Votre inscription n'a" +
                                                    " pas été fait !\n Essayez à nouveau",
                                            Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                            });
                }else {
                    Toast.makeText(InscriptionPage.this,"Votre inscription n'a" +
                            " pas été fait !\n Essayez à nouveau", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUI() {}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),ConnexionPage.class));
        this.finish();
    }
}