
package com.example.logbook;
import android.app.DatePickerDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, Validator.ValidationListener {

    @NotEmpty
    EditText edtName, edtPrice, edtNote,  edtStartDate, edtEndDate;
    Spinner spnPropertyType, spnBedRoom, spnFurnitureType;
    Button btnClick;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    private void initView() {
        edtName = (EditText) findViewById(R.id.name);
        edtPrice = (EditText) findViewById(R.id.price);
        edtNote = (EditText) findViewById(R.id.note);
        spnPropertyType = (Spinner) findViewById(R.id.propertyType);
        spnBedRoom = (Spinner) findViewById(R.id.bedRoom);
        spnFurnitureType = (Spinner) findViewById(R.id.furnitureType);
        edtStartDate = (EditText) findViewById(R.id.startDate);
        edtEndDate = (EditText) findViewById(R.id.endDate);
        btnClick = (Button) findViewById(R.id.submitButton);

        ArrayAdapter<CharSequence> propertyTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.propertyType, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> bedRoomAdapter = ArrayAdapter.createFromResource(this,
                R.array.bedRoom, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> furnitureTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.furnitureType, android.R.layout.simple_spinner_item);

        propertyTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bedRoomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        furnitureTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnPropertyType.setAdapter(propertyTypeAdapter);
        spnPropertyType.setOnItemSelectedListener(this);
        spnBedRoom.setAdapter(bedRoomAdapter);
        spnBedRoom.setOnItemSelectedListener(this);
        spnFurnitureType.setAdapter(furnitureTypeAdapter);
        spnFurnitureType.setOnItemSelectedListener(this);

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSave_onClick(view);
            }
        });


        edtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStartDate();
            }
        });

        edtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectEndDate();
            }
        });
    }

    private void buttonSave_onClick(View view) {
        validator.validate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date currentDate = new Date();


        String startDate = edtStartDate.getText().toString();
        String endDate = edtEndDate.getText().toString();

        if(startDate.compareTo(dateFormat.format(currentDate)) <= 0) {
            edtStartDate.setError(getText(R.string.start_date_is_after_current_date));
        }

        if(endDate.compareTo(startDate) < 0) {
            edtEndDate.setError(getText(R.string.end_date_must_be_after_start_date));
        }
    }

    private void selectStartDate(){
        Calendar calendar = Calendar.getInstance();
        int dateNow = calendar.get(Calendar.DATE);
        int monthNow = calendar.get(Calendar.MONTH);
        int yearNow = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                edtStartDate.setText(sdf.format(calendar.getTime()));
            }
        }, yearNow, monthNow, dateNow);
        datePickerDialog.show();
    }

    private void selectEndDate(){
        Calendar calendar = Calendar.getInstance();
        int dateNow = calendar.get(Calendar.DATE);
        int monthNow = calendar.get(Calendar.MONTH);
        int yearNow = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                edtEndDate.setText(sdf.format(calendar.getTime()));
            }
        }, yearNow, monthNow, dateNow);
        datePickerDialog.show();
    }


    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onValidationSucceeded() {
        String name = edtName.getText().toString();
        String price = edtPrice.getText().toString();
        String note = edtNote.getText().toString();
        String propertyType = spnPropertyType.getSelectedItem().toString();
        String bedRoom = spnBedRoom.getSelectedItem().toString();
        String furnitureType = spnFurnitureType.getSelectedItem().toString();
        String startDate = edtStartDate.getText().toString();
        String endDate = edtEndDate.getText().toString();

        Toast.makeText(getApplicationContext(),
                "Name: " + name + "\n" +
                        "Price: " + price + "\n" +
                        "Property Type: " + propertyType + "\n" +
                        "Bed Room: " + bedRoom + "\n" +
                        "Furniture Type: " + furnitureType + "\n" +
                        "Start Date: " + startDate + "\n" +
                        "End Date: " + endDate + "\n" +
                        "Note: " + note,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}