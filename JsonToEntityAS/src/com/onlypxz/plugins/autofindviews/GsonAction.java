package com.onlypxz.plugins.autofindviews;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.onlypxz.plugins.autofindviews.views.JsonToEntityV2;

public class GsonAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        JsonToEntityV2 frame= new JsonToEntityV2("Json To Entity Tools");
        frame.setVisible(true);
    }
}
