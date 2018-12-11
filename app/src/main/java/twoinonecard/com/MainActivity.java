package twoinonecard.com;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import android_serialport_api.SerialPortFinder;
import butterknife.BindView;
import butterknife.ButterKnife;
import twoinonecard.com.util.AppConfigs;
import twoinonecard.com.util.FileStorageHelper;

public class MainActivity extends Activity{

    @BindView(R.id.button01)
    Button button01;
    @BindView(R.id.button02)
    Button button02;
    private List<String> spList;
    private SharedPreferences sp;
    private SharedPreferences.Editor ed;
    private boolean spitem0 = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FileStorageHelper.copyFilesFromRaw(this, R.raw.base,"base.dat", AppConfigs.RootFile);
        FileStorageHelper.copyFilesFromRaw(this,R.raw.license,"license.lic", AppConfigs.RootFile);
        sp = getSharedPreferences("setting", Context.MODE_PRIVATE);

        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PortCardActivity.class);
                intent.putExtra("id",1);
                startActivity(intent);
            }
        });
        button02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PortCardActivity.class);
                intent.putExtra("id",0);
                startActivity(intent);
            }
        });

        SerialPortFinder spFinder=new SerialPortFinder();
        String str[]=spFinder.getAllDevicesPath();
        Spinner spinner = findViewById(R.id.spDwon);
        spList = new ArrayList<>();
        spList.add("0");
        for (String aStr : str) {
            spList.add(aStr);
        }
        ArrayAdapter spAdapter = new ArrayAdapter<>(this, R.layout.custom_spiner_text_item, spList);
        spinner.setAdapter(spAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spitem0) {
                    Toast.makeText(MainActivity.this,""+spList.get(position).trim(),Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("port",spList.get(position).trim());
                    edit.putInt("position",position);
                    edit.apply();
                }
                spitem0 = false;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner.setSelection(sp.getInt("position",0),true);
    }

}
