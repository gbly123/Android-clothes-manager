package com.example.redbook.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class Weather {
    @JSONField
    public String wendu;
    @JSONField
    public String ganmao;

    public Weather(String result) {
        try {
            JSONObject jsonObject = JSON.parseObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            if (data != null) {
                this.wendu = data.getString("wendu");
                this.ganmao = data.getString("ganmao");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
