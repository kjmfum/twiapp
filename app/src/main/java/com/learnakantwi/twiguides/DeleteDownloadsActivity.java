package com.learnakantwi.twiguides;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class DeleteDownloadsActivity extends AppCompatActivity {

    Button btDeleteVocabulary;
    Button btDeleteProverbs;
    Button btDeleteChildren;
    Button btDeleteConversation;
    Button btDeleteReading;
    Button btDeleteVerbs;
    Toast toast;


    public void deleteAllVocabulary(){
        int count =0;
        File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/");

        try {
            File [] files1 = myFiles.listFiles();

            if (files1.length>0) {
                for (File file : files1) {
                    count++;
                    boolean wasDeleted = file.delete();
                    if (!wasDeleted) {
                        System.out.println("Was not deleted");
                    }

                }
                if (count>0){
                    Toast.makeText(this, "Deleted "+ count + " Audio files", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (NullPointerException N){
            toast.setText("There are No Downloaded Proverbs Audio");
        }
        catch (Exception E){
            System.out.println("Errors "+ E);
            toast.setText("Couldn't Delete \n " +E);
        }

    }
    public void deleteAllProverbs(){
        int count =0;
        File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/PROVERBS/");

        try {
            File [] files1 = myFiles.listFiles();

            if (files1.length==0){
                toast.setText("There are No Downloaded Proverbs Audio");
                toast.show();
            }

            if (files1.length>0) {
                for (File file : files1) {
                    count++;
                    boolean wasDeleted = file.delete();
                    if (!wasDeleted) {
                        System.out.println("Was not deleted");
                    }

                }
               if (count>0){
                    Toast.makeText(this, "Deleted "+ count + " Audio files", Toast.LENGTH_SHORT).show();
                }

            }
        }catch (NullPointerException N){
            toast.setText("There are No Downloaded Proverbs Audio");
        }
        catch (Exception E){
            System.out.println("Errors "+ E);
            toast.setText("Couldn't Delete \n " +E);
        }

    }

    public void deleteAllChildren(){
        int count =0;
        File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/CHILDREN/");

        try {
            File [] files1 = myFiles.listFiles();

            if (files1.length==0){
                toast.setText("There are No Downloaded Children Audio");
                toast.show();
            }

            if (files1.length>0) {
                for (File file : files1) {
                    count++;
                    boolean wasDeleted = file.delete();
                    if (!wasDeleted) {
                        System.out.println("Was not deleted");
                    }

                }
                if (count>0){
                    Toast.makeText(this, "Deleted "+ count + " Audio files", Toast.LENGTH_SHORT).show();
                }

            }
        }catch (NullPointerException N){
            toast.setText("There are No Downloaded Children Audio");
        }
        catch (Exception E){
            System.out.println("Errors "+ E);
            toast.setText("Couldn't Delete \n " +E);
        }

    }

    public void deleteAllConversation(){
        int count =0;
        File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/SUBCONVERSATION/");

        try {
            File [] files1 = myFiles.listFiles();

            if (files1.length==0){
                toast.setText("There are No Downloaded Conversation Audio");
                toast.show();
            }

            if (files1.length>0) {
                for (File file : files1) {
                    count++;
                    boolean wasDeleted = file.delete();
                    if (!wasDeleted) {
                        System.out.println("Was not deleted");
                    }

                }
                if (count>0){
                    Toast.makeText(this, "Deleted "+ count + " Audio files", Toast.LENGTH_SHORT).show();
                }

            }
        }catch (NullPointerException N){
            toast.setText("There are No Downloaded Conversation Audio");
        }
        catch (Exception E){
            System.out.println("Errors "+ E);
            toast.setText("Couldn't Delete \n " +E);
        }

    }

    public void deleteAllReading(){
        int count =0;
        File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/READING/");

        try {
            File [] files1 = myFiles.listFiles();

            if (files1.length==0){
                toast.setText("There are No Downloaded Reading Audio");
                toast.show();
            }

            if (files1.length>0) {
                for (File file : files1) {
                    count++;
                    boolean wasDeleted = file.delete();
                    if (!wasDeleted) {
                        System.out.println("Was not deleted");
                    }

                }
                if (count>0){
                    Toast.makeText(this, "Deleted "+ count + " Audio files", Toast.LENGTH_SHORT).show();
                }

            }
        }catch (NullPointerException N){
            toast.setText("There are No Downloaded Reading Audio");
        }
        catch (Exception E){
            System.out.println("Errors "+ E);
            toast.setText("Couldn't Delete \n " +E);
        }

    }

    public void deleteAllVerbs(){
        int count =0;
        File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/VERBS/");

        try {
            File [] files1 = myFiles.listFiles();

            if (files1.length==0){
                toast.setText("There are No Downloaded Reading Audio");
                toast.show();
            }

            if (files1.length>0) {
                for (File file : files1) {
                    count++;
                    boolean wasDeleted = file.delete();
                    if (!wasDeleted) {
                        System.out.println("Was not deleted");
                    }

                }
                if (count>0){
                    Toast.makeText(this, "Deleted "+ count + " Audio files", Toast.LENGTH_SHORT).show();
                }

            }
        }catch (NullPointerException N){
            toast.setText("There are No Downloaded Verbs Audio");
        }
        catch (Exception E){
            System.out.println("Errors "+ E);
            toast.setText("Couldn't Delete \n " +E);
        }

    }

    public void Dialog(final String category) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.learnakantwiimage)
                .setTitle("Download Audio?")
                .setMessage("This will delete all " + category + " audio.\n " +
                        "Do you want to proceed?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //downloadAllClickCount++;
                        toast.setText("Deleting...");
                        toast.show();
                        // sharedDownloadingOrNot = "Yes";
                        switch (category){
                            case "Vocabulary":
                                deleteAllVocabulary();
                                return;
                            case "Proverbs":
                                deleteAllProverbs();
                                return;
                            case "Children":
                            deleteAllChildren();
                            return;
                            case "Conversation":
                                deleteAllConversation();
                                return;
                            case "Reading":
                            deleteAllReading();
                            return;
                            case "Verbs":
                                deleteAllVerbs();
                                return;
                        }
                        deleteAllVocabulary();
                        //Toast.makeText(Home.this, "Hi"+" "+ sharedDownloadingOrNot, Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("No", null)
                .show();

}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_downloads);

        toast= Toast.makeText(this, "", Toast.LENGTH_SHORT);

        btDeleteChildren = findViewById(R.id.btDeleteChildren);
        btDeleteConversation = findViewById(R.id.btDeleteConversation);
        btDeleteProverbs = findViewById(R.id.btDeleteProverbs);
        btDeleteVocabulary = findViewById(R.id.btDeleteVocabulary);
        btDeleteReading = findViewById(R.id.btDeleteReading);
        btDeleteVerbs = findViewById(R.id.btDeleteVerb);


        btDeleteVocabulary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog("Vocabulary");
            }
        });

        btDeleteProverbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog("Proverbs");
            }
        });

        btDeleteChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog("Children");
            }
        });

        btDeleteConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog("Conversation");
            }
        });

        btDeleteReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog("Reading");
            }
        });

        btDeleteVerbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog("Verbs");
            }
        });
    }


}
