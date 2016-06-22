package com.platform.supersonic.platformapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class StatsBase extends Fragment implements AdapterView.OnItemSelectedListener{

    String type;
    protected String token;

    private ListView kpiListView;

    public StatsBase() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stats, container, false);

        KPI kpis[] = new KPI[]
                {
                        new KPI(KPI.TREND.UP,"Clicks","1000"),
                        new KPI(KPI.TREND.DOWN,"Clicks","1000"),
                        new KPI(KPI.TREND.SAME,"Clicks","1000"),
                        new KPI(KPI.TREND.UP,"Clicks","1000"),
                        new KPI(KPI.TREND.DOWN,"Clicks","1000"),
                        new KPI(KPI.TREND.SAME,"Clicks","1000"),
                        new KPI(KPI.TREND.UP,"Clicks","1000"),
                        new KPI(KPI.TREND.DOWN,"Clicks","1000"),
                        new KPI(KPI.TREND.SAME,"Clicks","1000"),
                        new KPI(KPI.TREND.UP,"Clicks","1000"),
                        new KPI(KPI.TREND.DOWN,"Clicks","1000"),
                        new KPI(KPI.TREND.SAME,"Clicks","1000")
                };

        KPIAdapter kpisadapter = new KPIAdapter(this.getContext(),
                R.layout.kpi_row, kpis);


        kpiListView = (ListView) view.findViewById(R.id.kpiListView);

        kpiListView.setAdapter(kpisadapter);

//        TextView type = (TextView) view.findViewById(R.id.type);
//        type.setText(this.type);

        Spinner spinner = (Spinner) view.findViewById(R.id.dates_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.dates_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setOnItemSelectedListener(this);
        spinner.setAdapter(adapter);

        return view;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        String str = (String) parent.getItemAtPosition(pos);

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback

    }

}