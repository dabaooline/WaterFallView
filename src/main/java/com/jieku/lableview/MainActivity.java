package com.jieku.lableview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Lable> lables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WaterFallView viewById = (WaterFallView) findViewById(R.id.wfv);
        createData();
        viewById.setmLabels(lables);
        viewById.setOnItemClickListener(new WaterFallView.OnItemClickListener() {
            @Override
            public void onItemClick(int index, Lable label) {
                Toast.makeText(getApplicationContext(), label.getContents(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createData() {
        lables = new ArrayList<>();
        List<String> strings = Arrays.asList(new String[]{"你好", "拜拜", "再见", "seee", "baidu", "google"});
        for (int i = 0;i<strings.size();i++) {
            lables.add(new Lable(strings.get(i)));
        }
    }
}
