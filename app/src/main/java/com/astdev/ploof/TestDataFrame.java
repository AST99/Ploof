package com.astdev.ploof;

import androidx.appcompat.app.AppCompatActivity;

import com.astdev.jpandas.src.main.java.com.c_bata.DataFrame;
import com.astdev.jpandas.src.main.java.com.c_bata.Series;
import com.astdev.ploof.databinding.ActivityConnexionBinding;
import com.astdev.ploof.databinding.ActivityTestDataFrameBinding;
/*import com.astdev.ploof.jpandas.DataFrame;
import com.astdev.ploof.jpandas.Series;*/
import java.util.Iterator;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TestDataFrame extends AppCompatActivity {

    ActivityTestDataFrameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityTestDataFrameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Checking jpandas
        Series<Double> series_a = new Series<>();
        series_a.add(3.0);
        series_a.add(5.0);
        Series<Double> series_b = new Series<>();
        series_b.add(9.0);
        series_b.add(20.0);

        DataFrame<String, Series> df = new DataFrame<>();
        df.put("feature_a", series_a);
        df.put("feature_b", series_b);

        // Log.d(df.mean());
        Log.d("JPANDAS", String.valueOf(df.mean().get("feature_a")));
        Log.d("JPANDAS", String.valueOf(df.mean().get("feature_b")));
        // Log.d(df.std());
        Log.d("JPANDAS", String.valueOf(df.std().get("feature_a")));
        Log.d("JPANDAS", String.valueOf(df.std().get("feature_b")));

        binding.textView.setText("Jpandas 1 "+ df.mean().get("feature_a"));


    }
}