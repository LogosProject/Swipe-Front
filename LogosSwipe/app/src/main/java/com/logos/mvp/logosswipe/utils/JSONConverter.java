package com.logos.mvp.logosswipe.utils;

import com.logos.mvp.logosswipe.App;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

import greendao.Comment;
import greendao.CommentDao;
import greendao.Problem;
import greendao.ProblemDao;
import greendao.Solution;
import greendao.SolutionDao;
import greendao.SolutionScore;
import greendao.User;
import greendao.UserDao;
import greendao.Value;
import greendao.ValueDao;
import greendao.ValueSolutionScore;
import greendao.Versus;
import greendao.VersusDao;

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

    public static Versus versusConverter(JSONObject obj) throws JSONException{
        String id = obj.getString("id");
        ProblemDao problemDao = App.getInstance().getSession().getProblemDao();
        Problem problem = problemConverter(new JSONObject(obj.getString("problem")));
        problemDao.insertOrReplace(problem);
        SolutionDao solutionDao = App.getInstance().getSession().getSolutionDao();
        Solution solution1 = solutionConverter(new JSONObject(obj.getString("solution1")),problem.getId());
        Solution solution2 = solutionConverter(new JSONObject(obj.getString("solution2")),problem.getId());
        solutionDao.insertOrReplace(solution1);
        solutionDao.insertOrReplace(solution2);
        ValueDao valueDao = App.getSession().getValueDao();
        Value value = valueConverter(new JSONObject(obj.getString("value")),problem.getId());
        valueDao.insertOrReplace(value);
        return new Versus(Long.parseLong(id),solution1.getId(),solution2.getId(),value.getId());
    }

    public static User userConverter(JSONObject obj) throws JSONException {
        String id = obj.getString("id");
        String username = obj.getString("username");
        return new User(Long.parseLong(id),username);
    }
    public static Date dateConverter(JSONObject obj) throws JSONException{
        String millis = obj.getString("millis");
        return new Date(Long.parseLong(millis));
    }
    public static Comment commentConverter(JSONObject obj) throws JSONException {
        String id = obj.getString("id");
        String name = obj.getString("name");
        Date datetime = dateConverter(new JSONObject(obj.getString("dateTime")));
        /*try {
            datetime = ISO8601DateParser.parse(obj.getString("datetime"));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        String content = obj.getString("content");
        Versus versus = versusConverter(new JSONObject(obj.getString("versus")));
        /*User user = userConverter(new JSONObject(obj.getString("user")));
        if(user != null) {
            VersusDao versusDao = App.getInstance().getSession().getVersusDao();
            versusDao.insertOrReplace(versus);
            UserDao userDao = App.getInstance().getSession().getUserDao();
            userDao.insertOrReplace(user);
        }*/
        return new Comment(Long.parseLong(id), name, datetime,  content, -1L , versus.getId(),-1L);

    }

    public static ValueSolutionScore valueSolutionScoreConverter (JSONObject object,long problemId)throws JSONException {
        String id = object.getString("id");
        String score = object.getString("score");
        Value value = valueConverter(object.getJSONObject("value"), problemId);
        User user = userConverter(object.getJSONObject("user"));
        Solution solution = solutionConverter(object.getJSONObject("solution"),problemId);
        ValueDao valueDao = App.getSession().getValueDao();
        valueDao.insertOrReplace(value);
        App.getSession().getUserDao().insertOrReplace(user);
        App.getSession().getSolutionDao().insertOrReplace(solution);
        return new ValueSolutionScore(Long.parseLong(id), Double.parseDouble(score), value.getId(), user.getId(),solution.getId());
    }

    public static SolutionScore solutionScoreConverter(JSONObject object, long problemId)throws JSONException {
        String id = object.getString("id");
        String score = object.getString("score");
        User user = userConverter(object.getJSONObject("user"));
        Solution solution = solutionConverter(object.getJSONObject("solution"),problemId);

        App.getSession().getUserDao().insertOrReplace(user);
        App.getSession().getSolutionDao().insertOrReplace(solution);

        return new SolutionScore(Long.parseLong(id),Double.parseDouble(score),solution.getId(),user.getId());
    }
}
