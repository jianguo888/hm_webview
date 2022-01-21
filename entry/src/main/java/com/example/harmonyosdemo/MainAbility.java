package com.example.harmonyosdemo;

import com.example.harmonyosdemo.slice.MainAbilitySlice;
import com.example.harmonyosdemo.slice.SecondAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        //默认显示
        super.setMainRoute(MainAbilitySlice.class.getName());
        //配置路由规则显示
        addActionRoute( "action.second", SecondAbilitySlice.class.getName());
    }
}
