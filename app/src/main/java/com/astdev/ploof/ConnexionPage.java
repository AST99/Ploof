package com.astdev.ploof;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ConnexionPage extends AppCompatActivity {

    private TabLayout tabLayout;
    private Button btnConnexion, btnInscription;
    private TextInputEditText editTxtPhone_mail, editTxtPassWrd;
    private TextInputLayout editTxtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        this.tabLayout = findViewById(R.id.tabLayout);
        this.btnConnexion = findViewById(R.id.btnConnecter);
        this.btnInscription = findViewById(R.id.btnInscription);
        this.editTxtPhone_mail = findViewById(R.id.phoneOrMail);
        this.editTxtPassWrd = findViewById(R.id.passWrd);
        this.editTxtTitle = findViewById(R.id.titleEdit);

        selectedTab();

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
                        break;
                    case 1:
                        editTxtTitle.setHint("E-mail");
                        editTxtTitle.setEndIconDrawable(R.drawable.ic_email);
                        editTxtTitle.setPlaceholderText("exemple@ploof.com");
                        editTxtPhone_mail.setInputType(InputType.TYPE_CLASS_TEXT|
                                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        editTxtPhone_mail.setText("");
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
}