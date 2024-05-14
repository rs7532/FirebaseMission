package com.example.firebase;

import static com.example.firebase.ViewDetails.stuList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public static FirebaseDatabase FBDB;
    public static DatabaseReference refStudents;
    Button StuDetailsConfirm;
    EditText StuFirstName, StuLastName, GradeET, StuID;
    Spinner LayerSpin;
    ToggleButton vaccinationCheck;
    String[] layer;
    int selected_spin;
    String stuPos;
    Intent gi;
    boolean flagEditData;
    ArrayList<Student> studentsArr;


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
        setContentView(R.layout.activity_main);

        FBDB = FirebaseDatabase.getInstance();
        refStudents=FBDB.getReference("School");
        flagEditData = false;

        StuDetailsConfirm = findViewById(R.id.StudentConfirm);
        StuFirstName = findViewById(R.id.StuFirstName);
        StuLastName = findViewById(R.id.StuLastName);
        GradeET = findViewById(R.id.gradeET);
        StuID = findViewById(R.id.StuID);
        LayerSpin = findViewById(R.id.LayerSpin);
        vaccinationCheck = findViewById(R.id.vaccinationCheck);

        layer = new String[]{"Layer", "7", "8", "9", "10", "11", "12"};
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item, layer);
        LayerSpin.setAdapter(adp);
        LayerSpin.setOnItemSelectedListener(this);

        studentsArr = new ArrayList<>();
    }

    /**
     *
     * @param str
     * this code will check that the user wrote only letters in the first and last name.
     */
    public boolean isValid(String str){
        boolean flag = true;
        for (int i = 0; i < str.length(); i++){
            if (str.charAt(i) < 'a' || str.charAt(i) > 'z'){
                if (str.charAt(i) < 'A' || str.charAt(i) > 'Z'){
                    flag = false;
                }
            }
        }
        return flag;
    }

    /**
     *
     * @param id
     * this code will check that the ID that entered by the user isn't already in the system.
     */
    public boolean validID(String id){
        boolean flag = true;
        for (int i = 0; i < studentsArr.size(); i++){
            if (studentsArr.get(i).getId().equals(id)){
                flag = false;
            }
        }
        return flag;
    }

    /**
     *
     * @param view
     * this code is checking validation inputs and moves to the next activity of vaccination details
     * or creates a new student in the system.
     */
    public void StuConfirm(View view) {
        if (isValid(StuFirstName.getText().toString()) && isValid(StuLastName.getText().toString())){
            if (selected_spin != 0){
                if (Integer.parseInt(GradeET.getText().toString()) > 0 && Integer.parseInt(GradeET.getText().toString()) < 15){
                    if (!StuID.getText().toString().isEmpty() && StuID.getText().toString().length() < 10){
                        if (!flagEditData){
                            if (validID(StuID.getText().toString())) {
                                if (vaccinationCheck.isChecked()) {
                                    Intent intent = new Intent(this, VaccinationDetails.class);
                                    intent.putExtra("firstname", StuFirstName.getText().toString());
                                    intent.putExtra("lastname", StuLastName.getText().toString());
                                    intent.putExtra("layer", layer[selected_spin]);
                                    intent.putExtra("grade", Integer.parseInt(GradeET.getText().toString()));
                                    if (stuPos != null) {
                                        intent.putExtra("stuPos", stuPos);
                                    } else {
                                        intent.putExtra("stuPos", "-1");
                                    }
                                    intent.putExtra("id", StuID.getText().toString());
                                    startActivity(intent);
                                    StuFirstName.setText("");
                                    StuLastName.setText("");
                                    GradeET.setText("");
                                    flagEditData = false;
                                } else {
                                    Student student = new Student(StuID.getText().toString(), StuFirstName.getText().toString(), StuLastName.getText().toString(), layer[selected_spin], Integer.parseInt(GradeET.getText().toString()), false, null, null);
                                    refStudents.child(student.getLayer()).child(String.valueOf(student.getGrade())).child(student.getFirstName() + " " + student.getLastName()).setValue(student);
                                    StuID.setText("");
                                    StuFirstName.setText("");
                                    StuLastName.setText("");
                                    LayerSpin.setSelection(0);
                                    GradeET.setText("");
                                    vaccinationCheck.setChecked(true);
                                }
                            }
                            else{
                                Toast.makeText(this, "This ID already in the system!", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            if (vaccinationCheck.isChecked()) {
                                Intent intent = new Intent(this, VaccinationDetails.class);
                                intent.putExtra("firstname", StuFirstName.getText().toString());
                                intent.putExtra("lastname", StuLastName.getText().toString());
                                intent.putExtra("layer", layer[selected_spin]);
                                intent.putExtra("grade", Integer.parseInt(GradeET.getText().toString()));
                                if (stuPos != null) {
                                    intent.putExtra("stuPos", stuPos);
                                } else {
                                    intent.putExtra("stuPos", "-1");
                                }
                                intent.putExtra("id", StuID.getText().toString());
                                startActivity(intent);
                                flagEditData = false;
                            } else {
                                Student student = new Student(StuID.getText().toString(), StuFirstName.getText().toString(), StuLastName.getText().toString(), layer[selected_spin], Integer.parseInt(GradeET.getText().toString()), false, null, null);
                                refStudents.child(student.getLayer()).child(String.valueOf(student.getGrade())).child(student.getFirstName() + " " + student.getLastName()).setValue(student);
                                StuID.setText("");
                                StuFirstName.setText("");
                                StuLastName.setText("");
                                LayerSpin.setSelection(0);
                                GradeET.setText("");
                                vaccinationCheck.setChecked(true);
                            }
                        }
                    }
                    else{
                        Toast.makeText(this, "Please enter valid ID!", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(this, "The grade number is too big no?", Toast.LENGTH_LONG).show();
                }
            } else{
                Toast.makeText(this, "select layer!", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this, "Invalid input! check your first and last name, make sure they contain only letters!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     *
     * @param view
     * this code is putting the position of the selected item in the layers spinner in a global variable
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected_spin = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     *
     * this code is putting the data in the fields in a situation that the user wants to edit someone's details.
     */
    @Override
    protected void onResume() {
        super.onResume();
        gi = getIntent();
        stuPos = gi.getStringExtra("pos");

        if (stuPos != null) {
            if (Integer.parseInt(stuPos) != -1) {
                Student tmp = stuList.get(Integer.parseInt(stuPos));
                StuID.setText(tmp.getId());
                StuFirstName.setText(tmp.getFirstName());
                StuLastName.setText(tmp.getLastName());
                LayerSpin.setSelection(Integer.parseInt(tmp.getLayer()) - 6);
                GradeET.setText(String.valueOf(tmp.getGrade()));
                if (tmp.isVaccinated()) {
                    vaccinationCheck.setChecked(true);
                } else {
                    vaccinationCheck.setChecked(false);
                }
                flagEditData = true;
            }
        }


        refStudents.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dS = task.getResult();
                for (DataSnapshot layer : dS.getChildren()) {
                    for (DataSnapshot grade: layer.getChildren()){
                        for (DataSnapshot stu: grade.getChildren()){
                            studentsArr.add(stu.getValue(Student.class));
                        }
                    }
                }
            }
        });
    }

    /**
     * <p>
     *     the function get a variable of View type,
     *     the function will transfer the user to the credits screen
     * </>
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * <p
     *      the function get a variable of MenuItem type
     * </>
     * @return the function will as the user choice will move to the credits screen or close the menu
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String st = item.getTitle().toString();
        if(st.equals("View all students")){
            Intent intent = new Intent(MainActivity.this, ViewDetails.class);
            startActivity(intent);
        }
        else if(st.equals("filter")){
            Intent intent = new Intent(MainActivity.this, filteredStudents.class);
            startActivity(intent);
        }
        else if(st.equals("Credits")){
            Intent intent = new Intent(MainActivity.this, Credits.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}