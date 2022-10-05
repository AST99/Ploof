package com.astdev.ploof;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.astdev.ploof.databinding.ActivityConnexionBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ConnexionPage extends AppCompatActivity {

    private TextInputEditText editTxtmail;
    private TextInputEditText editTxtPassWrd;
    public static FirebaseAuth mAuth;
    private LinearLayout mainLayout, verificationLayout, numeroLayout, mail_layout;

    ActivityConnexionBinding binding;

    /*******************OTP************************/
    //Si l'envoie du code OTP échoue, "forceResending permet de renvoyer un autre code
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private String mVerificationId;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityConnexionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(ConnexionPage.this, MainFragment.class));
        }

        progressDialog = new ProgressDialog(ConnexionPage.this, R.style.MyAlertDialogStyle);
        progressDialog.setCanceledOnTouchOutside(false);

        this.mainLayout = findViewById(R.id.mainLayout);
        this.mail_layout = findViewById(R.id.mail_layout);
        this.verificationLayout = findViewById(R.id.verificationOTP);
        verificationLayout.setVisibility(View.GONE);
        this.numeroLayout = findViewById(R.id.numeroLayout);
        this.editTxtPassWrd = findViewById(R.id.passWrd);
        this.editTxtmail = findViewById(R.id.Mail);

        selectedTab();

        binding.btnOTPSubmit.setOnClickListener(view -> {
            verificationLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
        });

        binding.btnOTPContinuer.setOnClickListener(view -> {
            verificationLayout.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.GONE);
            phoneConnexion();
            String strPhone = Objects.requireNonNull(binding.phoneNumber.getText()).toString().trim();
            if (TextUtils.isEmpty(strPhone)){
                binding.phoneNumber.setError("Votre numéro de téléphone est requis!");
                binding.phoneNumber.requestFocus();
            }
            else startPhoneNumberVerification(strPhone);
        });

        binding.btnConnecter.setOnClickListener(view -> mailAndPassWrdConnexion());

        binding.btnInscription.setOnClickListener(view ->{
            startActivity(new Intent(getApplicationContext(),InscriptionPage.class));
            this.finish();
        });
    }

    private void selectedTab(){
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        mail_layout.setVisibility(View.GONE);
                        numeroLayout.setVisibility(View.VISIBLE);
                        editTxtmail.setText("");
                        break;
                    case 1:
                        mail_layout.setVisibility(View.VISIBLE);
                        numeroLayout.setVisibility(View.GONE);
                        editTxtmail.setText("");
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    /******************************Connexion avec le mail et le mot de passe***************************/
    private void mailAndPassWrdConnexion(){

        String mail = Objects.requireNonNull(binding.Mail.getText()).toString().trim();
        String passWrd = Objects.requireNonNull(binding.passWrd.getText()).toString().trim();

        if (mail.equals("")){
            if (TextUtils.isEmpty(editTxtmail.getText())){
                editTxtmail.setError("Votre e-mail est requis!");
                editTxtmail.requestFocus();
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(editTxtmail.getText()).
                    toString().trim()).matches()){
                editTxtmail.setError("Merci de fournir un email valide!");
                editTxtmail.requestFocus();
            }
        }
        else if (passWrd.equals("")){
            if (TextUtils.isEmpty(editTxtPassWrd.getText())){
                editTxtPassWrd.setError("Veuillez saisir un mot de passe!");
                editTxtPassWrd.requestFocus();
            }
            if (editTxtPassWrd.length()<5){
                editTxtPassWrd.setError("La longueur minimale du mot de passe doit être de 5 caractères");
                editTxtPassWrd.requestFocus();
            }
        }
        else {
            progressDialog.setMessage("Connexion en cours...!");
            progressDialog.show();
            try {
                mAuth.signInWithEmailAndPassword(mail, passWrd).addOnSuccessListener(authResult -> {
                    progressDialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), MainFragment.class));
                    editTxtPassWrd.setText("");
                    editTxtmail.setText("");
                    this.finish();
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(ConnexionPage.this,"La connexion a échouer !\n Vérifiez vos" +
                            " informations ou créez un compte !", Toast.LENGTH_LONG).show();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
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
                Toast.makeText(ConnexionPage.this,""+e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d("TAG","onCodeSend: "+verificationId);
                mVerificationId = verificationId;
                forceResendingToken = token;

                progressDialog.dismiss();
                binding.mainLayout.setVisibility(View.GONE);
                binding.verificationOTP.setVisibility(View.VISIBLE);

                Toast.makeText(ConnexionPage.this,"Code de vérification envoyé", Toast.LENGTH_SHORT).show();

                binding.txtViewSendTo.setText("Veuillez entrer le code de vérification " +
                        "qui a été \nenvoyer au "+ Objects.requireNonNull(binding.phoneNumber
                        .getText()).toString().trim());

            }
        };

        binding.txtViewResendCode.setOnClickListener(view -> {
            String strPhone = Objects.requireNonNull(binding.phoneNumber.getText()).toString().trim();
            if (TextUtils.isEmpty(strPhone)){
                binding.phoneNumber.setError("Votre numéro de téléphone est requis!");
                binding.phoneNumber.requestFocus();
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
        progressDialog.setMessage("Connexion en cours...!");
        progressDialog.show();

        mAuth.signInWithCredential(credential).addOnSuccessListener(authResult -> {
            progressDialog.dismiss();
            String phone = Objects.requireNonNull(mAuth.getCurrentUser()).getPhoneNumber();

            progressDialog.dismiss();
            startActivity(new Intent(getApplicationContext(), MainFragment.class));
            Toast.makeText(ConnexionPage.this,"connecté en tant que "+phone, Toast.LENGTH_SHORT).show();
            this.finish();

        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(ConnexionPage.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}