package com.example.pdfapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//This Adapter Will help you to handle the ListView
public class PDFAdapter extends ArrayAdapter<File> {
    Context context;
    ViewHolder viewHolder;
    ArrayList<File> pdfLists;

    public PDFAdapter(Context context, ArrayList<File> pdfLists) {
        super(context, R.layout.pdf_list_sample, pdfLists);
        this.context = context;
        this.pdfLists = pdfLists;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (pdfLists.size()>0)
            return pdfLists.size();
        else return 1;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pdf_list_sample, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewFileName = convertView.findViewById(R.id.iv_text);
            convertView.setTag(viewHolder);
//            Toast.makeText(context, pdfLists.get(position).getName(), Toast.LENGTH_SHORT).show();
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textViewFileName.setText(pdfLists.get(position).getName());
//        Toast.makeText(context, pdfLists.get(position).getName(), Toast.LENGTH_SHORT).show();
        return convertView;
    }

    public class ViewHolder{
        TextView textViewFileName;
    }

}
