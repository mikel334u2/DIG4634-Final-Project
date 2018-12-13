package com.example.owner.finalproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity2 extends AppCompatActivity {

    int r;
    int g;
    int b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Bundle intent = getIntent().getExtras();
        String f2_color = intent.getString("f2_color");

        TextView i_Hex = findViewById(R.id.hexadecimal);
        i_Hex.setText (f2_color);
        //String i_strHex = i_Hex.getText().toString();

        ImageView image00 = findViewById(R.id.image00);
        //image00.setBackgroundColor(Color.parseColor(i_strHex));
        image00.setBackgroundColor(Color.parseColor(f2_color));
    }

    public void onComplementaryClicked(View view)
    {
        /*
        TextView i_Hex = findViewById(R.id.hexadecimal);
        String i_strHex = i_Hex.getText().toString();
        Log.d("values", i_strHex);

        int i_value = Integer.parseInt(i_strHex, 16);
        Log.d("values", "i_value: " + i_value);

        String cal_strhex = Integer.toHexString(i_value);
        Log.d("values", "cal_strhex: " + cal_strhex);

        //int cal_value = Integer.parseInt(cal_strhex);
        //Log.d("values", "cal_value: " + cal_value);
        //int ivalue = Integer.valueOf(iHexDec.getText().toString());
        int white = 0xFFFFFF;
        String cal_white = Integer.toHexString(white);
        int dec_white = Integer.parseInt(cal_white, 16);

        Log.d("values", "dec_white: " + dec_white);

        int cal_comp = i_value - dec_white;
        Log.d("values", "cal_comp: " + cal_comp);

        String f_strhex = Integer.toHexString(cal_comp);
        //String f_strhex = String.valueOf(blah);
        //String f_strhex = String.format("#", (0xFFFFFF - i_value));
        Log.d("values", "f_strHex: " + f_strhex);
        TextView comp = findViewById(R.id.complementary);
        comp.setText(f_strhex);
        //compDec.setText(String.valueOf(nf.format(compValue)));
        */

        TextView i_Hex = findViewById(R.id.hexadecimal);
        String i_strHex = i_Hex.getText().toString();
        hex2Rgb(i_strHex);
        Log.d("values", "i_strHex: " + i_strHex);

        /*
        r = r *-1 + 255;
        g = g *-1 + 255;
        b = b *-1 + 255;
        */

        r = (~r) & 0xff;
        g = (~g) & 0xff;
        b = (~b) & 0xff;

        String f_strhex = String.format("#%02X%02X%02X", r, g, b).toUpperCase();
        TextView comp = findViewById(R.id.color01);
        comp.setText(f_strhex);
        Log.d("values", "complementary: " + f_strhex);

        ImageView image01 = findViewById(R.id.image01);
        image01.setBackgroundColor(Color.rgb(r, g, b));

        ImageView image02 = findViewById(R.id.image02);
        image02.setBackgroundColor(Color.TRANSPARENT);
        TextView color2 = findViewById(R.id.color02);
        color2.setText ("");
    }

    public void onMonochromaticClicked(View view)
    {
        int random = (int)(Math.random() * 50 + 1);

        TextView i_Hex = findViewById(R.id.hexadecimal);
        String i_strHex = i_Hex.getText().toString();

        i_strHex = i_strHex.replace("#", "");
        int i_value = Integer.parseInt(i_strHex, 16);
        Log.d("values", "i_value: " + i_value); //LOG

        int cal_mono = i_value - random;

        String f_strhex = Integer.toHexString(cal_mono).toUpperCase();
        TextView mono = findViewById(R.id.color01);
        mono.setText ("#" + f_strhex);

        Log.d("values", "monochromatic: " + f_strhex);

        ImageView image01 = findViewById(R.id.image01);
        image01.setBackgroundColor(Color.parseColor("#" + f_strhex));

        //Clears color 2
        ImageView image02 = findViewById(R.id.image02);
        image02.setBackgroundColor(Color.TRANSPARENT);
        TextView color2 = findViewById(R.id.color02);
        color2.setText ("");

    }

    public void onAnalogousClicked(View view)
    {
        int random = (int)(Math.random() * 150 + 10);

        TextView i_Hex = findViewById(R.id.hexadecimal);
        String i_strHex = i_Hex.getText().toString();

        i_strHex = i_strHex.replace("#", "");
        int i_value = Integer.parseInt(i_strHex, 16);
        Log.d("values", "i_value: " + i_value); //LOG

        int cal_ana01 = i_value + random;
        int cal_ana02 = i_value - random;

        //First color
        String f_strhex01 = Integer.toHexString(cal_ana01).toUpperCase();
        TextView ana01 = findViewById(R.id.color01);
        ana01.setText ("#" + f_strhex01);

        ImageView image01 = findViewById(R.id.image01);
        image01.setBackgroundColor(Color.parseColor("#" + f_strhex01));

        //Second color
        String f_strhex02 = Integer.toHexString(cal_ana02).toUpperCase();
        TextView ana02 = findViewById(R.id.color02);
        ana02.setText ("#" + f_strhex02);

        ImageView image02 = findViewById(R.id.image02);
        image02.setBackgroundColor(Color.parseColor("#" + f_strhex02));

    }

    public Color hex2Rgb(String colorStr) {
        colorStr = colorStr.replace("#", "");
        r = Integer.valueOf(colorStr.substring(0,2), 16);
        g = Integer.valueOf(colorStr.substring(2,4),16);
        b = Integer.valueOf(colorStr.substring(4,6), 16);
        Color color = Color.valueOf(r,g,b);
        return color;

        /*
        return new Color(
                Integer.valueOf(colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf(colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf(colorStr.substring( 5, 7 ), 16 ) );
        */
    }
}
