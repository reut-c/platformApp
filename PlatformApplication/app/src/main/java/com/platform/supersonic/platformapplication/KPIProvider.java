package com.platform.supersonic.platformapplication;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class KPIProvider implements RemoteViewsFactory {
    private ArrayList<KPI> kpiList;
    private Context context = null;
    private int appWidgetId;
    private String token;

    private static final String TAG = WidgetService.class.getSimpleName();

    public KPIProvider(Context context, Intent intent) {
        this.context = context;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        this.kpiList = new ArrayList<KPI>();
        populateKPIs();
    }

    protected void getToken(){
        SharedPreferences settings = this.context.getSharedPreferences(BasicActivity.USERDATA, 0);
        this.token = settings.getString(BasicActivity.TOKEN, null);
        Log.d(TAG,"Token: " + this.token);
    }

    private void populateKPIs() {
        try {

            SharedPreferences preference = this.context.getSharedPreferences(WidgetConfigurationActivity.PREFSFILE, 0);
            String type = preference.getString(this.appWidgetId+"-Type","Monetize");
            String KPIs = preference.getString(this.appWidgetId+"-KPIs","[]");
            String dateRange = preference.getString(this.appWidgetId+"-DateRange","Today");
            String dateClause = this.getDateClauseFromDropdown(dateRange);
            this.getToken();
            if (this.token != null){
                String url = "";
                if (type.equals("Promote")){
                    url = BasicActivity.BASE_URL + "api/rest/v1/partners/statistics/promoteTopData?breakdowns[]=date&breakdowns[]=campaign&showAllPossibleRecords=1&top=5" + dateClause;
                } else {
                    url = BasicActivity.BASE_URL + "api/rest/v1/partners/statistics/mediationTopData?breakdowns[]=date&breakdowns[]=adSource&showAllPossibleRecords=1&top=5&fields=revenue,eCPM,fillRate,requests,impressions&units=242&adSource=any" + dateClause;
                }

                Request request = new Request(url, this.token, "GET");
                HttpClient http = new HttpClient(request, null, this.context);
                AsyncTask<Void, Void, String> asyncTask = http.execute();
                String st = asyncTask.get();
                JSONObject kpis = new JSONObject();
                if (st != null){
                    JSONObject response = new JSONObject(st);
                    kpis = response.getJSONObject("kpis");
                }

                this.kpiList.clear();

                JSONArray requestedKPIs = new JSONArray(KPIs);
                for (int i=0; i<requestedKPIs.length();i++){
                    String kpi = requestedKPIs.getString(i);
                    switch (kpi){
                        case "Clicks":
                            this.addKPIToList(kpis, "clicks", "Clicks", false);
                            break;
                        case "Impressions":
                            this.addKPIToList(kpis, "impressions", "Impressions", false);
                            break;
                        case "eCPM":
                            this.addKPIToList(kpis, "eCPM", "eCPM", true);
                            break;
                        case "Requests":
                            this.addKPIToList(kpis, "requests", "Requests", false);
                            break;
                        case "Fulfillments":
                            this.addKPIToList(kpis, "fulfillments", "Fulfillments", false);
                            break;
                        case "Revenue":
                            this.addKPIToList(kpis, "revenue", "Revenue", true);
                            break;
                        case "Completions":
                            this.addKPIToList(kpis, "completionCount", "Completions", false);
                            break;
                        case "Spend":
                            this.addKPIToList(kpis, "spendInLeadingCurrency", "Spend", true);
                            break;
                    }
                }
            }

        } catch (Exception e){

        }

    }



    @Override
    public int getCount() {
        return kpiList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        populateKPIs();
    }

    @Override
    public void onDestroy() {

    }

    /*
        *Similar to getView of Adapter where instead of View
        *we return RemoteViews
        *
        */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.widget_kpi_row);
        KPI kpi = (KPI) kpiList.get(position);
        remoteView.setTextViewText(R.id.kpiWidgetName, kpi.title);
        remoteView.setTextViewText(R.id.kpiWidgetValue, kpi.value);
        switch (kpi.trend) {
            case UP:
                remoteView.setImageViewResource(R.id.kpiWidgetTrend,R.mipmap.up);
                break;
            case DOWN:
                remoteView.setImageViewResource(R.id.kpiWidgetTrend,R.mipmap.down);
                break;
            case SAME:
                remoteView.setImageViewResource(R.id.kpiWidgetTrend,R.mipmap.same);
                break;
        }
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    protected void addKPIToList(JSONObject kpis,String kpiName, String presentingName, boolean addCurrency) {
        try {
            JSONObject kpi = kpis.getJSONObject(kpiName);
            String value = kpi.getString("value");
            KPI clickKPI = new KPI(
                    this.getTrendFromStr(kpi.getString("trend")),
                    presentingName,
                    addCurrency ? this.formatKPINumber(value)  + " $" : this.formatKPINumber(value)
            );
            this.kpiList.add(clickKPI);
        } catch (JSONException e) {
            this.kpiList.add(new KPI(
                    this.getTrendFromStr("same"),
                    presentingName,
                    "N/A"
            ));
        }
    }

    private String formatKPINumber(String value){
        double amount = Double.parseDouble(value);
        DecimalFormat formatter = new DecimalFormat("#,###.##");
        return formatter.format(amount);
    }

    protected KPI.TREND getTrendFromStr(String trend){
        if (trend.equals("same")){
            return  KPI.TREND.SAME;
        }
        if (trend.equals("down")){
            return  KPI.TREND.DOWN;
        }
        if (trend.equals("up")){
            return KPI.TREND.UP;
        }

        return KPI.TREND.SAME;
    }

    protected String getDateClauseFromDropdown(String chosenDate) {
        String today = new SimpleDateFormat("yyyy-M-dd").format(new java.util.Date());
        switch (chosenDate){
            case "Today":
                return "&fromDate=" + today + "&toDate=" + today;
            case "Yesterday":
                String yesterday = this.getDaysBeforeDateString(1);
                return "&fromDate=" + yesterday + "&toDate=" + yesterday;
            case "Last 7 Days":
                String lastWeek = this.getDaysBeforeDateString(6);
                return "&fromDate=" + lastWeek + "&toDate=" + today;
            case "Last 14 Days":
                String lastTwoWeek = this.getDaysBeforeDateString(13);
                return "&fromDate=" + lastTwoWeek + "&toDate=" + today;
        }
        return "&fromDate=" + today + "&toDate=" + today;
    }

    private String getDaysBeforeDateString(int numberOfDays) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -numberOfDays);
        return dateFormat.format(cal.getTime());
    }
}