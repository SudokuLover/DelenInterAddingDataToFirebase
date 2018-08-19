package com.example.gauranggoel.addingordersdelenintern;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    String time="";
    String id="";
    String key="";
    String workType="";
    String workerName="";
    String cost="";
    String status="";
    String address="";
    String url="";
    Button btn;

    MyOrder order;

    EditText et1,et2,et3,et4,et5,et6,et7;
    ImageView img;

    DatabaseReference databaseReference;
    StorageReference storage;

    Uri filePath=null;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference= FirebaseDatabase.getInstance().getReference(Refrences.DATABASE_REFRENCE);
        storage= FirebaseStorage.getInstance().getReference();

        et1=findViewById(R.id.trans_Id);
        et2=findViewById(R.id.workerType);
        et3=findViewById(R.id.workerName);
        et4=findViewById(R.id.status);
        et5=findViewById(R.id.cost);
        et6=findViewById(R.id.time);
        et7=findViewById(R.id.address);
        img=findViewById(R.id.imageView);
        btn=findViewById(R.id.submit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final ProgressDialog pd = new ProgressDialog(MainActivity.this);
                pd.setTitle("Uploading");
                pd.setCanceledOnTouchOutside(false);
                pd.show();

                id = et1.getText().toString();
                workerName = et2.getText().toString();
                workerName = et3.getText().toString();
                status = et4.getText().toString();
                cost = et5.getText().toString();
                time = et6.getText().toString();
                final int[] i = {0};
                address=et7.getText().toString();
                if(filePath!=null)
                {
                    StorageReference sref = storage.child(Refrences.STORAGE_REFRENCE + System.currentTimeMillis()+"."+getFileExt(filePath));
                    sref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            url = taskSnapshot.getDownloadUrl().toString();

                        }
                    });

/*
                    final ProgressDialog pd = new ProgressDialog(this);
                    pd.setTitle("Uploading");
                    pd.setCancelable(false);
                    pd.show();

                    //getting refrence storage

                    StorageReference sref = storage.child(Refrences.STORAGE_REFRENCE + System.currentTimeMillis()+"."+getFileExt(filePath));

                    //sref.putFile(filePath);
                    // need to do some standard task -> how to read that image -> provide url to realtime database -> so that can see image
                    // provide Progress dialog to show uploading
                    sref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();;

                            //displaying toast
                            Toast.makeText(MainActivity.this, "file uploaded", Toast.LENGTH_SHORT).show();
                            //creating the upload to object to store uploaded image data
                            Upload upload = new Upload(et.getText().toString().trim(),taskSnapshot.getDownloadUrl().toString());
                            //made an object of upload class -> pass name and url  in it
                            // now name and url pass to our db

                            //adding an upload to firebase database;

                            String uploadId = refrence.push().getKey();
                            refrence.child(uploadId).setValue(upload);
                        }

                    }).addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                            Toast.makeText(MainActivity.this, "task complete ", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            pd.setMessage("Uploaded "+((int)progress)+"%..");
                        }
                    });
                }*/
                }

                order = new MyOrder(time,id,workType,workerName,cost,status,address,url);

                String uploadId=databaseReference.push().getKey();
                databaseReference.child(uploadId).setValue(order);
                pd.dismiss();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,101);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        filePath = data.getData();

        try{
            InputStream inputStream = getContentResolver().openInputStream(filePath);
            bitmap= BitmapFactory.decodeStream(inputStream);
            img.setImageBitmap(bitmap);
        }
        catch(Exception e)
        {

        }

    }

    public String getFileExt(Uri filePath){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(filePath));
    }
}
