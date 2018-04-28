package com.example.amrezzat.myapplication;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by amrezzat on 4/8/2018.
 */

public class curativeAdapter extends ArrayAdapter{
    private ArrayList<String> CurativeName;
    private ArrayList<String> CurativeReason;
    private ArrayList<String> CurativeSideEffect;
    private ArrayList<String> CurativeInformation;
    private final Activity context;

    //constructor to initialize context
    public curativeAdapter(Activity context, ArrayList<String> CurativeName, ArrayList<String> CurativeSideEffect, ArrayList<String> CurativeReason, ArrayList<String> CurativeInformation) {
        super(context, R.layout.list_item, CurativeName);
        this.context = context;
        this.CurativeName = CurativeName;
        this.CurativeReason = CurativeReason;
        this.CurativeSideEffect = CurativeSideEffect;
        this.CurativeInformation = CurativeInformation;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflat = context.getLayoutInflater();
        //to make activity going to new view
        View v = inflat.inflate(R.layout.list_item, null, true);
        //set textView value
        TextView CName = v.findViewById(R.id.curativeName);
        CName.setText(CurativeName.get(position));
        TextView CReason = v.findViewById(R.id.curativeReason);
        CReason.setText(CurativeReason.get(position));
        TextView CSideEffect = v.findViewById(R.id.curativeSideEffect);
        CSideEffect.setText(CurativeSideEffect.get(position));
        TextView CInformation = v.findViewById(R.id.curativeInformation);
        CInformation.setText(CurativeInformation.get(position));
        //return new View
        return v;
    }
}
