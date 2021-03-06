package com.ws.mesh.incores2.view.control;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.lang.reflect.Field;

/**
 * Created by We-Smart on 2017/7/27.
 */

public class CustomTimePicker extends TimePicker {


    public CustomTimePicker(Context context) {
        super(context);
        init();
    }

    public CustomTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init()
    {
        getNumberPicker(this);
    }


    private void getNumberPicker(TimePicker timePicker)
    {
        try
        {
            Class<?> clazz = Class.forName("com.android.internal.R$id");
            Field fieldHour = clazz.getField("hour");
            fieldHour.setAccessible(true);
            int hourId = fieldHour.getInt(null);
            NumberPicker hourNumberPicker = timePicker.findViewById(hourId);
            setDividerColor(hourNumberPicker);
            set_numberpicker_text_colour(hourNumberPicker);

            Field fieldminute = clazz.getField("minute");
            fieldminute.setAccessible(true);
            int minuteId = fieldminute.getInt(null);
            NumberPicker minuteNumberPicker = timePicker.findViewById(minuteId);
            setDividerColor(minuteNumberPicker);
            set_numberpicker_text_colour(minuteNumberPicker);

            Field fieldampm = clazz.getField("amPm");
            fieldminute.setAccessible(true);
            int ampmId = fieldampm.getInt(null);
            NumberPicker ampmNumberPicker = timePicker.findViewById(ampmId);
            setDividerColor(ampmNumberPicker);
            set_numberpicker_text_colour(ampmNumberPicker);

            //更改冒号颜色
            Field fieldDivider=clazz.getField("divider");
            fieldDivider.setAccessible(true);
            int dividerId=fieldDivider.getInt(null);
            TextView textView= timePicker.findViewById(dividerId);
            textView.setTextColor(Color.parseColor("#000000"));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void setDividerColor(NumberPicker picker)
    {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields)
        {
            Log.v("setDividerColor", "pf:" + pf.getName() + " type :" + pf.getGenericType());
            if (pf.getName().equals("mSelectionDivider"))//能找到这个域 （分割线视图)
            {
                Log.v("setDividerColor", "find......mSelectionDivider");
                pf.setAccessible(true);
                try
                {
                    ColorDrawable colorDrawable = new ColorDrawable();
                    colorDrawable.setColor(Color.parseColor("#c3c3c3"));
                    pf.set(picker, colorDrawable);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            if (pf.getName().equals("mSelectionDividerHeight"))//找不到这个私有域，（分割线的厚度）
            {
                Log.v("PowerSet", "find......mSelectionDividerHeight.");
                pf.setAccessible(true);
                try {
                    int result = 2;
                    pf.set(picker, result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void set_numberpicker_text_colour(NumberPicker number_picker){
        final int count = number_picker.getChildCount();
        final int color = Color.parseColor("#000000");

        for(int i = 0; i < count; i++){
            View child = number_picker.getChildAt(i);

            try{
                Field wheelpaint_field = number_picker.getClass().getDeclaredField("mSelectorWheelPaint");
                wheelpaint_field.setAccessible(true);

                ((Paint)wheelpaint_field.get(number_picker)).setColor(color);
                ((EditText)child).setTextColor(color);
                number_picker.invalidate();
            }
            catch(NoSuchFieldException e){
                Log.w("NoSuchField", e);
            }
            catch(IllegalAccessException e){
                Log.w("IllegalAccess", e);
            }
            catch(IllegalArgumentException e){
                Log.w("IllegalArgument", e);
            }
        }
    }
}
