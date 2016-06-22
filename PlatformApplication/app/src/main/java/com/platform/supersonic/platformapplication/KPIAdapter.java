package com.platform.supersonic.platformapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class KPIAdapter extends ArrayAdapter<KPI> {

    Context context;
    int layoutResourceId;
    KPI kpis[] = null;

    public KPIAdapter(Context context, int layoutResourceId, KPI[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.kpis = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        KPIHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(R.layout.kpi_row, parent, false);

            holder = new KPIHolder();
            holder.title = (TextView) row.findViewById(R.id.kpiName);
            holder.value = (TextView) row.findViewById(R.id.kpiValue);
            holder.trendIcon = (ImageView) row.findViewById(R.id.kpiTrend);
            row.setTag(holder);
        }
        else
        {
            holder = (KPIHolder)row.getTag();
        }

        KPI kpi = kpis[position];
        holder.title.setText(kpi.title);
        holder.value.setText(kpi.value);
        switch (kpi.trend) {
            case UP:
                holder.trendIcon.setImageResource(R.mipmap.up);
                break;
            case DOWN:
                holder.trendIcon.setImageResource(R.mipmap.down);
                break;
            case SAME:
                holder.trendIcon.setImageResource(R.mipmap.same);
                break;
        }
        return row;
    }

    static class KPIHolder
    {
        ImageView trendIcon;
        TextView title;
        TextView value;
    }
}