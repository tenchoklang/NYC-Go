package com.android.tenchoklang.nycgo;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

;

public class StepsFragment extends Fragment implements View.OnClickListener {
    private TextView textView;
    private EventHandler eventHandler = new EventHandler();

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        textView = (TextView)findViewById(R.id.counter);
//        findViewById(R.id.set_back).setOnClickListener(this);
//        findViewById(R.id.start).setOnClickListener(this);
//        findViewById(R.id.stop).setOnClickListener(this);
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);

        textView = (TextView)rootView.findViewById(R.id.counter);
        rootView.findViewById(R.id.set_back).setOnClickListener(this);
        rootView.findViewById(R.id.start).setOnClickListener(this);
        rootView.findViewById(R.id.stop).setOnClickListener(this);


        return rootView;
    }

        @Override
    public void onResume() {
        super.onResume();
        com.android.tenchoklang.nycgo.StepCounterService.eventHandler = eventHandler;
        textView.setText(String.valueOf(com.android.tenchoklang.nycgo.StepCounterService.steps));
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.set_back) {
            com.android.tenchoklang.nycgo.StepCounterService.steps = 0;
            textView.setText("0");
        }
        if (v.getId() == R.id.start) {
            com.android.tenchoklang.nycgo.StepCounterService.eventHandler = eventHandler;
            getActivity().startService(new Intent(getContext(), com.android.tenchoklang.nycgo.StepCounterService.class));
        }
        if (v.getId() == R.id.stop) {
            getActivity().stopService(new Intent(getContext(), com.android.tenchoklang.nycgo.StepCounterService.class));
        }
    }


    private class EventHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            textView.setText(String.valueOf(msg.what));
        }
    }
}