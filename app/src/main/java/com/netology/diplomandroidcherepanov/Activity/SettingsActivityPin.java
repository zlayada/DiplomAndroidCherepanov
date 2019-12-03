package com.netology.diplomandroidcherepanov.Activity;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.netology.diplomandroidcherepanov.R;

public class SettingsActivityPin extends AppCompatActivity {

    private Button mBtn_setting_pin_cod;        // Кнопка ввода пин-кода
    private ImageButton mBtn_vision_pin_cod;    // Кнопка изменения видимости пин-кода
    private EditText mField_inter_pin_cod;      // Поле ввода пин-кода
    private byte status_vision = 0;             // Статус видимости вводимого пин-кода "0" по умолчанию "не видно"

    SharedPreferences save_pin_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_pin_cod);

        initViev();
        savePinCode();

    }

    private void initViev() {

        mBtn_setting_pin_cod = findViewById(R.id.btn_setting_pin_cod);
        mBtn_vision_pin_cod = findViewById(R.id.btn_vision_pin_cod);
        mField_inter_pin_cod = findViewById(R.id.field_inter_pin_cod);
    }


    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_vision_pin_cod:

                if (status_vision == 0) {

                    mField_inter_pin_cod.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);             // Изменена клавиатура
                    mField_inter_pin_cod.setSelection(mField_inter_pin_cod.getText().length());            // Курсор поставлен в конце строки
                    mBtn_vision_pin_cod.setImageResource(R.drawable.im_vision);                            // Изменена иконка кнопки видимости
                    status_vision = 1;

                } else {

                    mField_inter_pin_cod.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);// Изменена клавиатураTYPE_CLASS_NUMBER
                    mField_inter_pin_cod.setSelection(mField_inter_pin_cod.getText().length());             // Курсор поставлен вконце строки
                    mBtn_vision_pin_cod.setImageResource(R.drawable.im_no_vision);                          // Изменена иконка кнопки видимост
                    status_vision = 0;
                }

                // TODO Убрать тост после отладки
                Toast.makeText(this, String.valueOf(status_vision), Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_setting_pin_cod:

                if (mField_inter_pin_cod.length() == 4) {
                    Toast.makeText(this, R.string.tPinTextSaved, Toast.LENGTH_SHORT).show();
                    savePinCode();
                    Intent intentPinActivity = new Intent(this, PinActivity.class);
                    startActivity(intentPinActivity);

                } else {

                    Toast.makeText(this, R.string.tPinTextSavedError, Toast.LENGTH_SHORT).show();
                }

                break;

            default:
                break;
        }
    }


    private void savePinCode() {

        save_pin_code = getSharedPreferences("pin", Context.MODE_PRIVATE);  // Создали объект настроек - пин-код
        SharedPreferences.Editor editor = save_pin_code.edit();                    // Извлекли редактор
        editor.putString("pin", mField_inter_pin_cod.getText().toString());         // Записали новый пин-код
        editor.apply();
    }

}

