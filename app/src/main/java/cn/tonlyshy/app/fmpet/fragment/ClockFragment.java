package cn.tonlyshy.app.fmpet.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.tonlyshy.app.fmpet.R;

/**
 * Created by Joseph on 2017/5/16.
 */

public class ClockFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.clock_fragment, container, false);
        return view;
    }
}
