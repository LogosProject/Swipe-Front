package greendao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.Solution;
import greendao.Problem;
import greendao.Versus;
import greendao.SolutionScore;
import greendao.VersusResponse;
import greendao.Comment;
import greendao.User;
import greendao.ValueSolutionScore;
import greendao.Value;
import greendao.ValueScore;

import greendao.SolutionDao;
import greendao.ProblemDao;
import greendao.VersusDao;
import greendao.SolutionScoreDao;
import greendao.VersusResponseDao;
import greendao.CommentDao;
import greendao.UserDao;
import greendao.ValueSolutionScoreDao;
import greendao.ValueDao;
import greendao.ValueScoreDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig solutionDaoConfig;
    private final DaoConfig problemDaoConfig;
    private final DaoConfig versusDaoConfig;
    private final DaoConfig solutionScoreDaoConfig;
    private final DaoConfig versusResponseDaoConfig;
    private final DaoConfig commentDaoConfig;
    private final DaoConfig userDaoConfig;
    private final DaoConfig valueSolutionScoreDaoConfig;
    private final DaoConfig valueDaoConfig;
    private final DaoConfig valueScoreDaoConfig;

    private final SolutionDao solutionDao;
    private final ProblemDao problemDao;
    private final VersusDao versusDao;
    private final SolutionScoreDao solutionScoreDao;
    private final VersusResponseDao versusResponseDao;
    private final CommentDao commentDao;
    private final UserDao userDao;
    private final ValueSolutionScoreDao valueSolutionScoreDao;
    private final ValueDao valueDao;
    private final ValueScoreDao valueScoreDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        solutionDaoConfig = daoConfigMap.get(SolutionDao.class).clone();
        solutionDaoConfig.initIdentityScope(type);

        problemDaoConfig = daoConfigMap.get(ProblemDao.class).clone();
        problemDaoConfig.initIdentityScope(type);

        versusDaoConfig = daoConfigMap.get(VersusDao.class).clone();
        versusDaoConfig.initIdentityScope(type);

        solutionScoreDaoConfig = daoConfigMap.get(SolutionScoreDao.class).clone();
        solutionScoreDaoConfig.initIdentityScope(type);

        versusResponseDaoConfig = daoConfigMap.get(VersusResponseDao.class).clone();
        versusResponseDaoConfig.initIdentityScope(type);

        commentDaoConfig = daoConfigMap.get(CommentDao.class).clone();
        commentDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        valueSolutionScoreDaoConfig = daoConfigMap.get(ValueSolutionScoreDao.class).clone();
        valueSolutionScoreDaoConfig.initIdentityScope(type);

        valueDaoConfig = daoConfigMap.get(ValueDao.class).clone();
        valueDaoConfig.initIdentityScope(type);

        valueScoreDaoConfig = daoConfigMap.get(ValueScoreDao.class).clone();
        valueScoreDaoConfig.initIdentityScope(type);

        solutionDao = new SolutionDao(solutionDaoConfig, this);
        problemDao = new ProblemDao(problemDaoConfig, this);
        versusDao = new VersusDao(versusDaoConfig, this);
        solutionScoreDao = new SolutionScoreDao(solutionScoreDaoConfig, this);
        versusResponseDao = new VersusResponseDao(versusResponseDaoConfig, this);
        commentDao = new CommentDao(commentDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);
        valueSolutionScoreDao = new ValueSolutionScoreDao(valueSolutionScoreDaoConfig, this);
        valueDao = new ValueDao(valueDaoConfig, this);
        valueScoreDao = new ValueScoreDao(valueScoreDaoConfig, this);

        registerDao(Solution.class, solutionDao);
        registerDao(Problem.class, problemDao);
        registerDao(Versus.class, versusDao);
        registerDao(SolutionScore.class, solutionScoreDao);
        registerDao(VersusResponse.class, versusResponseDao);
        registerDao(Comment.class, commentDao);
        registerDao(User.class, userDao);
        registerDao(ValueSolutionScore.class, valueSolutionScoreDao);
        registerDao(Value.class, valueDao);
        registerDao(ValueScore.class, valueScoreDao);
    }
    
    public void clear() {
        solutionDaoConfig.getIdentityScope().clear();
        problemDaoConfig.getIdentityScope().clear();
        versusDaoConfig.getIdentityScope().clear();
        solutionScoreDaoConfig.getIdentityScope().clear();
        versusResponseDaoConfig.getIdentityScope().clear();
        commentDaoConfig.getIdentityScope().clear();
        userDaoConfig.getIdentityScope().clear();
        valueSolutionScoreDaoConfig.getIdentityScope().clear();
        valueDaoConfig.getIdentityScope().clear();
        valueScoreDaoConfig.getIdentityScope().clear();
    }

    public SolutionDao getSolutionDao() {
        return solutionDao;
    }

    public ProblemDao getProblemDao() {
        return problemDao;
    }

    public VersusDao getVersusDao() {
        return versusDao;
    }

    public SolutionScoreDao getSolutionScoreDao() {
        return solutionScoreDao;
    }

    public VersusResponseDao getVersusResponseDao() {
        return versusResponseDao;
    }

    public CommentDao getCommentDao() {
        return commentDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public ValueSolutionScoreDao getValueSolutionScoreDao() {
        return valueSolutionScoreDao;
    }

    public ValueDao getValueDao() {
        return valueDao;
    }

    public ValueScoreDao getValueScoreDao() {
        return valueScoreDao;
    }

}
