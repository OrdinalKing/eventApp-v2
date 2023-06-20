package com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;


import java.sql.Time;
import java.text.BreakIterator;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import kotlinx.coroutines.android.HandlerDispatcher;

public class CreateEventActivity extends AppCompatActivity {
    public Button back;
    public Button create;
    public EditText title;
    public EditText description;
    public TextView dateText;
    public TextView timeText;
    public ImageButton datePicker;
    public ImageButton timePicker;
    public EditText location;
    public ImageView imageUpload;
    public Calendar calendar = Calendar.getInstance();
    public int year = calendar.get(Calendar.YEAR);
    public int month = calendar.get(Calendar.MONTH);
    public int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    public int hour = calendar.get(Calendar.HOUR_OF_DAY);
    public int minute = calendar.get(Calendar.MINUTE);
    private Uri imageUri;
    private String imageUrl;
    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        back = findViewById(R.id.back);
        create = findViewById(R.id.create);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        dateText = findViewById(R.id.dateText);
        datePicker = findViewById(R.id.datePicker);
        timeText = findViewById(R.id.timeText);
        timePicker = findViewById(R.id.timePicker);
        location = findViewById(R.id.location);
        imageUpload = findViewById(R.id.imageUpload);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Update the selected date text view with the selected date
                                dateText.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        CreateEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);

                                timeText.setText(selectedTime);
                            }
                        },
                        hour,
                        minute,
                        android.text.format.DateFormat.is24HourFormat(CreateEventActivity.this)
                );

                timePickerDialog.show();

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateEventActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().toString() == null || title.getText().toString().trim().isEmpty()){
                    Toast.makeText(CreateEventActivity.this, "Please insert title!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (description.getText().toString() == null || description.getText().toString().trim().isEmpty()){
                    Toast.makeText(CreateEventActivity.this, "Please insert description!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (location.getText().toString() == null || location.getText().toString().trim().isEmpty()){
                    Toast.makeText(CreateEventActivity.this, "Please insert location!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (timeText.getText().toString() == null || timeText.getText().toString().trim().isEmpty()){
                    Toast.makeText(CreateEventActivity.this, "Please insert time!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dateText.getText().toString() == null || dateText.getText().toString().trim().isEmpty()){
                    Toast.makeText(CreateEventActivity.this, "Please insert date!", Toast.LENGTH_SHORT).show();
                    return;
                }

                upload();

            }
        });

        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    private String getFileExtension(Uri uri){
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }

    private void upload() {

        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri !=null){
            StorageReference filePath = FirebaseStorage.getInstance().getReference("images").child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            StorageTask uploadtask = filePath.putFile(imageUri);
            uploadtask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Log.d("--Create Event ---", description.getText().toString());
                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();
                    HashMap<String, Object> eventData = new HashMap<> ();
                    eventData.put("title", title.getText().toString());
                    eventData.put("description", description.getText().toString());

                    eventData.put("location", location.getText().toString());
                    eventData.put("hours", "2");
                    eventData.put("image", imageUrl);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DATE, dayOfMonth);
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);

                    Date date = calendar.getTime();
                    Timestamp timestamp = new Timestamp(date);
                    eventData.put("date", timestamp);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("events")
                            .add(eventData)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("FireStore", "DocumentSnapshot written with ID: " + documentReference.getId());
                                    startActivity(new Intent(CreateEventActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("FireStore", "Error adding document", e);
                                    startActivity(new Intent(CreateEventActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                }
                            });
                    pd.dismiss();
                    ;                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    startActivity(new Intent(CreateEventActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageUpload.setImageURI(imageUri);
        }
        else {
            Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
        }
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            imageUri = result.getUri();
//            imageUpload.setImageURI(imageUri);
//
//        }
//        else {
//            Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
//        }
//    }
}