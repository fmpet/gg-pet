package cn.tonlyshy.app.fmpet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import cn.tonlyshy.app.fmpet.MyFloatService;
import cn.tonlyshy.app.fmpet.R;
import cn.tonlyshy.app.fmpet.core.FloatViewManager;

/**
 * Created by Liaowm5 on 2017/5/16.
 */

public class MainFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        Switch switchShow = (Switch) view.findViewById(R.id.switch_show);
        switchShow.setOnCheckedChangeListener(this);
        TextView txv=(TextView)view.findViewById(R.id.pet_name_textview);
        FloatViewManager floatViewManager=FloatViewManager.getInstance(getActivity());
        String petName=floatViewManager.getPetName();
        txv.setText(petName);
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_show:
                if (isChecked) {
                    Intent intent=new Intent(getActivity(), MyFloatService.class);
                    getActivity().startService(intent);
                    Log.d("TAG", "started");
                } else {
                    getActivity().stopService(new Intent(getActivity(), MyFloatService.class));
                }
                break;
            default:
                break;
        }
    }
}
