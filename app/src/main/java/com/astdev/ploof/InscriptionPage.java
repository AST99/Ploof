package com.astdev.ploof;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.astdev.ploof.databinding.ActivityConnexionBinding;
import com.astdev.ploof.databinding.ActivityInscriptionPageBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class InscriptionPage extends AppCompatActivity {

    private String choix="tel";
    private FirebaseAuth mAuth;

    ActivityInscriptionPageBinding binding;

    private int maxLength;
    InputFilter[] FilterArray = new InputFilter[1];

    private String nomPrenoms, phone, mail, passWrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityInscriptionPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        selectedTab();
        inscription();
    }

    private void selectedTab(){
        binding.tabLayoutInscription.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        binding.mailLayout.setVisibility(View.GONE);
                        binding.numeroLayoutInscription.setVisibility(View.VISIBLE);
                        binding.InscriptionNomPrenom.setText("");
                        binding.mailInscription.setText("");
                        binding.passWrdInscription.setText("");
                        binding.passWrdInscriptionConfirme.setText("");break;
                    case 1:
                        binding.mailLayout.setVisibility(View.VISIBLE);
                        binding.numeroLayoutInscription.setVisibility(View.GONE);break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void inscription(){
       binding.btnInscrire.setOnClickListener(view -> {
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
           else if (TextUtils.isEmpty(binding.passWrdInscription.getText())){
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
               mail = Objects.requireNonNull(binding.mailInscription.getText()).toString().trim();
               passWrd = Objects.requireNonNull(binding.passWrdInscription.getText()).toString().trim();
               nomPrenoms = Objects.requireNonNull(binding.InscriptionNomPrenom.getText()).toString().trim();
               createUserWithMail(mail,passWrd,nomPrenoms);
           }
        });
    }

    //=>m: mail, p: mot de passe, n: nom/prénom
    private void createUserWithMail(String m, String p, String n){

        try {
            ProgressDialog progressDialog = new ProgressDialog(InscriptionPage.this, R.style.MyAlertDialogStyle);
            progressDialog.setMessage("Inscription en cours...!");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(m,p).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    UsersModel user = new UsersModel(n, m, p);
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

    private void updateUI() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),ConnexionPage.class));
        this.finish();
    }
}