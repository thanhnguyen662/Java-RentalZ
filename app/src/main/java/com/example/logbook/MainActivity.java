package com.example.logbook;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.time.LocalDate;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Max;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.mobsandgeeks.saripaar.annotation.Url;


import java.util.*;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, Validator.ValidationListener {

    @NotEmpty
    EditText edtName, edtPrice,  edtStartDate, edtEndDate;

    EditText edtNote;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21
            edtEndDate.setShowSoftInputOnFocus(false);
            edtStartDate.setShowSoftInputOnFocus(false);
        } else { // API 11-20
            edtEndDate.setTextIsSelectable(true);
            edtEndDate.setShowSoftInputOnFocus(true);
        }
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog);
                builder.setTitle("Confirm?");
                builder.setMessage("Name: " + edtName.getText().toString() + "\n" +
                        "Price: " + edtPrice.getText().toString() + "\n" +
                        "Property Type: " + spnPropertyType.getSelectedItem().toString() + "\n" +
                        "Bed Room: " + spnBedRoom.getSelectedItem().toString() + "\n" +
                        "Furniture Type: " + spnFurnitureType.getSelectedItem().toString() + "\n" +
                        "Start Date: " + edtStartDate.getText().toString() + "\n" +
                        "End Date: " + edtEndDate.getText().toString() + "\n" +
                        "Note: " + edtNote.getText().toString());
                builder.setIcon(R.drawable.ic_launcher_foreground);
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        buttonSave_onClick(view);
                    };
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    };
                });
                builder.show();
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
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();

        String startDate = edtStartDate.getText().toString();
        String endDate = edtEndDate.getText().toString();

        if(dateFormat.format(currentDate).compareTo(startDate) > 0) {
            edtStartDate.setError(getText(R.string.start_date_is_after_current_date));
        }

        if(endDate.compareTo(startDate) < 0) {
            edtEndDate.setError(getText(R.string.end_date_must_be_after_start_date));
        }

        if(dateFormat.format(currentDate).compareTo(endDate) > 0) {
            edtEndDate.setError(getText(R.string.end_date_is_after_current_date));
        }
    }


//    @Override
//    public void onValidationSucceeded() {
//        Toast.makeText(this, "We got it right!", Toast.LENGTH_SHORT).show();
//    }

    public void confirmDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog);
        builder.setTitle("Confirm?");
        builder.setMessage("Hello");
        builder.setIcon(R.drawable.ic_launcher_foreground);
        builder.setPositiveButton("co", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onBackPressed();
            };
        });
        builder.setPositiveButton("ko", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onBackPressed();
            };
        });
    };

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
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
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
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
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

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();

        if(dateFormat.format(currentDate).compareTo(startDate) > 0) {
            return;
        } else if (endDate.compareTo(startDate) < 0) {
            return;
        } else if (dateFormat.format(currentDate).compareTo(endDate) > 0) {
            return;
        } else {
            Toast.makeText(this,
                    "Name: " + name + "\n" +
                            "Price: " + price + "\n" +
                            "Property Type: " + propertyType + "\n" +
                            "Bed Room: " + bedRoom + "\n" +
                            "Furniture Type: " + furnitureType + "\n" +
                            "Start Date: " + startDate + "\n" +
                            "End Date: " + endDate + "\n" +
                            "Note: " + note,
                    Toast.LENGTH_LONG).show();
        };
    };



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