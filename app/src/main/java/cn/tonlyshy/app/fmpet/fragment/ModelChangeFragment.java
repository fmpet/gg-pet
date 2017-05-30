package cn.tonlyshy.app.fmpet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import cn.tonlyshy.app.fmpet.R;
import cn.tonlyshy.app.fmpet.core.FloatViewManager;

/**
 * Created by Joseph on 2017/5/17.
 */

public class ModelChangeFragment extends Fragment {
    Switch switchApperance1;
    Switch switchApperance2;
    Switch switchApperance3;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.model_change_fragment,container,false);
        switchApperance1=(Switch)view.findViewById(R.id.switch_model_apperance1);
        switchApperance2=(Switch)view.findViewById(R.id.switch_model_apperance2);
        switchApperance3=(Switch)view.findViewById(R.id.switch_model_apperance3);

        FloatViewManager floatViewManager=FloatViewManager.getInstance(getContext());
        int apperanceId=floatViewManager.getApperance();

        switch (apperanceId){
            case 0:
                switchApperance1.setChecked(true);
                switchApperance2.setChecked(false);
                switchApperance3.setChecked(false);
                break;
            case 1:
                switchApperance1.setChecked(false);
                switchApperance2.setChecked(true);
                switchApperance3.setChecked(false);
                break;
            case 2:
                switchApperance1.setChecked(false);
                switchApperance2.setChecked(false);
                switchApperance3.setChecked(true);
                break;
        }

        switchApperance1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    FloatViewManager floatViewManager=FloatViewManager.getInstance(getContext());
                    floatViewManager.setPetApperance(0);
                    switchApperance2.setChecked(false);
                    switchApperance3.setChecked(false);
                }
            }
        });

        switchApperance2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    FloatViewManager floatViewManager=FloatViewManager.getInstance(getContext());
                    floatViewManager.setPetApperance(1);
                    switchApperance1.setChecked(false);
                    switchApperance3.setChecked(false);
                }
            }
        });

        switchApperance3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    FloatViewManager floatViewManager=FloatViewManager.getInstance(getContext());
                    floatViewManager.setPetApperance(2);
                    switchApperance1.setChecked(false);
                    switchApperance2.setChecked(false);
                }
            }
        });

        return view;
    }
}
