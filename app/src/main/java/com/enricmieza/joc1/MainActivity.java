package com.enricmieza.joc1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorMgr;
    Sensor sensor;
    ImageView img;

    float velocitat = 2.0f;
    float iniciX, iniciY;

    int statusBar, width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // La bola
        img = (ImageView) findViewById(R.id.imageView);

        // Obtenim les dimensions de la pantalla
        DisplayMetrics display = this.getBaseContext().getResources().getDisplayMetrics();
        width = display.widthPixels;
        height = display.heightPixels;


        // Mida de l'statusBar per calcular l'alçada de l'aplicació
        statusBar = getResources().getDimensionPixelSize(getResources().getIdentifier("status_bar_height", "dimen", "android"));

        // Inicialitzem a 0 les variables per controlar les pulsacions tàctils
        iniciX = iniciY = 0;

        // Inicialitzem el sensor de l'acceleròmetre
        sensorMgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMgr.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorMgr.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorMgr.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            moveBall(sensorEvent.values[1], sensorEvent.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor s, int i) {

    }

    private void moveBall(float x, float y) {

        float novaPosicioX = img.getX() + x * velocitat;
        // Coordenada x

        // Si el moviment és cap a la dreta
        if (x > 0) {

            // Comprovem que no surti de les dimensions de la pantalla en assignar la nova posició
            if (novaPosicioX + img.getWidth() <= width) {
                img.setX(novaPosicioX);

            }
            // Si en surt, establim la posició màxima en horitzontal perquè es pugui veure la imatge.
            else img.setX(width - img.getWidth());
        }
        // Fem el mateix pel moviment cap a l'esquerra
        else {
            if (novaPosicioX >= 0) {
                img.setX(novaPosicioX);

            } else img.setX(0);

        }


        // Coordenada y

        float novaPosicioY = img.getY() + y * velocitat;

        // El concepte és el mateix que a la X però hem de tenir en compte la barra d'estat
        if (y > 0)
            if (novaPosicioY + img.getHeight() + statusBar <= height) {
                img.setY(novaPosicioY);

            } else img.setY(height - img.getHeight() - statusBar);
        else {
            if (novaPosicioY >= 0) {
                img.setY(novaPosicioY);

            } else img.setY(0);

        }


    }

}
