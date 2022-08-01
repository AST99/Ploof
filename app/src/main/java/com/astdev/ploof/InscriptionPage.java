package com.astdev.ploof;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.TextureView;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class InscriptionPage extends AppCompatActivity {

    private TabLayout tabLayout;
    private Button btnInscription;
    private TextInputEditText editTxtNomPrenom, editTxtPhone_mail, editTxtPassWrd, confirmeditTxtPassWrd;
    private TextInputLayout editTxtTitle, passWrdTitle;
    private String choix="tel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription_page);

        this.tabLayout = findViewById(R.id.tabLayoutInscription);
        this.editTxtNomPrenom = findViewById(R.id.InscriptionNomPrenom);
        this.editTxtPhone_mail = findViewById(R.id.phoneOrMailInscription);
        this.editTxtPassWrd = findViewById(R.id.passWrdInscription);
        this.confirmeditTxtPassWrd = findViewById(R.id.passWrdConfirm);
        this.btnInscription =findViewById(R.id.btnInscrire);
        this.passWrdTitle = findViewById(R.id.passWrdTitle);
        this.editTxtTitle = findViewById(R.id.titlePhoneOrMail);


        selectedTab();
        inscription();

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

    private void inscription(){
        ProgressDialog progressDialog = new ProgressDialog(InscriptionPage.this, R.color.buttonColor);
        progressDialog.setMessage("Inscription en cours...!");
        progressDialog.setCanceledOnTouchOutside(false);

        btnInscription.setOnClickListener(view -> {
            try {
                if (TextUtils.isEmpty(editTxtNomPrenom.getText())){
                    editTxtNomPrenom.setError("Votre nom et prénoms sont requis!");
                    editTxtNomPrenom.requestFocus();
                    return;
                }

                if (choix.equals("mail")){
                    if (TextUtils.isEmpty(editTxtPhone_mail.getText())){
                        editTxtPhone_mail.setError("Votre e-mail est requis!");
                        editTxtPhone_mail.requestFocus();
                        return;
                    }

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
                }

                if (TextUtils.isEmpty(editTxtPassWrd.getText())){
                    editTxtPassWrd.setError("Veuillez saisir un mot de passe!");
                    editTxtPassWrd.requestFocus();
                    return;
                }
                if (editTxtPassWrd.length()<5){
                    editTxtPassWrd.setError("La longueur minimale du mot de passe doit être de 5 caractères");
                    editTxtPassWrd.requestFocus();
                }

                if (!Objects.requireNonNull(editTxtPassWrd.getText()).toString().trim().
                        equals(Objects.requireNonNull(confirmeditTxtPassWrd.getText()).toString().trim())){
                    confirmeditTxtPassWrd.setError("mot de passe invalide");
                    confirmeditTxtPassWrd.requestFocus();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }

}