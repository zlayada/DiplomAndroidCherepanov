package com.netology.diplomandroidcherepanov.Activity;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.netology.diplomandroidcherepanov.R;

public class PinActivity extends AppCompatActivity {

    // Клавиатура ввода пин-кода
    private Button mKey1_btn;
    private Button mKey2_btn;
    private Button mKey3_btn;
    private Button mKey4_btn;
    private Button mKey5_btn;
    private Button mKey6_btn;
    private Button mKey7_btn;
    private Button mKey8_btn;
    private Button mKey9_btn;
    private Button mKey0_btn;
    private Button mKeyClear_btn;

    private String pinCod = "";     // Переменная для записи введенного пин-кода
    private int counterAttempt = 0; // Переменная счетчика попыток ввода пин-кода
    private int limit_inter_pin_code = 3; // Переменная лимита попыток ввода пин-кода (три попытки)

    // Поля визуализации введения пин-кода
    private View mOnePin;
    private View mTwoPin;
    private View mThreePin;
    private View mFourPin;

    private SharedPreferences pin_code_shared_preferences; // Буфер для хранения пин-кода



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        initView();
        loadNewPinCode();

    }

    // Инициализируем элементы экрана
    private void initView() {

        mKey1_btn = findViewById(R.id.key1_btn);
        mKey2_btn = findViewById(R.id.key2_btn);
        mKey3_btn = findViewById(R.id.key3_btn);
        mKey4_btn = findViewById(R.id.key4_btn);
        mKey5_btn = findViewById(R.id.key5_btn);
        mKey6_btn = findViewById(R.id.key6_btn);
        mKey7_btn = findViewById(R.id.key7_btn);
        mKey8_btn = findViewById(R.id.key8_btn);
        mKey9_btn = findViewById(R.id.key9_btn);
        mKey0_btn = findViewById(R.id.key0_btn);
        mKeyClear_btn = findViewById(R.id.keyClear_btn);

        mOnePin = findViewById(R.id.onePin);
        mTwoPin = findViewById(R.id.twoPin);
        mThreePin = findViewById(R.id.threePin);
        mFourPin = findViewById(R.id.fourPin);
    }


    // ******************************
    // Обрабатываем нажатия на кнопки
    // ******************************

    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.key0_btn:

                pinCod = pinCod + getString(R.string.tPinKeyText0);
                pinMarkerFieldRight();
                break;

            case R.id.key1_btn:
                pinCod = pinCod + getString(R.string.tPinKeyText1);
                pinMarkerFieldRight();
                break;

            case R.id.key2_btn:
                pinCod = pinCod + getString(R.string.tPinKeyText2);
                pinMarkerFieldRight();
                break;

            case R.id.key3_btn:
                pinCod = pinCod + getString(R.string.tPinKeyText3);
                pinMarkerFieldRight();
                break;

            case R.id.key4_btn:

                pinCod = pinCod + getString(R.string.tPinKeyText4);
                pinMarkerFieldRight();
                break;

            case R.id.key5_btn:
                pinCod = pinCod + getString(R.string.tPinKeyText5);
                pinMarkerFieldRight();
                break;

            case R.id.key6_btn:
                pinCod = pinCod + getString(R.string.tPinKeyText6);
                pinMarkerFieldRight();
                break;

            case R.id.key7_btn:
                pinCod = pinCod + getString(R.string.tPinKeyText7);
                pinMarkerFieldRight();
                break;

            case R.id.key8_btn:
                pinCod = pinCod + getString(R.string.tPinKeyText8);
                pinMarkerFieldRight();
                break;

            case R.id.key9_btn:
                pinCod = pinCod + getString(R.string.tPinKeyText9);
                pinMarkerFieldRight();
                break;

            case R.id.keyClear_btn:

                if (pinCod.length() != 0) {
                    pinMarkerFieldLeft();
                    pinCod = pinCod.substring(0, pinCod.length() - 1);
                }
                break;

            default:
                break;
        }

    }

    // *************************************************
    // Методы работы с визуализацией поля ввода пин-кода
    // *************************************************

    // Меняем цвет точек визуализаии ввода пин-кода вправо
    public void pinMarkerFieldRight() {

        if (pinCod.length() == 1) {
            mOnePin.setBackgroundResource(R.drawable.background_pin_enter);
        } else if (pinCod.length() == 2) {
            mTwoPin.setBackgroundResource(R.drawable.background_pin_enter);
        } else if (pinCod.length() == 3) {
            mThreePin.setBackgroundResource(R.drawable.background_pin_enter);
        } else if (pinCod.length() == 4) {
            mFourPin.setBackgroundResource(R.drawable.background_pin_enter);
            verificationPinCod();
        }
    }


    // Меняем цвет точек визуализаии ввода пин-кода при удалении влево
    public void pinMarkerFieldLeft() {

        if (pinCod.length() != 0) {

            if (pinCod.length() == 3) {
                mThreePin.setBackgroundResource(R.drawable.background_pin);
            } else if (pinCod.length() == 2) {
                mTwoPin.setBackgroundResource(R.drawable.background_pin);
            } else if (pinCod.length() == 1) {
                mOnePin.setBackgroundResource(R.drawable.background_pin);
            }
        }
    }

    // Меняем цвет точек визуализаии ввода пин-кода на исходный
    public void pinMarkerFieldClear() {

        mFourPin.setBackgroundResource(R.drawable.background_pin);
        mThreePin.setBackgroundResource(R.drawable.background_pin);
        mTwoPin.setBackgroundResource(R.drawable.background_pin);
        mOnePin.setBackgroundResource(R.drawable.background_pin);

    }

    // *************************
    // Методы работы с пин-кодом
    // *************************

    // Проверка кол. попыток ввода, совпадения пин-кода и переходы на другие активити
    public void verificationPinCod() {

        pin_code_shared_preferences = getSharedPreferences("pin", MODE_PRIVATE);
        String savedPinCod = pin_code_shared_preferences.getString("pin", "");

        // Проверка совпадения пин-кода
        if (savedPinCod.equals(pinCod)) {

            Toast.makeText(this, R.string.tPinTextInter, Toast.LENGTH_SHORT).show();

            // Переход на экран заметок
            Intent intentListActivity = new Intent(this, NotesListActivity.class);
            startActivity(intentListActivity);

        } else {

            Toast.makeText(this, R.string.tToastErrorPin, Toast.LENGTH_SHORT).show();
        }

        pinMarkerFieldClear();
        pinCod ="";
        counterAttempt +=1;         // Считаем попытки ввода пин-кода

        // Проверка лимита попыток ввода пин-кода
        if (counterAttempt > limit_inter_pin_code)  {

            Toast.makeText(this, R.string.tToastErrorAttemptPin, Toast.LENGTH_SHORT).show();
            this.finish(); // Закрываем приложение

        }

    }

    public void loadNewPinCode () {

        pin_code_shared_preferences = getSharedPreferences("pin", MODE_PRIVATE);
        String savedPinCod = pin_code_shared_preferences.getString("pin", "");

        // Если пин-код не вводился, то переход на активити ввода пин-кода
        if (savedPinCod.equals("")) {

            Intent intentSettings = new Intent(this, SettingsActivityPin.class);
            startActivity(intentSettings);

        }

    }
}
