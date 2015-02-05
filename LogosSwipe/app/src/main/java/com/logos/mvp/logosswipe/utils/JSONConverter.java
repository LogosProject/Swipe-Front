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
import greendao.User;
import greendao.UserDao;
import greendao.Value;
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
        return new Versus(Long.parseLong(id),solution1.getId(),solution2.getId());
    }

    public static User userConverter(JSONObject obj) throws JSONException {
        String id = obj.getString("id");
        String username = obj.getString("username");
        return new User(Long.parseLong(id),username);
    }

    public static Comment commentConverter(JSONObject obj) throws JSONException {
        String id = obj.getString("id");
        String name = obj.getString("name");
        Date datetime = null;
        try {
            datetime = ISO8601DateParser.parse(obj.getString("datetime"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String content = obj.getString("content");
        Versus versus = versusConverter(new JSONObject(obj.getString("versus")));
        User user = userConverter(new JSONObject(obj.getString("user")));
        VersusDao versusDao = App.getInstance().getSession().getVersusDao();
        versusDao.insertOrReplace(versus);
        UserDao userDao = App.getInstance().getSession().getUserDao();
        userDao.insertOrReplace(user);
        return new Comment(Long.parseLong(id), name, datetime,  content, -1L , versus.getId(), user.getId());

    }
}
