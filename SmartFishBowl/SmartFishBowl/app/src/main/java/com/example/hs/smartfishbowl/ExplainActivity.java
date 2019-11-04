package com.example.hs.smartfishbowl;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class ExplainActivity extends AppCompatActivity {

    ArrayList veloList;
    ArrayList locList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);

        TextView explainA = (TextView)findViewById(R.id.explainA);
        TextView explainB = (TextView)findViewById(R.id.explainB);
        TextView explainC = (TextView)findViewById(R.id.explainC);

        Intent intent = getIntent();
        veloList = intent.getParcelableArrayListExtra("veloList");
        locList = intent.getParcelableArrayListExtra("locList");

        for(int i = 0; i < veloList.size(); i++)
        {
            if(veloList.get(i).equals("fast"))
            {
                explainA.setText("이상증세 상세 설명");
                explainB.setText("[기생충성 질병]\n기생충이 어류의 아가미, 표피, 지느러미, 내장에 침입해 기생하면서 어체의 생리 및 면역학적 균형을 교란시켜 질병으로 발현하게 되며, 기생충의 종류에 따라 질병의 종류가 구분됩니다. 기생충성 질병은 오염된 환경에서 많이 발생하므로 사육장소의 청결유지 및 수용밀도를 낮추는 방식으로 관리해야 합니다.");
            }
            else if(veloList.get(i).equals("slow"))
            {
                explainA.setText("이상증세 상세 설명");
                explainB.setText("[솔방울병]\n추위가 풀리는 봄에 각종 담수어류에서 발생하는 세균성 질병으로, 이 병에 감염되면 처음에는 특별한 증상이 없이 활동성이 떨어지고 무리에서 떨어져나와 지냅니다. 만성이 되면 피부와 근육에 궤양이 나타나고 입 속, 지느러미, 아가미뚜껑, 항문 주위 등이 붉어지며 지느러미가 떨어져나가는 증상 등을 볼 수 있습니다.\n" +
                        "치료를 위해서는 항생제를 사용하면 효과를 볼 수 있습니다. 또한, 수온을 27∼29℃로 올려주는 것도 좋고 환경 스트레스를 없애주는 것도 중요합니다.\n\n[세균성 질병]\n세균성 질병은 세균의 감염에 의해 발생하는 질병으로 광학 현미경으로 관찰할 수 있다고 합니다. 세균성 질병은 발생빈도가 높고 전염성이 강하고 일단 감염이 되면 누적폐사량이 많아지기 때문에 경제적 손실이 큰 질병에 속합니다.\n\n[바이러스성 질병]\n입자 크기가 20~300nm로서 전자현미경으로만 볼 수 있는 극히 작은 미생물로 인한 질병입니다. 바이러스성 질병은 어체에 의한 수평감염뿐 아니라 수정란에 의해서 수직적으로 감염될 수 있습니다. 한번 발생되면 일시에 대량 폐사를 일으키는 경우가 많고 약제에 의한 치료가 불가해서 예방차원의 방역조치가 필요하다고 합니다.");
            }

            if(locList.get(i).equals("top"))
            {
                explainA.setText("이상증세 상세 설명");
                explainC.setText("\n[부레병]\n부레병은 주로 금붕어에게 나타나는 질병으로 거꾸로 뒤집힌 채 위로 떠오르거나 아래로 가라앉는 등의 특징을 보입니다. 부레병의 원인으로는 장내 기생충이나 과도한 먹이를 먹음으로 인한 변비가 질병의 원인이 될 수 있습니다. 부레병 치료로는 금붕어에게 완두콩을 삶아 먹이로 주면 효과를 볼 수 있으며, 부레의 일부를 제거하는 수술 치료가 있습니다.\n");
            }
        }

        if(veloList.get(0).equals("veloNormal") && veloList.get(1).equals("veloNormal") && locList.get(0).equals("locNormal") && locList.get(1).equals("locNormal"))
        {
            explainA.setText("금붕어를 키우기 위해 주의할 점");
            explainB.setText("1.금붕어에게 적합한 물\n금붕어 사용용수로 수돗물을 그대로 사용하기에 적합하지 않습니다. 수돗물을 이용할 때는 염소를 제거해야 합니다. 염소 제거의 방법으로는 미리 물을 받아둔 뒤 1~2일간 방치해두는 방법입니다. 또 하나의 방법은 하이포를 사용하여 물을 중화시키는 방법입니다. 시판하는 하이포를 수돗물 1리터당 1정도의 비율로 넣어 사용합니다.\n2. 먹이의 양\n하루에 한 번 1분 안에 먹을 수 있는 양으로 주시면 됩니다.");
            explainC.setText("");
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
