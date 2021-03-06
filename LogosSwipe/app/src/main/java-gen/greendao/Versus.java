package greendao;

import greendao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table VERSUS.
 */
public class Versus {

    private Long id;
    private Long solution1Id;
    private Long solution2Id;
    private Long valueID;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient VersusDao myDao;

    private Solution solution1;
    private Long solution1__resolvedKey;

    private Solution solution2;
    private Long solution2__resolvedKey;

    private Value versusId;
    private Long versusId__resolvedKey;


    public Versus() {
    }

    public Versus(Long id) {
        this.id = id;
    }

    public Versus(Long id, Long solution1Id, Long solution2Id, Long valueID) {
        this.id = id;
        this.solution1Id = solution1Id;
        this.solution2Id = solution2Id;
        this.valueID = valueID;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getVersusDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSolution1Id() {
        return solution1Id;
    }

    public void setSolution1Id(Long solution1Id) {
        this.solution1Id = solution1Id;
    }

    public Long getSolution2Id() {
        return solution2Id;
    }

    public void setSolution2Id(Long solution2Id) {
        this.solution2Id = solution2Id;
    }

    public Long getValueID() {
        return valueID;
    }

    public void setValueID(Long valueID) {
        this.valueID = valueID;
    }

    /** To-one relationship, resolved on first access. */
    public Solution getSolution1() {
        Long __key = this.solution1Id;
        if (solution1__resolvedKey == null || !solution1__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SolutionDao targetDao = daoSession.getSolutionDao();
            Solution solution1New = targetDao.load(__key);
            synchronized (this) {
                solution1 = solution1New;
            	solution1__resolvedKey = __key;
            }
        }
        return solution1;
    }

    public void setSolution1(Solution solution1) {
        synchronized (this) {
            this.solution1 = solution1;
            solution1Id = solution1 == null ? null : solution1.getId();
            solution1__resolvedKey = solution1Id;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Solution getSolution2() {
        Long __key = this.solution2Id;
        if (solution2__resolvedKey == null || !solution2__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SolutionDao targetDao = daoSession.getSolutionDao();
            Solution solution2New = targetDao.load(__key);
            synchronized (this) {
                solution2 = solution2New;
            	solution2__resolvedKey = __key;
            }
        }
        return solution2;
    }

    public void setSolution2(Solution solution2) {
        synchronized (this) {
            this.solution2 = solution2;
            solution2Id = solution2 == null ? null : solution2.getId();
            solution2__resolvedKey = solution2Id;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Value getVersusId() {
        Long __key = this.valueID;
        if (versusId__resolvedKey == null || !versusId__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ValueDao targetDao = daoSession.getValueDao();
            Value versusIdNew = targetDao.load(__key);
            synchronized (this) {
                versusId = versusIdNew;
            	versusId__resolvedKey = __key;
            }
        }
        return versusId;
    }

    public void setVersusId(Value versusId) {
        synchronized (this) {
            this.versusId = versusId;
            valueID = versusId == null ? null : versusId.getId();
            versusId__resolvedKey = valueID;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
