package com.example.pdfapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;

public class PdfListsActivity extends AppCompatActivity {

    //NavigationDrawer
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    ListView listView;
    PDFAdapter adapter;
    public static ArrayList<File> fileList;
    public static int REQUEST_PERMISSION = 1;
    boolean boolean_permission;
    File dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_lists);

        //Navigation
        navigationView = findViewById(R.id.nav_View);
        drawerLayout = findViewById(R.id.drawer);
        View navView = navigationView.inflateHeaderView(R.layout.header);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                userMenueSelector(menuItem);
                return false;
            }
        });//Navigation Finish

        listView = findViewById(R.id.pdf_list);
        dir = new File(Environment.getExternalStorageDirectory().toString());
        fileList = new ArrayList<>();

        permission_fn();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), ViewPDFActivity.class).putExtra("position", position));
            }
        });

    }

    private void permission_fn() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(PdfListsActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
            }
            else {
                ActivityCompat.requestPermissions(PdfListsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION);
            }
        }
        else {
            boolean_permission = true;
            getFile(dir);
            adapter = new PDFAdapter(getApplicationContext(), fileList);
            listView.setAdapter(adapter);

        }
    }

    private ArrayList<File> getFile(File dir) {
        File listFile[] = dir.listFiles();

        if (listFile != null && listFile.length > 0){

            for (int i = 0; i < listFile.length ; i++) {

                if (listFile[i].isDirectory()){
                    getFile(listFile[i]);
                }
                else {
                    boolean booleanPdf = false;
                    if (listFile[i].getName().endsWith(".pdf")){

                        for (int j = 0; j < fileList.size() ; j++) {
                            if (fileList.get(j).getName().equals(listFile[i].getName())){
                                booleanPdf = true;
                            }else {}
                        }

                        if (booleanPdf){
                            booleanPdf = false;
                        }
                        else fileList.add(listFile[i]);
                    }
                }

            }
        }
        return fileList;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                boolean_permission = true;
                getFile(dir);
                adapter = new PDFAdapter(getApplicationContext(), fileList);
                listView.setAdapter(adapter);
            }
            else {
                Toast.makeText(this, "Please Allow Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void userMenueSelector(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.createPDF_menu:
                startActivity(new Intent(getApplicationContext(), CreatePdfActivity.class));
                break;
            case R.id.openPDF_menu:
                openPDF();
                break;

        }
    }

    private void openPDF(){
//        Intent myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        myFileIntent.setType("application/pdf");
//        startActivityForResult(myFileIntent, 100);
        startActivity(new Intent(PdfListsActivity.this, OpenPDFActivity.class));
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100 && resultCode == RESULT_OK && data != null){
//            Uri uri = data.getData();
//            startActivity(new Intent(PdfListsActivity.this, OpenPDFActivity.class)
//                    .putExtra("uri", uri));
//        }
//    }

    //Navigation                                                                                    Navigation
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return  super.onOptionsItemSelected(item);
    }


}
