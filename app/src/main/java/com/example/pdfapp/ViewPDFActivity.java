package com.example.pdfapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

public class ViewPDFActivity extends AppCompatActivity {

    PDFView pdfView;
    int position = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_p_d_f);

        //For Back Button
        getSupportActionBar().setTitle("Pdf View");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pdfView = findViewById(R.id.pdfView);
        position = getIntent().getIntExtra("position", -1);
        

        //Display Pdf In PdfViewer
        displayPDF();

    }

    private void displayPDF() {
        pdfView.fromFile(PdfListsActivity.fileList.get(position))
                .enableSwipe(true)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
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
