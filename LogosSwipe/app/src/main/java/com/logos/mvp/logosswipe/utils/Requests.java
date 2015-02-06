package com.logos.mvp.logosswipe.utils;

import java.util.ArrayList;

import greendao.Solution;

/**
 * Created by Sylvain on 30/01/15.
 */
public class Requests {
    private static final String SERVER_URL="http://178.62.199.79:8080/swipe-back/";
    public static final String USER_ID="109";

    // ** GET **//
    public static String getProblemsUrl(){
        return SERVER_URL+"problems";
    }
    public static String getValuesProblemUrl(Long problemId){
        return getProblemsUrl()+"/"+problemId+"/values";
    }
    public static String getSolutionsProblemUrl(Long problemId){
        return getProblemsUrl()+"/"+problemId+"/solutions";
    }

    public static String getNextVersusProblem(long problemId){
        return getProblemsUrl()+"/"+problemId+"/versus/next/?userId="+USER_ID;
    }
    public static String getVersusComments(long versusId){
        return SERVER_URL+"versus/"+versusId+"/comments";
    }

    public static String getSolutionsScores(long problemId){
        return getProblemsUrl()+"/"+problemId+"/solutionscores/?userId="+USER_ID;
    }
    // ** POST **//
    public static String postProblemUrl(){
        return SERVER_URL+"problems";
    }

    public static String postCommentVersusUrl(long versusId)
    {
        return SERVER_URL+"versus/"+versusId+"/comments";
    }
    public static String postValueProblemUrl(Long problemId){
        return postProblemUrl()+"/"+problemId+"/values";
    }

    public static String postSolutionProblemUrl(Long problemId){
        return postProblemUrl()+"/"+problemId+"/solutions";
    }

    public static String postSolutionsSelectedUrl(long problemId, ArrayList<Solution> solutions){
        String ret= postProblemUrl()+"/"+problemId+"/solutions/select";
        ret+="/?userId="+USER_ID;
        for(int i=0;i<solutions.size();i++){
            ret+="&solutionsId="+solutions.get(i).getId();
        }
        return ret;
    }
    public static String postValueScoreUrl(Long valueId){
        return SERVER_URL+"values/"+valueId+"/valuescores";
    }

    public static String postVersusScoreUrl(Long versusId){
        return SERVER_URL+"versus/"+versusId+"/versusresponses";
    }

}
