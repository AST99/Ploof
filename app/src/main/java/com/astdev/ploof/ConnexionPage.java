package com.astdev.ploof;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Objects;

public class ConnexionPage extends AppCompatActivity {

    private TabLayout tabLayout;
    private Button btnConnexion, btnInscription;
    private TextInputEditText editTxtPhone_mail, editTxtPassWrd;
    private TextInputLayout editTxtTitle;

    private FirebaseAuth mAuth;
    private String choix="tel";
    private String phone, mail, passWrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        /*ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();*/

        mAuth = FirebaseAuth.getInstance();

        this.tabLayout = findViewById(R.id.tabLayout);
        this.btnConnexion = findViewById(R.id.btnConnecter);
        this.btnInscription = findViewById(R.id.btnInscription);
        this.editTxtPhone_mail = findViewById(R.id.phoneOrMail);
        this.editTxtPassWrd = findViewById(R.id.passWrd);
        this.editTxtTitle = findViewById(R.id.titleEdit);

        selectedTab();

        btnConnexion.setOnClickListener(view -> {
            textBoxError();
            startActivity(new Intent(getApplicationContext(), MainFragment.class));
            /*if (choix.equals("mail"))
                mailAndPassWrdConnexion();*/
            //else if (choix.equals("tel"))
        });
        btnInscription.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),InscriptionPage.class)));
    }

    private void selectedTab(){

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        editTxtTitle.setHint("Numéro de téléphone");
                        editTxtTitle.setEndIconDrawable(R.drawable.ic_phone);
                        editTxtTitle.setPlaceholderText("05000000");
                        editTxtPhone_mail.setInputType(InputType.TYPE_CLASS_PHONE);
                        editTxtPhone_mail.setText("");
                        choix = "tel";
                        break;
                    case 1:
                        editTxtTitle.setHint("E-mail");
                        editTxtTitle.setEndIconDrawable(R.drawable.ic_email);
                        editTxtTitle.setPlaceholderText("exemple@ploof.com");
                        editTxtPhone_mail.setInputType(InputType.TYPE_CLASS_TEXT|
                                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        editTxtPhone_mail.setText("");
                        choix = "mail";
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
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
                editTxtPhone_mail.setText("");
                this.finish();
            }
            else {
                progressDialog.dismiss();
                Toast.makeText(ConnexionPage.this,"La connexion a échouer !\n Vérifiez vos" +
                        " informations ou créez un compte !", Toast.LENGTH_LONG).show();
            }
        });
    }

    /****************************Gestion des erreurs au niveau des champs de saisie******************/
    private void textBoxError(){
        try {
            if (choix.equals("mail")){
                if (TextUtils.isEmpty(editTxtPhone_mail.getText())){
                    editTxtPhone_mail.setError("Votre e-mail est requis!");
                    editTxtPhone_mail.requestFocus();
                    return;
                }
                else mail = Objects.requireNonNull(editTxtPhone_mail.getText()).toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(editTxtPhone_mail.getText()).
                        toString().trim()).matches()){
                    editTxtPhone_mail.setError("Merci de fournir un email valide!");
                    editTxtPhone_mail.requestFocus();
                    return;
                }
            }

            if (choix.equals("tel")){
                if (TextUtils.isEmpty(editTxtPhone_mail.getText())){
                    editTxtPhone_mail.setError("Votre numéro de téléphone est requis!");
                    editTxtPhone_mail.requestFocus();
                    return;
                }
                else phone = Objects.requireNonNull(editTxtPhone_mail.getText()).toString().trim();
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

    private void updateUI(FirebaseUser user) {

    }
}