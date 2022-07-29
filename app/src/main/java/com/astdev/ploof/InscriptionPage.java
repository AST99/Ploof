package com.astdev.ploof;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class InscriptionPage extends AppCompatActivity {

    private TabLayout tabLayout;
    private Button btnInscription;
    private TextInputEditText editTxtNomPrenom, editTxtPhone_mail, editTxtPassWrd, confirmeditTxtPassWrd;
    private TextInputLayout editTxtTitle;

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
        this.editTxtTitle = findViewById(R.id.titlePhoneOrMail);


        selectedTab();

    }


    private void selectedTab(){

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        editTxtTitle.setHint("Numéro de téléphone");
                        editTxtTitle.setEndIconDrawable(R.drawable.ic_phone);
                        //editTxtPhone_mail.setHint("77777777");
                        editTxtPhone_mail.setInputType(InputType.TYPE_CLASS_PHONE);
                        break;
                    case 1:
                        editTxtTitle.setHint("E-mail");
                        editTxtTitle.setEndIconDrawable(R.drawable.ic_email);
                        //editTxtPhone_mail.setHint("exemple@ploof.com");
                        editTxtPhone_mail.setInputType(InputType.TYPE_CLASS_TEXT|
                                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
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