package com.arteriatech.ss.msec.iscan.v4.utils.annotationutils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AnnotationUtils<T> implements Serializable {
    /**
     * Getting annotated fields/direct filed names
     * @param classReference this will gives all fields declared in class
     */
    public static List<String> getAllFields(Class classReference){
        List<String>keyArrayList = new ArrayList<>();
        Field[] fields = classReference.getDeclaredFields();
        for (Field field:fields) {
            if (field.getName() != null) {
                keyArrayList.add(field.getName());
            }
        }
        return keyArrayList;
    }

    /**
     * Getting annotated fields/direct filed names
     * @param classReference this will gives class reference to annotated fields
     */
    public static List<String> getAnnotationFields(Class classReference){
        List<String>keyArrayList = new ArrayList<>();
        Field[] fields = classReference.getDeclaredFields();
        for (Field field:fields) {
           Annotation[] annotations = field.getDeclaredAnnotations();
           if (annotations.length>0){
               keyArrayList.add(field.getName());
           }
        }
        return keyArrayList;
    }
    /**
     * Getting annotated fields/direct filed names
     * @param classReference this will gives class reference to annotated fields includes the presence of desired annotation class
     */
    public static List<String> getAnnotationFields(Class classReference,Class<? extends Annotation> annotationClass){
        boolean isNavigationProperty;
        List<String>keyArrayList = new ArrayList<>();
        Field[] fields = classReference.getDeclaredFields();
        for (Field field:fields) {
            isNavigationProperty = field.isAnnotationPresent(annotationClass);
            if (isNavigationProperty) {
                Annotation[] annotations = field.getDeclaredAnnotations();
                if (annotations.length>0){
                    keyArrayList.add(field.getName());
                }
            }
        }
        return keyArrayList;
    }
    /**
     * Getting annotated fields/direct filed names
     * @param t this will gives class reference to annotated field key
     *          ODatakey(key = " companyName ") This method will return the keyname as companyName.
     */
    @SuppressWarnings("all")
    public List<T> getAnnotationFieldKeys(T t){
        List<T>list= new ArrayList<>();
        try {
            t = (T) t.getClass().newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field:fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation:annotations) {
                ODataKey oDataKey = (ODataKey) annotation;
                list.add((T) oDataKey.key());
                System.out.println(oDataKey.key());
            }
        }
        return list;
    }

    /**
     * This method will return Annotated Field and needfully written for ETAGs.
     * @param classReference dynamic class reference
     * @return returns the Annotated field
     */
    public static Field getAnnotationETagField(Class classReference){
        Field[] fields = classReference.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ODataETag.class)) {
                return field;
            }
        }
        return null;
    }
}
