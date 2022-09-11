package com.astdev.ploof;

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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ConnexionPage extends AppCompatActivity {

    private TabLayout tabLayout;
    private TextInputEditText editTxtmail, editTxtPassWrd, editTxtPhone;
    private TextInputLayout editTxtTitle;

    private FirebaseAuth mAuth;
    private String choix="";
    private String phone, mail, passWrd;
    private LinearLayout mainLayout, verificationLayout, numeroLayout, mail_layout;

    /*******************OTP************************/
    private ActivityConnexionBinding binding;
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

        choix="tel";

        progressDialog = new ProgressDialog(ConnexionPage.this, R.style.MyAlertDialogStyle);
        progressDialog.setCanceledOnTouchOutside(false);

        this.tabLayout = findViewById(R.id.tabLayout);
        this.mainLayout = findViewById(R.id.mainLayout);
        this.mail_layout = findViewById(R.id.mail_layout);
        this.verificationLayout = findViewById(R.id.verificationOTP);
        verificationLayout.setVisibility(View.GONE);

        this.numeroLayout = findViewById(R.id.numeroLayout);
        this.editTxtPassWrd = findViewById(R.id.passWrd);
        this.editTxtPhone = findViewById(R.id.phoneNumber);
        this.editTxtmail = findViewById(R.id.Mail);
        this.editTxtTitle = findViewById(R.id.titleEdit);

        selectedTab();

        binding.btnOTPSubmit.setOnClickListener(view -> {
            verificationLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
        });

        binding.btnOTPContinuer.setOnClickListener(view -> {
            verificationLayout.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.GONE);
        });

        binding.btnConnecter.setOnClickListener(view -> mailAndPassWrdConnexion());
        binding.btnOTPContinuer.setOnClickListener(view -> phoneConnexion());

       binding.btnInscription.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),InscriptionPage.class)));
    }

    private void selectedTab(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        mail_layout.setVisibility(View.GONE);
                        numeroLayout.setVisibility(View.VISIBLE);
                        editTxtmail.setText("");
                        choix = "tel";break;
                    case 1:
                        mail_layout.setVisibility(View.VISIBLE);
                        numeroLayout.setVisibility(View.GONE);
                        editTxtmail.setText("");
                        choix = "mail"; break;
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
        ProgressDialog progressDialog = new ProgressDialog(ConnexionPage.this, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Connexion en cours...!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(mail, passWrd).addOnCompleteListener(task -> {

            if (task.isSuccessful()){
                progressDialog.dismiss();
                startActivity(new Intent(getApplicationContext(), MainFragment.class));
                editTxtPassWrd.setText("");
                editTxtmail.setText("");
                //this.finish();
            }
            else {
                progressDialog.dismiss();
                Toast.makeText(ConnexionPage.this,"La connexion a échouer !\n Vérifiez vos" +
                        " informations ou créez un compte !", Toast.LENGTH_LONG).show();
            }
        });
    }

    /****************************Connexion pa numéro de téléphone*************************************/
    private void phoneConnexion(){

        /*OTP*/
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

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d("TAG","onCodeSend: "+verificationId);
                mVerificationId = verificationId;
                forceResendingToken = token;

                progressDialog.dismiss();
                binding.mainLayout.setVisibility(View.GONE);
                binding.verificationOTP.setVisibility(View.VISIBLE);

                Toast.makeText(ConnexionPage.this,"Code de vérification envoyé", Toast.LENGTH_LONG).show();

                binding.txtViewSendTo.setText("Veuillez entrer le code de vérification " +
                        "qui a été \nenvoyer au "+binding.phoneNumber.getText().toString().trim());

            }
        };
        binding.btnOTPContinuer.setOnClickListener(view ->{
            String strPhone = Objects.requireNonNull(binding.phoneNumber.getText()).toString().trim();
            if (TextUtils.isEmpty(strPhone)){
                binding.phoneNumber.setError("Votre numéro de téléphone est requis!");
                binding.phoneNumber.requestFocus();
            }
            else startPhoneNumberVerification(strPhone);
        });

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
        /*Fin OTP */

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
            String phone = mAuth.getCurrentUser().getPhoneNumber();
            startActivity(new Intent(getApplicationContext(), MainFragment.class));
            Toast.makeText(ConnexionPage.this,"connecté en tant que "+phone, Toast.LENGTH_LONG).show();

        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(ConnexionPage.this,""+e.getMessage(), Toast.LENGTH_LONG).show();
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

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber("+225"+phone)
                .setTimeout(60L, TimeUnit.SECONDS).setActivity(this)
                .setCallbacks(mCallBacks).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    /****************************Gestion des erreurs au niveau des champs de saisie******************/
    private void textBoxError(){
        try {
            if (choix.equals("mail")){
                if (TextUtils.isEmpty(editTxtmail.getText())){
                    editTxtmail.setError("Votre e-mail est requis!");
                    editTxtmail.requestFocus();
                    return;
                }
                else mail = Objects.requireNonNull(editTxtmail.getText()).toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(editTxtmail.getText()).
                        toString().trim()).matches()){
                    editTxtmail.setError("Merci de fournir un email valide!");
                    editTxtmail.requestFocus();
                    return;
                }
            }

            if (choix.equals("tel")){
                if (TextUtils.isEmpty(editTxtmail.getText())){
                    editTxtmail.setError("Votre numéro de téléphone est requis!");
                    editTxtmail.requestFocus();
                    return;
                }
                else phone = Objects.requireNonNull(editTxtmail.getText()).toString().trim();
            }

            if (TextUtils.isEmpty(editTxtPassWrd.getText())){
                editTxtPassWrd.setError("Veuillez saisir un mot de passe!");
                editTxtPassWrd.requestFocus();
                return;
            }
            else passWrd = Objects.requireNonNull(editTxtPassWrd.getText()).toString().trim();
            if (editTxtPassWrd.length()<5){
                editTxtPassWrd.setError("La longueur minimale du mot de passe doit être de 5 caractères");
                editTxtPassWrd.requestFocus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
           reload();
    }

    private void reload() { }
}