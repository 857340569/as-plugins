package com.onlypxz.plugins.autofindviews;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.onlypxz.plugins.autofindviews.entity.XmlElement;
import com.onlypxz.plugins.autofindviews.utils.ConfigFrame;
import com.onlypxz.plugins.autofindviews.utils.Utils;
import org.apache.http.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tracker on 2017/6/23.
 */
public class FindAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // 获取project
        Project project = e.getProject();
        // 获取选中内容
        final Editor mEditor = e.getData(PlatformDataKeys.EDITOR);
        if (null == mEditor) {
            return;
        }


        SelectionModel model = mEditor.getSelectionModel();
        String selectedText = model.getSelectedText();
        if (TextUtils.isEmpty(selectedText)) {
            // 未选中布局内容，显示dialog
            selectedText = Messages.showInputDialog(project, "layout（不需要输入R.layout.）：" , "未选中布局内容，请输入layout文件名", Messages.getInformationIcon());
            if (TextUtils.isEmpty(selectedText)) {
                Utils.showPopupBalloon(mEditor, "未输入layout文件名",1);
                return;
            }
        }
        // 获取布局文件，通过FilenameIndex.getFilesByName获取
        // GlobalSearchScope.allScope(project)搜索整个项目
        PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, selectedText + ".xml", GlobalSearchScope.allScope(project));
        if (psiFiles.length <= 0) {
            Utils.showPopupBalloon(mEditor, "未找到选中的布局文件",1);
            return;
        }
        XmlFile xmlFile = (XmlFile) psiFiles[0];

        PsiClass psiClass = Utils.getTargetClass(mEditor, xmlFile);

        ConfigFrame.AutoType autoType= ConfigFrame.AutoType.Field;
        if(!Utils.isExtendsActivityOrActivityCompat(project,psiClass))//不是在Activity中
        {
            autoType=Utils.isExtendsFragmentOrFragmentV4(project,psiClass)? ConfigFrame.AutoType.Fragment: ConfigFrame.AutoType.ViewHolder;
        }
        Utils.showPopupBalloon(mEditor, autoType.name(),1);
        ConfigFrame.autoType=autoType;
        ConfigFrame.createFrame(xmlFile);

    }

}
