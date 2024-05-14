package com.example.firebase;

import static com.example.firebase.MainActivity.refStudents;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class filteredStudents extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    Spinner filterSpin, layerSpinner, gradeSpinner;
    ListView StudentsLv;
    ArrayList<String> filterAr, layerAr, gradeAr, studentsAr, allStudentsNames;
    ArrayList<Student> allStudents;
    int[] loc;
    ArrayAdapter<String> adp;
    ArrayAdapter adpFilter, layeradp, studentsLvAdp, gradeAdp;


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
        setContentView(R.layout.activity_filtered_students);

        filterSpin = findViewById(R.id.filterSpin);
        layerSpinner = findViewById(R.id.LayerSpinner);
        gradeSpinner = findViewById(R.id.GradeSpinner);
        StudentsLv = findViewById(R.id.StudentsLv);
        StudentsLv.setOnItemClickListener(this);

        filterAr = new ArrayList<>();
        filterAr.add("Choose a filter");
        filterAr.add("By class");
        filterAr.add("By layer");
        filterAr.add("See all");
        filterAr.add("unvaccinable");
        adpFilter = new ArrayAdapter(filteredStudents.this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item, filterAr);
        filterSpin.setAdapter(adpFilter);
        filterSpin.setOnItemSelectedListener(this);

        layerAr = new ArrayList<>();
        layerAr.add("7");
        layerAr.add("8");
        layerAr.add("9");
        layerAr.add("10");
        layerAr.add("11");
        layerAr.add("12");
        layeradp = new ArrayAdapter(filteredStudents.this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item, layerAr);
        layerSpinner.setAdapter(layeradp);
        layerSpinner.setOnItemSelectedListener(this);


        allStudents = new ArrayList<>();
        allStudentsNames = new ArrayList<>();
        studentsLvAdp = new ArrayAdapter(filteredStudents.this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item, allStudentsNames);
        StudentsLv.setAdapter(studentsLvAdp);

        gradeAr = new ArrayList<>();
        gradeAdp = new ArrayAdapter(filteredStudents.this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item, gradeAr);
        gradeSpinner.setAdapter(gradeAdp);
        gradeSpinner.setOnItemSelectedListener(this);
    }

    /**
     *
     * this code is putting all the students that in the fire base into arraylist
     */
    @Override
    protected void onStart() {
        super.onStart();

        for (int i = 7; i < 13; i++) {
            refStudents.child(String.valueOf(i)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataSnapshot dS = task.getResult();
                        for (DataSnapshot grade : dS.getChildren()) {
                            for (DataSnapshot stu : grade.getChildren()) {
                                Student tmp = stu.getValue(Student.class);
                                allStudents.add(tmp);
                                allStudentsNames.add(tmp.getFirstName() + " " + tmp.getLastName());
                            }
                        }
                    } else {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                }
            });
            studentsLvAdp.notifyDataSetChanged();
        }
    }

    /**
     *
     * this code is showing to the user the students by the chosen filter.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        allStudents.clear();
        allStudentsNames.clear();
        Query query;
        if (parent.equals(filterSpin)) {
            if (position == 1 || position == 2) {
                layerSpinner.setVisibility(View.VISIBLE);
                if (position == 1) {
                    gradeSpinner.setVisibility(View.VISIBLE);
                }
                else {
                    gradeSpinner.setVisibility(View.INVISIBLE);
                }
            }
            else{
                layerSpinner.setVisibility(View.INVISIBLE);

                if (position == 3){
                    query = refStudents.orderByKey();
                    query.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            DataSnapshot dS = task.getResult();
                            for (DataSnapshot layer : dS.getChildren()) {
                                for (DataSnapshot grade: layer.getChildren()){
                                    for (DataSnapshot stu: grade.getChildren()){
                                        Student tmp = stu.getValue(Student.class);
                                        if (tmp.isVaccinated() && tmp.getSecondVaccination() != null){
                                            allStudents.add(tmp);
                                            allStudentsNames.add(tmp.getFirstName() + " " + tmp.getLastName());
                                        }
                                    }
                                }
                            }
                            studentsLvAdp.notifyDataSetChanged();
                        }
                    });
                }

                else if (position == 4){
                    query = refStudents.orderByKey();
                    query.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            DataSnapshot dS = task.getResult();
                            for (DataSnapshot layer : dS.getChildren()) {
                                for (DataSnapshot grade: layer.getChildren()){
                                    for (DataSnapshot stu: grade.getChildren()){
                                        Student tmp = stu.getValue(Student.class);
                                        if (!tmp.isVaccinated()){
                                            allStudents.add(tmp);
                                            allStudentsNames.add(tmp.getFirstName() + " " + tmp.getLastName());
                                        }
                                    }
                                }
                            }
                            studentsLvAdp.notifyDataSetChanged();
                        }
                    });
                }
            }
        }

        else if (parent.equals(layerSpinner)) {
            gradeAr.clear();
            query = refStudents.child(String.valueOf(layerAr.get(position))).orderByKey();
            query.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    DataSnapshot dS = task.getResult();
                    for (DataSnapshot grade : dS.getChildren()) {
                        gradeAr.add(grade.getKey());
                        for (DataSnapshot stu: grade.getChildren()){
                            if (stu.getValue() != null){
                                Student tmp = stu.getValue(Student.class);
                                if (tmp.isVaccinated() && tmp.getSecondVaccination() != null){
                                    if (gradeSpinner.getVisibility() == View.VISIBLE){
                                        if (gradeAr.get(0).equals(tmp.getGrade())){
                                            allStudents.add(tmp);
                                            allStudentsNames.add(tmp.getFirstName() + " " + tmp.getLastName());
                                        }
                                    }
                                    else{
                                        allStudents.add(tmp);
                                        allStudentsNames.add(tmp.getFirstName() + " " + tmp.getLastName());
                                    }
                                }
                            }
                        }
                    }
                    studentsLvAdp.notifyDataSetChanged();
                    gradeAdp.notifyDataSetChanged();
                }
            });
        }

        else {
            if (!gradeAr.isEmpty()) {
                query = refStudents.child(layerSpinner.getSelectedItem().toString()).child(gradeSpinner.getSelectedItem().toString()).orderByKey();
                query.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        DataSnapshot dS = task.getResult();
                        for (DataSnapshot stu : dS.getChildren()) {
                            if (stu.getValue() != null) {
                                Student tmp = stu.getValue(Student.class);
                                if (tmp.isVaccinated() && tmp.getSecondVaccination() != null) {
                                    allStudents.add(tmp);
                                    allStudentsNames.add(tmp.getFirstName() + " " + tmp.getLastName());
                                }
                            }
                        }
                        studentsLvAdp.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     *
     * @param view
     * this code is showing the details of the selected student
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setMessage(ViewDetails.details(allStudents.get(position)));

        AlertDialog ad = adb.create();
        ad.show();
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
        if(st.equals("Add Student")){
            Intent intent = new Intent(filteredStudents.this, MainActivity.class);
            startActivity(intent);
        }
        else if(st.equals("View all students")){
            Intent intent = new Intent(filteredStudents.this, ViewDetails.class);
            startActivity(intent);
        }
        else if(st.equals("Credits")){
            Intent intent = new Intent(filteredStudents.this, Credits.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}