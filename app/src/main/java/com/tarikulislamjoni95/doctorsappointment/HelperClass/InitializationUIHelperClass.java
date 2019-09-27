package com.tarikulislamjoni95.doctorsappointment.HelperClass;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class InitializationUIHelperClass
{
    private View view;

    public InitializationUIHelperClass(View view)
    {
        this.view=view;
    }

    public TextView[] setTextViews(int[] textViewsId)
    {
        TextView[] textViews=new TextView[textViewsId.length];
        for(int i=0; i<textViews.length; i++)
        {
            textViews[i]=view.findViewById(textViewsId[i]);
        }
        return textViews;
    }
    public EditText[] setEditTexts(int[] editTextsId)
    {
        EditText[] editTexts=new EditText[editTextsId.length];
        for(int i=0; i<editTextsId.length; i++)
        {
            editTexts[i]=view.findViewById(editTextsId[i]);
        }
        return editTexts;
    }
    public ImageView[] setImageViews(int[] imageViewsId) {
        ImageView[] imageViews=new ImageView[imageViewsId.length];
        for (int i=0; i<imageViewsId.length; i++)
        {
            imageViews[i]=view.findViewById(imageViewsId[i]);
        }
        return imageViews;
    }

    public CircleImageView[] setCircleImageViews(int[] circularImageViewsId) {
        CircleImageView[] circleImageViews=new CircleImageView[circularImageViewsId.length];
        for (int i=0; i<circularImageViewsId.length; i++)
        {
            circleImageViews[i]=view.findViewById(circularImageViewsId[i]);
        }
        return circleImageViews;
    }

    public Button[] setButtons(int[] buttonsId)
    {
        Button[] buttons=new Button[buttonsId.length];
        for(int i=0; i<buttonsId.length; i++)
        {
            buttons[i]=view.findViewById(buttonsId[i]);
        }
        return buttons;
    }

    public Spinner[] setSpinner(int[] spinnersId)
    {
        Spinner[] spinners=new Spinner[spinnersId.length];
        for(int i=0; i<spinnersId.length; i++)
        {
            spinners[i]=view.findViewById(spinnersId[i]);
        }
        return spinners;
    }

    public RadioGroup[] setRadioGroup(int[] radioGroupsId)
    {
        RadioGroup[] radioGroups=new RadioGroup[radioGroupsId.length];
        for(int i=0; i<radioGroups.length; i++)
        {
            radioGroups[i]=view.findViewById(radioGroupsId[i]);
        }
        return radioGroups;
    }
    public RadioButton[] setRadioButton(int[] radioButtonsId)
    {
        RadioButton[] radioButtons=new RadioButton[radioButtonsId.length];
        for(int i=0; i<radioButtons.length; i++)
        {
            radioButtons[i]=view.findViewById(radioButtonsId[i]);
        }
        return radioButtons;
    }

    public LinearLayout[] setLinearLayout(int[] linearLayoutId)
    {
        LinearLayout[] linearLayouts=new LinearLayout[linearLayoutId.length];
        for(int i=0; i<linearLayoutId.length; i++)
        {
            linearLayouts[i]=view.findViewById(linearLayoutId[i]);
        }
        return linearLayouts;
    }

}
