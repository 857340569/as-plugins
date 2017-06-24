package com.onlypxz.plugins.autofindviews.utils;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.*;
import com.intellij.psi.search.EverythingGlobalScope;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.JBColor;
import com.onlypxz.plugins.autofindviews.entity.XmlElement;
import org.apache.http.util.TextUtils;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Utils {
    /**
     * 通过strings.xml获取的值
     */
    private static String StringValue;

    /**
     * 显示dialog
     *
     * @param editor editor
     * @param result 内容
     * @param time   显示时间，单位秒
     */
    public static void showPopupBalloon(final Editor editor, final String result, final int time) {
        ApplicationManager.getApplication().invokeLater(() -> {
            JBPopupFactory factory = JBPopupFactory.getInstance();
            factory.createHtmlTextBalloonBuilder(result, null, new JBColor(new Color(116, 214, 238), new Color(76, 112, 117)), null)
                    .setFadeoutTime(time * 1000)
                    .createBalloon()
                    .show(factory.guessBestPopupLocation(editor), Balloon.Position.below);
        });
    }

    /**
     * 获取所有设置id的xml 元素集合
     *
     * @param xmlFile
     */
    public static List<XmlElement> getXmlElementsForIds(XmlFile xmlFile) {
        return getXmlElementsForIds(xmlFile, null);
    }

    /**
     * 获取所有设置id的xml 元素集合
     *
     * @param xmlFile
     * @param xmlElements 存放所有的xml 元素集合
     */
    private static List<XmlElement> getXmlElementsForIds(XmlFile xmlFile, List<XmlElement> xmlElements) {
        if (xmlElements == null) {
            xmlElements = new ArrayList<XmlElement>();
        }
        final List<XmlElement> finalXmlElements = xmlElements;
        // 获取project
        final Project project = xmlFile.getProject();
        // 遍历xml文件的所有元素
        xmlFile.accept(new XmlRecursiveElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                super.visitElement(element);
                // 解析Xml标签
                if (element instanceof XmlTag) {
                    XmlTag tag = (XmlTag) element;
                    System.out.println("tag:"+tag.getName()+" "+tag.getValue());
                    // 获取Tag的名字（TextView）或者自定义或者是inclue
                    String fieldTypeStr = tag.getName();
                    if (fieldTypeStr.equalsIgnoreCase("include"))//include other layout.xml
                    {
                        // 获取布局
                        XmlAttribute layout = tag.getAttribute("layout");
                        String layoutName = Utils.getLayoutName(layout.getValue());
                        XmlFile includeFile = null;
                        PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, layoutName + ".xml", GlobalSearchScope.allScope(project));
                        if (psiFiles.length > 0) {
                            includeFile = (XmlFile) psiFiles[0];
                        }
                        if (includeFile != null) {
                            // 递归
                            getXmlElementsForIds(includeFile, finalXmlElements);
                        }
                        return;
                    } else if (fieldTypeStr.contains("."))//自定义
                    {
                        fieldTypeStr = fieldTypeStr.substring(fieldTypeStr.lastIndexOf(".") + 1);
                    }//处理后全部当作系统的View来处理
                    XmlAttribute viewIdAttribute = tag.getAttribute("android:id");
                    if (viewIdAttribute == null) return;
                    String idStr = viewIdAttribute.getValue();
                    if (!TextUtils.isEmpty(idStr) && idStr.contains("@+id/")) {
                        String fieldNameStr = idStr.substring(idStr.lastIndexOf("/") + 1);
                        XmlElement xmlElement = new XmlElement(fieldTypeStr, fieldNameStr);
                        finalXmlElements.add(xmlElement);
                    }
                }
            }
        });
        return finalXmlElements;
    }

    /**
     * 生成代码部分
     *
     * @param xmlElements
     * @param autoType
     * @return
     */
    public static String createFoundCode(List<XmlElement> xmlElements, ConfigFrame.AutoType autoType) {
        String viewCodes = "";
        StringBuilder viewFieldBuilder = new StringBuilder();
        StringBuilder viewInitBuilder = new StringBuilder();
        StringBuilder viewSetTextBuilder = new StringBuilder();
        for (XmlElement xmlElement:xmlElements)
        {
            String fieldType=xmlElement.getFieldType();
            String fieldName=xmlElement.getFieldName();
            switch (autoType) {
                case Field:
                    viewFieldBuilder.append("private "+fieldType+" "+fieldName+";\n");
                    viewInitBuilder.append("\t"+fieldName+" = findView(R.id."+fieldName+");\n");
                    break;

                case Fragment:
                    viewFieldBuilder.append("private "+fieldType+" "+fieldName+";\n");
                    viewInitBuilder.append("\t"+fieldName+" = findView(view,R.id."+fieldName+");\n");
                    break;
                case ViewHolder:
                    viewFieldBuilder.append(fieldType+" "+fieldName+" = CommonViewHolder.findView(convertView,R.id."+fieldName+");\n");
                    break;
            }
        }
        if(autoType!=ConfigFrame.AutoType.ViewHolder)
        {
            viewCodes=viewFieldBuilder.toString();
            viewCodes+="\nprivate void initViews("+(autoType==ConfigFrame.AutoType.Fragment?"View view":"")+"){\n"+viewInitBuilder.toString()
                    +"}";
        }else
        {
            viewCodes=viewFieldBuilder.toString();
        }
        return  viewCodes;
    }


    /**
     * 第一个字母大写
     *
     * @param key key
     * @return String
     */
    public static String firstToUpperCase(String key) {
        return key.substring(0, 1).toUpperCase(Locale.CHINA) + key.substring(1);
    }


    /**
     * layout.getValue()返回的值为@layout/layout_view
     *
     * @param layout layout
     * @return String
     */
    public static String getLayoutName(String layout) {
        if (layout == null || !layout.startsWith("@") || !layout.contains("/")) {
            return "";
        }

        // @layout layout_view
        String[] parts = layout.split("/");
        if (parts.length != 2) {
            return "";
        }
        // layout_view
        return parts[1];
    }

    /**
     * 根据当前文件获取对应的class文件
     *
     * @param editor editor
     * @param file   file
     * @return PsiClass
     */
    public static PsiClass getTargetClass(Editor editor, PsiFile file) {
        int offset = editor.getCaretModel().getOffset();
        PsiElement element = file.findElementAt(offset);
        if (element == null) {
            return null;
        } else {
            PsiClass target = PsiTreeUtil.getParentOfType(element, PsiClass.class);
            return target instanceof SyntheticElement ? null : target;
        }
    }

    /**
     * 判断mClass是不是继承activityClass或者activityCompatClass
     *
     * @param mProject mProject
     * @param mClass   mClass
     * @return boolean
     */
    public static boolean isExtendsActivityOrActivityCompat(Project mProject, PsiClass mClass) {
        // 根据类名查找类
        PsiClass activityClass = JavaPsiFacade.getInstance(mProject).findClass("android.app.Activity", new EverythingGlobalScope(mProject));
        PsiClass activityCompatClass = JavaPsiFacade.getInstance(mProject).findClass("android.support.v7.app.AppCompatActivity", new EverythingGlobalScope(mProject));
        return (activityClass != null && mClass.isInheritor(activityClass, true))
                || (activityCompatClass != null && mClass.isInheritor(activityCompatClass, true));
    }

    /**
     * 判断mClass是不是继承fragmentClass或者fragmentV4Class
     *
     * @param mProject mProject
     * @param mClass   mClass
     * @return boolean
     */
    public static boolean isExtendsFragmentOrFragmentV4(Project mProject, PsiClass mClass) {
        // 根据类名查找类
        PsiClass fragmentClass = JavaPsiFacade.getInstance(mProject).findClass("android.app.Fragment", new EverythingGlobalScope(mProject));
        PsiClass fragmentV4Class = JavaPsiFacade.getInstance(mProject).findClass("android.support.v4.app.Fragment", new EverythingGlobalScope(mProject));
        return (fragmentClass != null && mClass.isInheritor(fragmentClass, true))
                || (fragmentV4Class != null && mClass.isInheritor(fragmentV4Class, true));
    }


    /**
     * 根据方法名获取方法
     *
     * @param mClass     mClass
     * @param methodName methodName
     * @return PsiMethod
     */
    static PsiMethod getPsiMethodByName(PsiClass mClass, String methodName) {
        for (PsiMethod psiMethod : mClass.getMethods()) {
            if (psiMethod.getName().equals(methodName)) {
                return psiMethod;
            }
        }
        return null;
    }


}