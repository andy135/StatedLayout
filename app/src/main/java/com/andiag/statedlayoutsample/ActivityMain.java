package com.andiag.statedlayoutsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.andiag.statedlayout.StatedLayout;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends AppCompatActivity {

    RecyclerView recycler;
    AdapterContent adapter;
    StatedLayout statedLayout;
    Button bLoading,bEmpty,bError,bContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = (RecyclerView) findViewById(R.id.recycler);
        statedLayout = (StatedLayout) findViewById(R.id.statedLayout);
        bContent = (Button) findViewById(R.id.buttonContent);
        bError = (Button) findViewById(R.id.buttonError);
        bEmpty = (Button) findViewById(R.id.buttonEmpty);
        bLoading = (Button) findViewById(R.id.buttonLoading);
        adapter = new AdapterContent(this,new ArrayList<ItemContent>());
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        setClickListerners();

        adapter.updateList(getSampleContent());
    }

    private void setClickListerners(){
        bContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statedLayout.setState(StatedLayout.STATE_CONTENT);
            }
        });
        bLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statedLayout.setState(StatedLayout.STATE_LOADING);
            }
        });
        bEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statedLayout.setState(StatedLayout.STATE_EMPTY);
            }
        });
        bError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statedLayout.setState(StatedLayout.STATE_ERROR);
            }
        });
    }

    private List<ItemContent> getSampleContent(){
        List<ItemContent> result = new ArrayList<>();
        for(int i = 0;i<50;i++){
            result.add(new ItemContent(android.R.drawable.ic_dialog_email,"Item "+i));
        }
        return result;
    }
}
