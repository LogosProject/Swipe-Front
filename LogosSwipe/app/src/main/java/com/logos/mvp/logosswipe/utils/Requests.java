package com.logos.mvp.logosswipe.utils;

/**
 * Created by Sylvain on 30/01/15.
 */
public class Requests {
    private static final String SERVER_URL="http://178.62.199.79:8080/swipe-back/";


    // ** GET **//
    public static String getProblemsUrl(){
        return SERVER_URL+"problems";
    }
    public static String getValuesProblemUrl(Long problemId){
        return getProblemsUrl()+"/"+problemId+"/values";
    }


    // ** POST **//
    public static String postProblemUrl(){
        return SERVER_URL+"problems";
    }

    public static String postValueProblemUrl(Long problemId){
        return postProblemUrl()+"/"+problemId+"/values";
    }
}
