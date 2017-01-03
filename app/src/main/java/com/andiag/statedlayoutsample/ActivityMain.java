package com.andiag.statedlayoutsample;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.andiag.statedlayout.StatedLayout;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends AppCompatActivity implements StatedLayout.StateCallbackListener {

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
        statedLayout.setStateCallbackListener(this);
        statedLayout.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        statedLayout.setTextSize(32);
        statedLayout.setTintColor(ContextCompat.getColor(this, R.color.colorAccent));
        statedLayout.setAlternateIcons(true);
        bContent = (Button) findViewById(R.id.buttonContent);
        bError = (Button) findViewById(R.id.buttonError);
        bEmpty = (Button) findViewById(R.id.buttonEmpty);
        bLoading = (Button) findViewById(R.id.buttonLoading);
        adapter = new AdapterContent(this,new ArrayList<ItemContent>());
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        setClickListerners();

        adapter.updateList(getSampleContent(50));
    }

    private void setClickListerners(){
        bContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statedLayout.setContent();
            }
        });
        bLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statedLayout.setLoading();
            }
        });
        bEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statedLayout.setEmpty();
            }
        });
        bError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statedLayout.setError();
            }
        });
    }

    private List<ItemContent> getSampleContent(int size) {
        List<ItemContent> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add(new ItemContent(R.drawable.stated_empty_, "Item " + i));
        }
        return result;
    }

    @Override
    public void onRetryClick() {
        Toast.makeText(this, "Retrying", Toast.LENGTH_SHORT).show();
        adapter.updateList(getSampleContent(100));
    }

    @Override
    public void onStateLoading() {
        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStateError() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStateContent() {
        Toast.makeText(this, "Content", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStateEmpty() {
        Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
    }
}
