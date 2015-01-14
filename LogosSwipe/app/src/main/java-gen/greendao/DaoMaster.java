package greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

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
 * Master of DAO (schema version 3): knows all DAOs.
*/
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 3;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        SolutionDao.createTable(db, ifNotExists);
        ProblemDao.createTable(db, ifNotExists);
        VersusDao.createTable(db, ifNotExists);
        SolutionScoreDao.createTable(db, ifNotExists);
        VersusResponseDao.createTable(db, ifNotExists);
        CommentDao.createTable(db, ifNotExists);
        UserDao.createTable(db, ifNotExists);
        ValueSolutionScoreDao.createTable(db, ifNotExists);
        ValueDao.createTable(db, ifNotExists);
        ValueScoreDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        SolutionDao.dropTable(db, ifExists);
        ProblemDao.dropTable(db, ifExists);
        VersusDao.dropTable(db, ifExists);
        SolutionScoreDao.dropTable(db, ifExists);
        VersusResponseDao.dropTable(db, ifExists);
        CommentDao.dropTable(db, ifExists);
        UserDao.dropTable(db, ifExists);
        ValueSolutionScoreDao.dropTable(db, ifExists);
        ValueDao.dropTable(db, ifExists);
        ValueScoreDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }
    
    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(SolutionDao.class);
        registerDaoClass(ProblemDao.class);
        registerDaoClass(VersusDao.class);
        registerDaoClass(SolutionScoreDao.class);
        registerDaoClass(VersusResponseDao.class);
        registerDaoClass(CommentDao.class);
        registerDaoClass(UserDao.class);
        registerDaoClass(ValueSolutionScoreDao.class);
        registerDaoClass(ValueDao.class);
        registerDaoClass(ValueScoreDao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }
    
}