package com.example.pdfapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class CreatePdfActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE1 = 1;
    private static final int PICK_IMAGE2 = 2;
    private static final int PICK_IMAGE3 = 3;
    private static final int PICK_IMAGE4 = 4;
    private static final int PICK_IMAGE5 = 5;

    Bitmap[] bitmaps = new Bitmap[5];

    ImageView imageView1, imageView2, imageView3, imageView4, imageView5;
    EditText fileNameEditText;
    Button createPDF_BT, cleaBT;

    private ProgressDialog loadingBar;

    PdfDocument pdfDocument;

    File pdfDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pdf);

        //For Back Button
        getSupportActionBar().setTitle("Create PDF");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Initialization
        initialize();

        //Clicks
        allClicks();

        //Permissions
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        //Create Folder For Save Pdf
        createFolder();
    }

    private void initialize() {
        imageView1 =findViewById(R.id.add_imageView1);
        imageView2 =findViewById(R.id.add_imageView2);
        imageView3 =findViewById(R.id.add_imageView3);
        imageView4 =findViewById(R.id.add_imageView4);
        imageView5 =findViewById(R.id.add_imageView5);
        fileNameEditText = findViewById(R.id.file_name_ET);
        createPDF_BT = findViewById(R.id.createPDF_button);
        cleaBT = findViewById(R.id.clear_button);
        loadingBar = new ProgressDialog(this);
    }

    private void createPDF() {
        pdfDocument = new PdfDocument();
        int imageNum = imageCount();
        Toast.makeText(this, ""+imageNum, Toast.LENGTH_SHORT).show();
        if (imageNum == 0){
            Toast.makeText(this, "Please Select Image First", Toast.LENGTH_SHORT).show();
        }
        else{
            //Loading
            loadingBar.setTitle("Create PDF");
            loadingBar.setMessage("Please wait, while creating your PDF...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            for (int i = 1; i <= imageNum; i++) {
                PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(bitmaps[i-1].getWidth(), bitmaps[i-1].getHeight(), i).create();
                PdfDocument.Page page = pdfDocument.startPage(myPageInfo);
                Canvas canvas = page.getCanvas();
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#FFFFFF"));
                ;
                canvas.drawPaint(paint);

                bitmaps[i-1] = Bitmap.createScaledBitmap(bitmaps[i-1], bitmaps[i-1].getWidth(), bitmaps[i-1].getWidth(), true);
                paint.setColor(Color.BLUE);
                canvas.drawBitmap(bitmaps[i-1], 0, 0, null);

                pdfDocument.finishPage(page);
            }
            savePdfInStorage();
        }
    }

    private void savePdfInStorage() {
        if (fileNameEditText.getText().toString().trim().equals("")){
            Toast.makeText(this, "Please Enter File Name First", Toast.LENGTH_SHORT).show();
        }else{
            String directory = pdfDirectory + "/";
            String name = directory + fileNameEditText.getText().toString().trim() + " " + getName() + ".pdf";
            File pdf = new File(name);

            try {
                pdfDocument.writeTo(new FileOutputStream(pdf));
                Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            pdfDocument.close();
            clearAll();
            loadingBar.dismiss();
        }
    }

    private String getName() {
        Calendar calendarDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-mm-yyyy");
        String saveCurrentDate = currentDate.format(calendarDate.getTime());

        Calendar calendarTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:MM");
        String saveCurrentTime = currentTime.format(calendarTime.getTime());

        return saveCurrentDate + " " + saveCurrentTime;
    }

    private void clearAll() {

        imageView1.setImageResource(R.drawable.add_image);
        imageView2.setImageResource(R.drawable.add_image);
        imageView3.setImageResource(R.drawable.add_image);
        imageView4.setImageResource(R.drawable.add_image);
        imageView5.setImageResource(R.drawable.add_image);

        CardView cardView2 = findViewById(R.id.cardView2);
        cardView2.setVisibility(View.INVISIBLE);

        CardView cardView3 = findViewById(R.id.cardView3);
        cardView3.setVisibility(View.INVISIBLE);

        CardView cardView4 = findViewById(R.id.cardView4);
        cardView4.setVisibility(View.INVISIBLE);

        CardView cardView5 = findViewById(R.id.cardView5);
        cardView5.setVisibility(View.INVISIBLE);

        fileNameEditText.setText("");
    }

    private int imageCount() {
        if(bitmaps[0]==null){
            return 0;
        }
        else if(bitmaps[1]==null){
            return 1;
        }
        else if(bitmaps[2]==null){
            return 2;
        }
        else if(bitmaps[3]==null){
            return 3;
        }
        else if(bitmaps[4]==null){
            return 4;
        }
        else return 5;
    }

    private void createFolder() {
        File direct = new File(Environment.getExternalStorageDirectory() + "/Pdf");
        if (!direct.exists()) {
            File directory = new File(Environment.getExternalStorageDirectory() + "/Pdf App/Pdf/");
            directory.mkdirs();
            pdfDirectory = directory;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE1 && resultCode == RESULT_OK && data != null){
            setImageInImageView(requestCode, resultCode, data);
        }
        else if (requestCode == PICK_IMAGE2 && resultCode == RESULT_OK && data != null){
            setImageInImageView(requestCode, resultCode, data);
        }
        else if (requestCode == PICK_IMAGE3 && resultCode == RESULT_OK && data != null){
            setImageInImageView(requestCode, resultCode, data);
        }
        else if (requestCode == PICK_IMAGE4 && resultCode == RESULT_OK && data != null){
            setImageInImageView(requestCode, resultCode, data);
        }
        else if (requestCode == PICK_IMAGE5 && resultCode == RESULT_OK && data != null){
            setImageInImageView(requestCode, resultCode, data);
        }
    }

    private void setImageInImageView(int serial, int resultCode, Intent data) {
        Uri selectedImage = data.getData();

        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmaps[serial-1] = BitmapFactory.decodeStream(inputStream);

        //set Images
        switch (serial){
                case 1:
                    imageView1.setImageBitmap(bitmaps[serial-1]);
                    CardView cardView2 = findViewById(R.id.cardView2);
                    cardView2.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    imageView2.setImageBitmap(bitmaps[serial-1]);
                    CardView cardView3 = findViewById(R.id.cardView3);
                    cardView3.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    imageView3.setImageBitmap(bitmaps[serial-1]);
                    CardView cardView4 = findViewById(R.id.cardView4);
                    cardView4.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    imageView4.setImageBitmap(bitmaps[serial-1]);
                    CardView cardView5 = findViewById(R.id.cardView5);
                    cardView5.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    imageView5.setImageBitmap(bitmaps[serial-1]);
                    break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_imageView1:
                selectImageFromGallery(PICK_IMAGE1);
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add_imageView2:
                selectImageFromGallery(PICK_IMAGE2);
                break;
            case R.id.add_imageView3:
                selectImageFromGallery(PICK_IMAGE3);
                break;
            case R.id.add_imageView4:
                selectImageFromGallery(PICK_IMAGE4);
                break;
            case R.id.add_imageView5:
                selectImageFromGallery(PICK_IMAGE5);
                break;
            case R.id.createPDF_button:
                createPDF();
                break;
            case R.id.clear_button:
                clearAll();
                break;
        }
    }

    private void selectImageFromGallery(int num){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, num);
            Toast.makeText(this, "Image"+num, Toast.LENGTH_SHORT).show();
        }
    }

    private void allClicks() {
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        imageView5.setOnClickListener(this);

        createPDF_BT.setOnClickListener(this);
        cleaBT.setOnClickListener(this);
    }

    //For Back Button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
