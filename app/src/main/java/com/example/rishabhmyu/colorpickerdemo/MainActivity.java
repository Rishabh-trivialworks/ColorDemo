package com.example.rishabhmyu.colorpickerdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {
    private static final int REQUEST_CAMERA_PERMISSION_RESULT = 0;
    SimpleDrawingView objSimpleDrawingView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VerticalSlideColorPicker colorPicker = (VerticalSlideColorPicker) findViewById(R.id.color_picker);
        this.objSimpleDrawingView = (SimpleDrawingView) findViewById(R.id.simpleDrawingView1);
//        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
//            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
//        }
        findViewById(R.id.textcolor).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.objSimpleDrawingView.onClickUndo();
            }
        });
        colorPicker.setOnColorChangeListener(new VerticalSlideColorPicker.OnColorChangeListener() {
            public void onColorChange(int selectedColor) {
                if (selectedColor != 0) {
                    MainActivity.this.objSimpleDrawingView.setPathColor(selectedColor);
                }
            }
        });
    }

    private void savingFile() {
        FileOutputStream fileOutputStream;
        NullPointerException e;
        FileNotFoundException e2;
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFiles");
        if (!folder.exists()) {
            Log.i("hello", " " + folder.mkdirs());
        }
        File file = new File(folder, "drawing.png");
        if (!file.exists()) {
            try {
                boolean success = file.createNewFile();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        try {
            FileOutputStream ostream = new FileOutputStream(file);
            try {
                Bitmap well = this.objSimpleDrawingView.getBitmap();
                Bitmap save = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);
                Paint paint = new Paint();
                paint.setColor(-1);
                Canvas now = new Canvas(save);
                now.drawRect(new Rect(0, 0, 320, 480), paint);
                now.drawBitmap(well, new Rect(0, 0, well.getWidth(), well.getHeight()), new Rect(0, 0, 320, 480), null);
                if (save == null) {
                    save.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    Toast.makeText(this, "File saved", Toast.LENGTH_SHORT).show();
                } else {
                    save.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    Toast.makeText(this, "File saved",Toast.LENGTH_SHORT).show();
                }
                fileOutputStream = ostream;
            } catch (NullPointerException e4) {
                e = e4;
                fileOutputStream = ostream;
                e.printStackTrace();
                Toast.makeText(this, "Null error", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e6) {
            e = e6;
            e.printStackTrace();
            Toast.makeText(this, "Null error", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e7) {
            e2 = e7;
            e2.printStackTrace();
            Toast.makeText(this, "File error",Toast.LENGTH_SHORT).show();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults[0] == 0) {
            Toast.makeText(this, "permission successfull", Toast.LENGTH_SHORT).show();
        }
    }

}
