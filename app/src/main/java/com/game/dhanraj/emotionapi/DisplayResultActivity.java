package com.game.dhanraj.emotionapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.projectoxford.emotion.EmotionServiceClient;
import com.microsoft.projectoxford.emotion.EmotionServiceRestClient;
import com.microsoft.projectoxford.emotion.contract.RecognizeResult;
import com.microsoft.projectoxford.emotion.contract.Scores;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DisplayResultActivity extends AppCompatActivity {

    public EmotionServiceClient emotionServiceClient = new EmotionServiceRestClient("subscription key");
    private ImageView imageView ;
    private Button process,next;
    private String result="neutral";
    private TextView anger,disgust,fear,happiness,sadness,contempt,surprise,neutral,majoremotion;
    Bitmap b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_result);


        imageView = (ImageView) findViewById(R.id.imageview);
        process = (Button) findViewById(R.id.processbtn);
        next = (Button) findViewById(R.id.nextbtn);
        anger=(TextView) findViewById(R.id.anger);
        disgust = (TextView) findViewById(R.id.disgust);
        contempt = (TextView) findViewById(R.id.contempt);
        fear = (TextView) findViewById(R.id.fear);
        happiness = (TextView) findViewById(R.id.happiness);
        sadness = (TextView) findViewById(R.id.sadness);
        surprise = (TextView) findViewById(R.id.surprise);
        neutral = (TextView) findViewById(R.id.neutral);
        majoremotion = (TextView) findViewById(R.id.majoremotion);

         Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            String name = extras.getString("Dhanraj");

            if(name.equals("rar"))
            {
                b=extras.getParcelable("imageformcamera");
            }
           if(name.equals("ramram"))
           {
              Uri imageUri = Uri.parse(extras.getString("imageformcamera"));
            try {
                b= MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
           }

        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        
         imageView.setImageBitmap(b);
        
        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connectivityManager
                        = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {


                    AsyncTask<InputStream, String, List<RecognizeResult>> emotionTask = new AsyncTask<InputStream, String, List<RecognizeResult>>() {
                        ProgressDialog mDialog = new ProgressDialog(DisplayResultActivity.this);

                        @Override
                        protected List<RecognizeResult> doInBackground(InputStream... params) {
                            try {
                                publishProgress("Recognizing...");
                                List<RecognizeResult> result = emotionServiceClient.recognizeImage(params[0]);
                                return result;
                            } catch (Exception ex) {
                                return null;
                            }
                        }

                        @Override
                        protected void onPreExecute() {
                            mDialog.show();
                        }

                        @Override
                        protected void onPostExecute(List<RecognizeResult> recognizeResults) {
                            mDialog.dismiss();
                            for (RecognizeResult res : recognizeResults) {
                                String status = getEmo(res);
                                //Toast.makeText(DisplayResultActivity.this,result, Toast.LENGTH_SHORT).show();

                                imageView.setImageBitmap(ImageHelper.drawRectOnBitmap(b, res.faceRectangle, status));
                            }
                        }

                        @Override
                        protected void onProgressUpdate(String... values) {
                            mDialog.setMessage(values[0]);
                        }
                    };
                    emotionTask.execute(inputStream);

                    process.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.VISIBLE);
                  //  Toast.makeText(DisplayResultActivity.this,result, Toast.LENGTH_SHORT).show();
                }

                else
                {
                    Toast.makeText(DisplayResultActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DisplayResultActivity.this,SuggestionsActivity.class);
                i.putExtra("result1",result);
                startActivity(i);
                finish();
            }
        });


    }

    private String getEmo(RecognizeResult res) {
        List<Double> list = new ArrayList<>();
        Scores scores = res.scores;

        list.add(scores.anger);
        list.add(scores.happiness);
        list.add(scores.contempt);
        list.add(scores.disgust);
        list.add(scores.fear);
        list.add(scores.neutral);
        list.add(scores.sadness);
        list.add(scores.surprise);

        Collections.sort(list);

        for(int i=0;i<list.size();i++)
        {
            if(list.get(i)==scores.anger)
            {
                anger.setText("Anger : " + list.get(i).toString());
            }
            if(list.get(i)==scores.fear)
            {
                fear.setText("Fear : "+list.get(i).toString());
            }
            if(list.get(i)==scores.contempt)
            {
                contempt.setText("Contempt : "+list.get(i).toString());
            }
            if(list.get(i)==scores.disgust)
            {
                disgust.setText("Disgust : " +list.get(i).toString());
            }
            if(list.get(i)==scores.happiness)
            {
                happiness.setText("Happiness : " +list.get(i).toString());
            }
            if(list.get(i)==scores.neutral)
            {
                neutral.setText("Neutral : "+list.get(i).toString());
            }
            if(list.get(i)==scores.surprise)
            {
                surprise.setText("Surprise : " +list.get(i).toString());
            }
            if(list.get(i)==scores.sadness)
            {
                sadness.setText("Sadness : "+list.get(i).toString());
            }
        }

        double max = list.get(list.size()-1);
        if(max == scores.anger)
        {
            majoremotion.setText("Major Depicted Emotion : "+"Anger");
            result = "Anger";
            return "Anger";
        }

        else if(max == scores.fear) {
            majoremotion.setText("Major Depicted Emotion : "+"Fear");
            result = "Fear";
            return "Fear";
        }
        else if(max == scores.happiness)
        {
            majoremotion.setText("Major Depicted Emotion : "+"Happiness");
            result = "Happiness";
            return "Happiness";
        }
        else if(max == scores.disgust)
        {
            majoremotion.setText("Major Depicted Emotion : "+"Disgust");
            result = "Disgust";
            return "Disgust";
        }
       else  if(max == scores.contempt)
        {
            majoremotion.setText("Major Depicted Emotion : "+"Contempt");
            result = "Contempt";
            return "Contempt";
        }
       else  if(max == scores.sadness)
        {
            majoremotion.setText("Major Depicted Emotion : "+"Sadness");
            result = "Sadness";
            return "Sadness";
        }
       else  if(max == scores.surprise)
        {
            majoremotion.setText("Major Depicted Emotion : "+"Surprise");
            result = "Surprise";
            return "Surprise";
        }
       else  if(max == scores.neutral)
        {
            majoremotion.setText("Major Depicted Emotion : "+"Neutral");
            result = "Neutral";
            return "Neutral";
        }
        else
            return "Neutral";


    }



}


