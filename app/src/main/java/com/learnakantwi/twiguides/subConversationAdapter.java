package com.learnakantwi.twiguides;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class subConversationAdapter extends BaseAdapter implements Filterable {



    Context context;
    ArrayList<subConversation> originalArray , tempArray;
    //TimeAdapter.CustomFilter cf;

    StorageReference storageReference;
    MediaPlayer playFromDevice;
    MediaPlayer mp1;

  Toast toast;



    public subConversationAdapter(Context context, ArrayList<subConversation> originalArray) {
        this.context = context;
        this.originalArray = originalArray;
        this.tempArray = originalArray;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void playFromFirebase(StorageReference musicRef) {


        if (isNetworkAvailable()) {

            try {
                final File localFile = File.createTempFile("aduonu", "m4a");

                if (localFile != null) {
                    musicRef.getFile(localFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {


                                    if (mp1 != null) {
                                        mp1.stop();
                                        mp1.reset();
                                        mp1.release();
                                    }
                                    mp1 = new MediaPlayer();

                                    try {
                                        mp1.setDataSource(context.getApplicationContext(), Uri.fromFile(localFile));
                                        mp1.prepareAsync();
                                        mp1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                            @Override
                                            public void onPrepared(MediaPlayer mp) {
                                                mp.start();
                                               /* if (!mp1.isPlaying() && !playFromDevice.isPlaying()) {
                                                    mp.start();
                                                }*/
                                            }
                                        });
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle failed download
                            // ...
                          /*  toast.setText("Unable to download now. Please try later");*/
                        }
                    });
                } else {
                    /*toast.setText("Unable to download now. Please try later");
                    toast.show();*/
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        else {
           /* toast.setText("Please Connect to the Internet");
            toast.show();*/
        }

    }

    public void playFromFileOrDownload(final String filename){
        toast = Toast.makeText(context,filename,Toast.LENGTH_LONG);

        File myFile = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/SUBCONVERSATION/"+filename+ ".m4a");
        if (myFile.exists()){



            try {
                if (playFromDevice != null){
                    playFromDevice.pause();
                    playFromDevice.reset();
                    playFromDevice.release();
                }

                playFromDevice = new MediaPlayer();

                playFromDevice.setDataSource(myFile.toString());
                playFromDevice.prepareAsync();
                playFromDevice.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                      /* toast.setText("Got here");
                        toast.show();*/
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            if (isNetworkAvailable()){
                StorageReference storageReference= FirebaseStorage.getInstance().getReference();
                final StorageReference musicRef = storageReference.child("/Conversations/" + filename + ".m4a");
                musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String url = uri.toString();
                        playFromFirebase(musicRef);
                        downloadFile(context.getApplicationContext(), filename, ".m4a", url);

                        /*toast.setText(appearText);
                        toast.show();*/

                        //Toast.makeText(getApplicationContext(), appearText, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context.getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                /*toast.setText("Please connect to Internet to download audio");
                toast.show();*/
            }


        }
    }

    public void downloadFile(final Context context, final String filename, final String fileExtension, final String url) {


        if (isNetworkAvailable()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(url);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setVisibleInDownloadsUi(false);
                    request.setDestinationInExternalFilesDir(context.getApplicationContext(), Environment.DIRECTORY_MUSIC+ "/SUBCONVERSATION", filename + fileExtension);
                    downloadManager.enqueue(request);
                }
            };
            Thread myThread = new Thread(runnable);
            myThread.start();
        }
        else
        {
           /* toast.setText("No Internet");
            toast.show();*/

        }
    }

    public void downloadOnly(final String filename){
        File myFiles = new File("/storage/emulated/0/Android/data/com.learnakantwi.twiguides/files/Music/SUBCONVERSATION/"+filename+".m4a");

        if (!myFiles.exists()){
            if (isNetworkAvailable()){
                StorageReference storageReference= FirebaseStorage.getInstance().getReference();
                final StorageReference musicRef = storageReference.child("/Conversations/" + filename + ".m4a");

                musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        downloadFile(context.getApplicationContext(), filename, ".m4a", url);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    /*toast.setText("Lost Internet Connection");
                    toast.show();*/
                        Toast.makeText(context, "Lost Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                Toast.makeText(context, "Please connect to Internet to download audio", Toast.LENGTH_SHORT).show();
           /* toast.setText("Please connect to Internet to download audio");
            toast.show();*/
            }
        }
        else {
            toast = Toast.makeText(context,"File Already Downloaded",Toast.LENGTH_LONG);
            toast.show();
           // Toast.makeText(context,"File Already Downloaded" , Toast.LENGTH_SHORT).show();
        }


    }

    public void update(ArrayList<subConversation> results){
        originalArray = new ArrayList<>();
        originalArray.addAll(results);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return originalArray.size();
    }

    @Override
    public Object getItem(int position) {
        return originalArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView= inflater.inflate(R.layout.activity_sub_conversation, null);

        final TextView tvTwi = convertView.findViewById(R.id.tvTwiSpeech);
        final TextView tvEnglish = convertView.findViewById(R.id.tvEnglishSpeech);
        final ImageView ivDownload = convertView.findViewById(R.id.btDownload);
        final LinearLayout linearLayout = convertView.findViewById(R.id.llayHorizontal);

        tvTwi.setText(originalArray.get(position).getTwiConversation());
        tvEnglish.setText(originalArray.get(position).getEnglishConversation());
        //if image would have been --- setImageResource.


        ivDownload.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {

                                                      String tvText = tvTwi.getText().toString();


                                                      PlayFromFirebase playFromFirebase1 = new PlayFromFirebase();

                                                      String filename = PlayFromFirebase.viewTextConvert(tvText);

                                                      downloadOnly(filename);

                                                  }
                                              });

        tvTwi.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String tvText = tvTwi.getText().toString();
                PlayFromFirebase playFromFirebase1 = new PlayFromFirebase();
                String filename = PlayFromFirebase.viewTextConvert(tvText);
                downloadOnly(filename);
                return false;
            }
        });

        tvEnglish.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String tvText = tvTwi.getText().toString();
                PlayFromFirebase playFromFirebase1 = new PlayFromFirebase();
                String filename = PlayFromFirebase.viewTextConvert(tvText);
                downloadOnly(filename);
                return true;
            }
        });

        tvTwi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTwi.setBackgroundColor(Color.GREEN);
                tvEnglish.setBackgroundColor(Color.GREEN);
                linearLayout.setBackgroundColor(Color.GREEN);

                String tvText = tvTwi.getText().toString();


                PlayFromFirebase playFromFirebase1 = new PlayFromFirebase();

                String filename = PlayFromFirebase.viewTextConvert(tvText);

                                                    //playFromFirebase1.allClick(filename);

                playFromFileOrDownload(filename);

               /* toast = Toast.makeText(context,tvText,Toast.LENGTH_LONG);
                toast.show();*/

                                                    //Toast.makeText(context, "Yes that's right", Toast.LENGTH_SHORT).show();
                                                    //Toast.makeText(context, tvText, Toast.LENGTH_SHORT).show();

                                                    //btDownload.setBackgroundColor(Color.GREEN);



                tvTwi.setTextColor(Color.WHITE);
                tvEnglish.setTextColor(Color.WHITE);

                new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            tvTwi.setBackgroundColor(Color.WHITE);
                                                            tvEnglish.setBackgroundColor(Color.WHITE);
                                                            linearLayout.setBackgroundColor(Color.WHITE);


                                                            //btDownload.setBackgroundColor(Color.WHITE);

                                                            tvTwi.setTextColor(Color.GREEN);
                                                            tvEnglish.setTextColor(Color.BLACK);
                                                        }
                                                    }, 2500);


                                                }
                                            });

        tvEnglish.setOnClickListener(new
                                         View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                // Toast.makeText(context, tvTwi.getText().toString(), Toast.LENGTH_SHORT).show();
                                                 tvTwi.setBackgroundColor(Color.GREEN);
                                                 tvEnglish.setBackgroundColor(Color.GREEN);
                                                 linearLayout.setBackgroundColor(Color.GREEN);

                                                 tvTwi.setTextColor(Color.WHITE);
                                                 tvEnglish.setTextColor(Color.WHITE);



                                                 String tvText = tvTwi.getText().toString();


                                                 PlayFromFirebase playFromFirebase1 = new PlayFromFirebase();

                                                 String filename = PlayFromFirebase.viewTextConvert(tvText);

                                                 //playFromFirebase1.allClick(filename);

                                                 playFromFileOrDownload(filename);


                                                 // Toast.makeText(filename).show();
                                                // Toast.makeText(context, filename, Toast.LENGTH_SHORT).show();

                                              /*   PlayFromFirebase playFromFirebase1 = new PlayFromFirebase();

                                                 playFromFirebase1.viewTextConvert(filename);


                                                playFromFileOrDownload(filename);*/

                                                 new Handler().postDelayed(new Runnable() {
                                                     @Override
                                                     public void run() {
                                                         tvTwi.setBackgroundColor(Color.WHITE);
                                                         tvEnglish.setBackgroundColor(Color.WHITE);
                                                         linearLayout.setBackgroundColor(Color.WHITE);

                                                         tvTwi.setTextColor(Color.GREEN);
                                                         tvEnglish.setTextColor(Color.BLACK);
                                                     }
                                                 }, 2500);


                                             }
                                         });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

}