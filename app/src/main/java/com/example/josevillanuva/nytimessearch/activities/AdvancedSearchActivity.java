package com.example.josevillanuva.nytimessearch.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.josevillanuva.nytimessearch.R;
import com.example.josevillanuva.nytimessearch.extras.DatePickerFragment;
import com.example.josevillanuva.nytimessearch.models.AdvancedSearch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AdvancedSearchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextView tvSetStartDate;
    TextView tvSetEndDate;
    Date startDate;
    Date endDate;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    String fieldName;
    Spinner spNewsDesk;
    Spinner spOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);

        tvSetStartDate = (TextView)findViewById(R.id.tvSetStartDate);
        tvSetEndDate = (TextView)findViewById(R.id.tvSetEndDate);

        this.loadDisplay();
    }

    public void loadDisplay(){
        spNewsDesk = (Spinner) findViewById(R.id.spNewsDesk);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.news_desk_values, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNewsDesk.setAdapter(adapter);

        spOrder = (Spinner) findViewById(R.id.spOrder);
        ArrayAdapter<CharSequence> adapter01 = ArrayAdapter.createFromResource(this,
                R.array.order_values, android.R.layout.simple_spinner_item);

        adapter01.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOrder.setAdapter(adapter01);
    }

    public void showTimePickerDialog(View v){
        switch (v.getId()){
            case R.id.tvStartDateLabel:
                fieldName = datefield.start.toString();
                break;

            case R.id.tvEndDateLabel:
                fieldName = datefield.end.toString();
                break;
        }

        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day){
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        if(fieldName != null)
        {
            switch (fieldName){
                case "start":
                    //tvSetStartDate.setText(c.getTime().toString());
                    tvSetStartDate.setText(dateFormat.format(c.getTime()));
                    this.startDate = c.getTime();
                    break;

                case "end":
                    //tvSetEndDate.setText(c.getTime().toString());
                    tvSetEndDate.setText(dateFormat.format(c.getTime()));
                    this.endDate = c.getTime();
                    break;
            }
        }
    }

    public void onCancel(View view) {
        this.finish();
    }

    public void OnSave(View view) {
        AdvancedSearch advancedSearch = new AdvancedSearch(
                this.startDate,
                this.endDate,
                spNewsDesk.getSelectedItem().toString(),
                spOrder.getSelectedItem().toString());


        Bundle bundle = new Bundle();
        bundle.putParcelable("AdvancedSearch", advancedSearch);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(20, intent);
        finish();
    }

    public enum datefield
    {
        start,

        end
    }

}
