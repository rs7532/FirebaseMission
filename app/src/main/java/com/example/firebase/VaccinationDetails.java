package com.example.firebase;

import static com.example.firebase.MainActivity.refStudents;
import static com.example.firebase.ViewDetails.stuList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VaccinationDetails extends AppCompatActivity {
    CheckBox firstVac, secondVac;
    EditText firstLoc, firstDate, secondLoc, secondDate;
    Intent gi;
    String firstname, lastname, layer, stuId, stuPos;
    int grade;

    /**
     * @author Roey Schwartz rs7532@bs.amalnet.k12.il
     * @version 1
     * @since 23.04.2024
     * this code will help our school manager to manage his students' details
     */

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination_details);

        firstVac = findViewById(R.id.firstVaccination);
        secondVac = findViewById(R.id.secondVaccination);
        firstLoc = findViewById(R.id.firstLoc);
        firstDate = findViewById(R.id.firstDate);
        secondLoc = findViewById(R.id.secondLoc);
        secondDate = findViewById(R.id.secondDate);

        gi = getIntent();
        firstname = gi.getStringExtra("firstname");
        lastname = gi.getStringExtra("lastname");
        layer = gi.getStringExtra("layer");
        grade = gi.getIntExtra("grade", -1);
        stuPos = gi.getStringExtra("stuPos");
        stuId = gi.getStringExtra("id");


        if (Integer.parseInt(stuPos) != -1){
            Student tmp = stuList.get(Integer.parseInt(stuPos));
            if (tmp.getFirstVaccination() != null){
                firstLoc.setText(tmp.getFirstVaccination().getPlace());
                firstDate.setText(tmp.getFirstVaccination().getDateOfVaccination());
                firstVac.setChecked(true);
                if (tmp.getSecondVaccination() != null){
                    secondLoc.setText(tmp.getFirstVaccination().getPlace());
                    secondDate.setText(tmp.getFirstVaccination().getDateOfVaccination());
                    secondVac.setChecked(true);
                }
            }
        }
    }

    /**
     *
     * @param view
     * this code is checking validation inputs and creates a new student in the system.
     */
    public void VacConfirm(View view) {
        Vaccination firstvacTemp = null;
        Vaccination secondvacTemp = null;
        boolean flag = true;
        if (firstVac.isChecked()){
            if (!(firstLoc.getText().toString().isEmpty() || firstDate.getText().toString().isEmpty())){
                firstvacTemp = new Vaccination(firstLoc.getText().toString(), firstDate.getText().toString());
                if (secondVac.isChecked()){
                    if (!(secondLoc.getText().toString().isEmpty() || secondDate.getText().toString().isEmpty())){
                        secondvacTemp = new Vaccination(secondLoc.getText().toString(), secondDate.getText().toString());
                    }
                    else{
                        Toast.makeText(this, "fill the full details of the second vaccination!", Toast.LENGTH_SHORT).show();
                        flag = false;
                    }
                }
            } else{
                Toast.makeText(this, "fill the full details of the first vaccination! ", Toast.LENGTH_SHORT).show();
                flag = false;
            }
        }
        if (flag){
            Student student = new Student(stuId, firstname, lastname, layer, grade, true, firstvacTemp, secondvacTemp);
            refStudents.child(student.getLayer())
                    .child(String.valueOf(student.getGrade()))
                    .child(firstname+" "+lastname)
                    .setValue(student);

            firstVac.setChecked(false);
            secondVac.setChecked(false);
            firstLoc.setText("");
            firstDate.setText("");
            secondLoc.setText("");
            secondDate.setText("");

            finish();
        }
    }
}