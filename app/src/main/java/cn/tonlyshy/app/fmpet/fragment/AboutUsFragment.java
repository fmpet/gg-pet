package cn.tonlyshy.app.fmpet.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.tonlyshy.app.fmpet.R;

/**
 * Created by Joseph on 2017/5/17.
 */

public class AboutUsFragment extends Fragment {

    private String versionName;
    private TextView textView;
    PackageInfo pi;
    PackageManager pm;
    private ImageView imageView1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_us_fragment, container, false);
        textView = (TextView) view.findViewById(R.id.version_textview);
        imageView1 = (ImageView) view.findViewById(R.id.afraid_1);
        pm = getActivity().getPackageManager();
        try {
             pi = pm.getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionName = pi.versionName;
        textView.setText("Version: " + versionName);
        Glide.with(getActivity()).load(R.drawable.afraid2).into(imageView1);
        return view;
    }
}
