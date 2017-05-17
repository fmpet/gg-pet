package cn.tonlyshy.app.fmpet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.tonlyshy.app.fmpet.R;
import cn.tonlyshy.app.fmpet.core.FloatViewManager;

/**
 * Created by Joseph on 2017/5/17.
 */

public class ModelChangeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.model_change_fragment,container,false);
        final EditText editText=(EditText)view.findViewById(R.id.new_name_edt);
        Button btnCancel=(Button) view.findViewById(R.id.btn_cancel_new_name);
        Button btnConfirm=(Button)view.findViewById(R.id.btn_confirm_new_name);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"取消",Toast.LENGTH_SHORT).show();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatViewManager floatViewManager=FloatViewManager.getInstance(getActivity());
                floatViewManager.setPetName(editText.getText().toString());
                Toast.makeText(getActivity(),"新昵称："+editText.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
