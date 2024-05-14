package com.example.firebase;

import static com.example.firebase.MainActivity.refStudents;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewDetails extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static ArrayList<Student> stuList;
    ArrayList<String> stuNames;
    ListView allStuLv;
    ArrayAdapter<String> adp;

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
        setContentView(R.layout.activity_view_details);

        stuList = new ArrayList<>();
        stuNames = new ArrayList<>();
        allStuLv = findViewById(R.id.allStudentsLv);

        stuList.clear();
        stuNames.clear();
        adp = new ArrayAdapter<>(ViewDetails.this,
                android.R.layout.simple_spinner_item, stuNames);
        allStuLv.setAdapter(adp);

        allStuLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent si = new Intent(ViewDetails.this, MainActivity.class);
                si.putExtra("pos", String.valueOf(position));
                startActivity(si);
                return false;
            }
        });
        allStuLv.setOnItemClickListener(this);
    }

    /**
     *
     * this code is putting all the students that in the fire base into arraylist
     */
    @Override
    protected void onStart() {
        super.onStart();
        for (int i = 7 ; i < 13 ; i++){
            refStudents.child(String.valueOf(i)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataSnapshot dS = task.getResult();
                        for (DataSnapshot grade : dS.getChildren()) {
                            for (DataSnapshot stu : grade.getChildren()) {
                                Student tmp = stu.getValue(Student.class);
                                stuList.add(tmp);
                                stuNames.add(tmp.getFirstName() + " " + tmp.getLastName());
                            }
                        }
                        adp.notifyDataSetChanged();
                    }
                    else {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                }
            });
        }
    }

    /**
     *
     * @param tmp
     * this code is creates a text with the details of the student the it got as parameter.
     */
    public static String details(Student tmp){
        if (tmp.isVaccinated()){
            if (tmp.getFirstVaccination() == null && tmp.getSecondVaccination() == null)
                return ("Student name: "+tmp.getFirstName()+" "+tmp.getLastName()+"\n"+
                        "layer: "+tmp.getLayer()+"\n"+
                        "grade: "+tmp.getGrade()+"\n"+
                        "The student haven't vaccinated yet.");
            else if (tmp.getFirstVaccination() != null && tmp.getSecondVaccination() == null){
                return ("Student name: "+tmp.getFirstName()+" "+tmp.getLastName()+"\n"+
                        "layer: "+tmp.getLayer()+"\n"+
                        "grade: "+tmp.getGrade()+"\n"+
                        "First vaccination were at "+tmp.getFirstVaccination().getPlace()+" in "+tmp.getFirstVaccination().getDateOfVaccination()+".\n"+
                        "The student haven't vaccinated the second vaccination yet.");
            }
            else if (tmp.getFirstVaccination() != null && tmp.getSecondVaccination() != null){
                return ("Student name: "+tmp.getFirstName()+" "+tmp.getLastName()+"\n"+
                        "layer: "+tmp.getLayer()+"\n"+
                        "grade: "+tmp.getGrade()+"\n"+
                        "First vaccination were at "+tmp.getFirstVaccination().getPlace()+" in "+tmp.getFirstVaccination().getDateOfVaccination()+".\n"+
                        "Second vaccination were at "+tmp.getSecondVaccination().getPlace()+" in "+tmp.getSecondVaccination().getDateOfVaccination()+".");
            }
        }
        else{
            return ("Student name: "+tmp.getFirstName()+" "+tmp.getLastName()+"\n"+
                    "layer: "+tmp.getLayer()+"\n"+
                    "grade: "+tmp.getGrade()+"\n"+
                    "the student can't be vaccinated");
        }
        return null;
    }

    /**
     *
     * @param view
     * this code is showing the details of the selected student
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Student tmp = stuList.get(position);
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setMessage(details(tmp));

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
            Intent intent = new Intent(ViewDetails.this, MainActivity.class);
            startActivity(intent);
        }
        else if(st.equals("filter")){
            Intent intent = new Intent(ViewDetails.this, filteredStudents.class);
            startActivity(intent);
        }
        else if(st.equals("Credits")){
            Intent intent = new Intent(ViewDetails.this, Credits.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}