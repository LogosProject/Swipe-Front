package greendao;

import greendao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table VERSUS_RESPONSE.
 */
public class VersusResponse {

    private Long id;
    private Double response;
    private Long versusId;
    private Long userId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient VersusResponseDao myDao;

    private Versus versus;
    private Long versus__resolvedKey;

    private User user;
    private Long user__resolvedKey;


    public VersusResponse() {
    }

    public VersusResponse(Long id) {
        this.id = id;
    }

    public VersusResponse(Long id, Double response, Long versusId, Long userId) {
        this.id = id;
        this.response = response;
        this.versusId = versusId;
        this.userId = userId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getVersusResponseDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getResponse() {
        return response;
    }

    public void setResponse(Double response) {
        this.response = response;
    }

    public Long getVersusId() {
        return versusId;
    }

    public void setVersusId(Long versusId) {
        this.versusId = versusId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /** To-one relationship, resolved on first access. */
    public Versus getVersus() {
        Long __key = this.versusId;
        if (versus__resolvedKey == null || !versus__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            VersusDao targetDao = daoSession.getVersusDao();
            Versus versusNew = targetDao.load(__key);
            synchronized (this) {
                versus = versusNew;
            	versus__resolvedKey = __key;
            }
        }
        return versus;
    }

    public void setVersus(Versus versus) {
        synchronized (this) {
            this.versus = versus;
            versusId = versus == null ? null : versus.getId();
            versus__resolvedKey = versusId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public User getUser() {
        Long __key = this.userId;
        if (user__resolvedKey == null || !user__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
            	user__resolvedKey = __key;
            }
        }
        return user;
    }

    public void setUser(User user) {
        synchronized (this) {
            this.user = user;
            userId = user == null ? null : user.getId();
            user__resolvedKey = userId;
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
