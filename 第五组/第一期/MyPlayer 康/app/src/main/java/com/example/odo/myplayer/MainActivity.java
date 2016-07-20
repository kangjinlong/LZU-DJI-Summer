package com.example.odo.myplayer;


import android.content.res.AssetManager;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener, View.OnClickListener {
    private MediaPlayer mediaPlayer;
    private Surface surface;
    private Button[] button = new Button[3];
    private TextureView textureView;
    private Thread v;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textureView = (TextureView) findViewById(R.id.movie_display);

        textureView.setSurfaceTextureListener(this);
        button[0] = (Button) findViewById(R.id.pause);
        button[1] = (Button) findViewById(R.id.play);
        button[2] = (Button) findViewById(R.id.stop);
        for (Button b : button) {
            b.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.pause) {
            mediaPlayer.pause();
            return;
        }
        if (v.getId() == R.id.stop) {
            mediaPlayer.stop();
        }
        if (v.getId() == R.id.play) {
            mediaPlayer.start();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        System.out.println("onSurfaceTextureAvailable onSurfaceTextureAvailable");
        surface = new Surface(surfaceTexture);
        new PlayVideo().start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        System.out.println("onSurfaceTextureSizeChanged onSurfaceTextureSizeChanged");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        System.out.println("onSurfaceTextureDestroyed onSurfaceTextureDestroyed");
        surfaceTexture = null;
        surface = null;
        mediaPlayer.stop();
        mediaPlayer.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    private class PlayVideo extends Thread {
        @Override
        public void run() {
            try {
                File file = new File(Environment.getExternalStorageDirectory() + "/ansen.mp4");
                if (!file.exists()) {
                    copyFile();
                }
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(file.getAbsolutePath());
                mediaPlayer.setSurface(surface);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });
                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void copyFile()  {
        AssetManager assetManager = this.getAssets();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = getAssets().open("ansen.mp4");
            String file = Environment.getExternalStorageDirectory() + "/ansen.mp4";
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);

            }
            inputStream.close();
            inputStream = null;
            outputStream.flush();
            outputStream.close();
            outputStream = null;
        } catch (Exception e) {
            Log.e("Tag", e.getMessage());
        }
    }
}