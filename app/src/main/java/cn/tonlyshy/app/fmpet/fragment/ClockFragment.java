package cn.tonlyshy.app.fmpet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TabHost;
import android.widget.TimePicker;

import cn.tonlyshy.app.fmpet.R;
import cn.tonlyshy.app.fmpet.plugin.NotificationMonitor;


/**
 * Created by Joseph on 2017/5/16.
 */

public class ClockFragment extends DialogFragment implements View.OnClickListener {

    private Button btnConfirm;
    private Button btnCancel;
    private TimePicker timePicker;
    private DatePicker datePicker;
    private int selectedHour;
    private int selectedMinute;
    private TabHost tabHost;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.clock_fragment, container, false);
        btnConfirm = (Button) view.findViewById(R.id.btn_confirm_time);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel_time);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        tabHost = (TabHost) view.findViewById(R.id.tab_host);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("one").setIndicator("日期").setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("two").setIndicator("时间").setContent(R.id.tab2));
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int tabIndex = tabHost.getCurrentTab();

                //System.out.println(mTabHost.getTabContentView().getChildCount());
                if(tabIndex==1){
                    tabHost.getTabContentView().getChildAt(0).startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.left_out));
                    tabHost.getTabContentView().getChildAt(1).startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.right_in));
                }
                else{
                    tabHost.getTabContentView().getChildAt(0).startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.left_in));
                    tabHost.getTabContentView().getChildAt(1).startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.right_out));
                }
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_time:
                Log.d("button", "confirm time");
                selectedHour = timePicker.getCurrentHour();
                selectedMinute = timePicker.getCurrentMinute();
                Intent intent = new Intent(getActivity(), NotificationMonitor.class);
                intent.setAction(NotificationMonitor.ACTION_SET_ALARM);
                intent.putExtra("hour", selectedHour);
                intent.putExtra("minute", selectedMinute);
                intent.putExtra("day", datePicker.getDayOfMonth());
                intent.putExtra("month", datePicker.getMonth());
                intent.putExtra("year", datePicker.getYear());
                getActivity().startService(intent);
                break;
            case R.id.btn_cancel_time:
                Log.d("button", "cancel time");

                break;
            default:
                break;
        }
        dismiss();//退出fragment

    }


}
