package com.example.huy.asynctask;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView image_Picture;
    private Button button_Previous, button_Next;
    private List<String> mLinks;
    private ProgressDialog mProgressDialog;
    private int mCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_Previous:
                if(mCurrentPosition == 0){
                    mCurrentPosition = mLinks.size();
                }
                mCurrentPosition--;
                new loadImageFromInternet().execute(mLinks.get(mCurrentPosition));
                break;
            case R.id.button_Next:
                mCurrentPosition++;
                if(mCurrentPosition == mLinks.size()){
                    mCurrentPosition = 0;
                }
                new loadImageFromInternet().execute(mLinks.get(mCurrentPosition));
            default:
                break;
        }
    }

    private void initView() {
        image_Picture = findViewById(R.id.image_Picture);
        button_Next = findViewById(R.id.button_Next);
        button_Previous = findViewById(R.id.button_Previous);
        mLinks = new ArrayList<>();
        mLinks = Arrays.asList(getResources().getStringArray(R.array.links));
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading");
        new loadImageFromInternet().execute(mLinks.get(mCurrentPosition));
    }

    public class loadImageFromInternet extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(strings[0]);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            image_Picture.setImageBitmap(bitmap);
            mProgressDialog.dismiss();
        }
    }
}
