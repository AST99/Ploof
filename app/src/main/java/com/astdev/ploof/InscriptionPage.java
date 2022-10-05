package com.astdev.ploof;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.astdev.ploof.databinding.ActivityInscriptionPageBinding;
import com.astdev.ploof.models.UsersModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class InscriptionPage extends AppCompatActivity {

    private String choix="tel";
    private FirebaseAuth mAuth;

    ActivityInscriptionPageBinding binding;
    ProgressDialog progressDialog;

    /*******************OTP************************/
    //Si l'envoie du code OTP échoue, "forceResending permet de renvoyer un autre code
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private String mVerificationId;

    private int maxLength;
    InputFilter[] FilterArray = new InputFilter[1];

    private String nomPrenoms, phone, mail, passWrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityInscriptionPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(InscriptionPage.this, R.style.MyAlertDialogStyle);
        progressDialog.setCanceledOnTouchOutside(false);

        selectedTab();
        inscriptionMail();
        phoneConnexion();
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

    private void inscriptionMail(){
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

    /******************************Inscription avec le mail****************************************/
    //=>m: mail, p: mot de passe, n: nom/prénom
    private void createUserWithMail(String m, String p, String n){
        try {
            ProgressDialog progressDialog = new ProgressDialog(InscriptionPage.this, R.style.MyAlertDialogStyle);
            progressDialog.setMessage("Inscription en cours...!");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(m,p).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    UsersModel user = new UsersModel(n, m, "",p,"","","","");
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



    /****************************Connexion pa numéro de téléphone*************************************/
    private void phoneConnexion(){
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(InscriptionPage.this,""+e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d("TAG","onCodeSend: "+verificationId);
                mVerificationId = verificationId;
                forceResendingToken = token;

                progressDialog.dismiss();
                binding.mainInscriptionLayout.setVisibility(View.GONE);
                binding.verificationOTPInscription.setVisibility(View.VISIBLE);

                Toast.makeText(InscriptionPage.this,"Code de vérification envoyé", Toast.LENGTH_SHORT).show();

                binding.txtViewSendTo.setText("Veuillez entrer le code de vérification " +
                        "qui a été \nenvoyer au "+ Objects.requireNonNull(binding.inscriptionPhoneNumber
                        .getText()).toString().trim());

            }
        };
        binding.btnOTPContinuerInscription.setOnClickListener(view ->{
            String strPhone = Objects.requireNonNull(binding.inscriptionPhoneNumber.getText()).toString().trim();
            if (TextUtils.isEmpty(strPhone)){
                binding.inscriptionPhoneNumber.setError("Votre numéro de téléphone est requis!");
                binding.inscriptionPhoneNumber.requestFocus();
            }
            else startPhoneNumberVerification(strPhone);
        });

        binding.txtViewResendCode.setOnClickListener(view -> {
            String strPhone = Objects.requireNonNull(binding.inscriptionPhoneNumber.getText()).toString().trim();
            if (TextUtils.isEmpty(strPhone)){
                binding.inscriptionPhoneNumber.setError("Votre numéro de téléphone est requis!");
                binding.inscriptionPhoneNumber.requestFocus();
            }
            else resendVerificationCode(strPhone, forceResendingToken);
        });

        binding.btnOTPSubmit.setOnClickListener(view ->{
            String code = Objects.requireNonNull(binding.codeVerification.getText()).toString().trim();
            if (TextUtils.isEmpty(code)){
                binding.codeVerification.setError("Le code de vérification est requis!");
                binding.codeVerification.requestFocus();
            }
            else verifyPhoneNumberWithCode(mVerificationId, code);
        });
    }

    private void verifyPhoneNumberWithCode(String mVerificationId, String code) {
        progressDialog.setMessage("Vérification du code");
        progressDialog.show();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        progressDialog.setMessage("Inscription en cours...!");
        progressDialog.show();

        mAuth.signInWithCredential(credential).addOnSuccessListener(authResult -> {
            progressDialog.dismiss();
            String phone = Objects.requireNonNull(mAuth.getCurrentUser()).getPhoneNumber();
            String name = Objects.requireNonNull(binding.inscriptionPhoneName.getText()).toString().trim();

            UsersModel user = new UsersModel(name,"",phone,"","","","","");
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance()
                            .getCurrentUser()).getUid()).setValue(user);

            startActivity(new Intent(getApplicationContext(), MainFragment.class));
            Toast.makeText(InscriptionPage.this,"Inscrit(e) en tant que "+phone, Toast.LENGTH_SHORT).show();
            this.finish();

        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(InscriptionPage.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void resendVerificationCode(String phone, PhoneAuthProvider.ForceResendingToken token) {
        progressDialog.setMessage("Vérification du code");
        progressDialog.show();

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone).setTimeout(60L, TimeUnit.SECONDS).setActivity(this)
                .setCallbacks(mCallBacks).setForceResendingToken(token).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void startPhoneNumberVerification(String phone) {
        progressDialog.setMessage("Vérification du numéro de téléphone");
        progressDialog.show();

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber("+226"+phone)
                .setTimeout(60L, TimeUnit.SECONDS).setActivity(this)
                .setCallbacks(mCallBacks).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void updateUI() {}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),ConnexionPage.class));
        this.finish();
    }
}