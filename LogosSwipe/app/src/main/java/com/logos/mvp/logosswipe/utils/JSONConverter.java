package com.logos.mvp.logosswipe.utils;

import org.json.JSONException;
import org.json.JSONObject;

import greendao.Problem;
import greendao.Solution;
import greendao.Value;

/**
 * Created by Sylvain on 30/01/15.
 */
public class JSONConverter {
    public static Problem problemConverter(JSONObject obj) throws JSONException{
        String id = obj.getString("id");
        String name = obj.getString("name");
        String description = obj.getString("description");
        return new Problem(Long.parseLong(id),name,description);
    }
    public static Value valueConverter(JSONObject obj,Long problemId) throws JSONException{
        String id = obj.getString("id");
        String name = obj.getString("name");
        String description = obj.getString("description");

        return new Value(Long.parseLong(id),name,description,problemId);
    }
    public static Solution solutionConverter(JSONObject obj,Long problemId) throws JSONException{
        String id = obj.getString("id");
        String name = obj.getString("name");
        String description = obj.getString("description");
        return new Solution(Long.parseLong(id),name,description,problemId);
    }
}
