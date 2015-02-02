package greendao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.VersusResponse;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table VERSUS_RESPONSE.
*/
public class VersusResponseDao extends AbstractDao<VersusResponse, Long> {

    public static final String TABLENAME = "VERSUS_RESPONSE";

    /**
     * Properties of entity VersusResponse.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Response = new Property(1, Double.class, "response", false, "RESPONSE");
        public final static Property VersusId = new Property(2, Long.class, "versusId", false, "VERSUS_ID");
        public final static Property UserId = new Property(3, Long.class, "userId", false, "USER_ID");
    };

    private DaoSession daoSession;


    public VersusResponseDao(DaoConfig config) {
        super(config);
    }
    
    public VersusResponseDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'VERSUS_RESPONSE' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'RESPONSE' REAL," + // 1: response
                "'VERSUS_ID' INTEGER," + // 2: versusId
                "'USER_ID' INTEGER);"); // 3: userId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'VERSUS_RESPONSE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, VersusResponse entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Double response = entity.getResponse();
        if (response != null) {
            stmt.bindDouble(2, response);
        }
 
        Long versusId = entity.getVersusId();
        if (versusId != null) {
            stmt.bindLong(3, versusId);
        }
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(4, userId);
        }
    }

    @Override
    protected void attachEntity(VersusResponse entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public VersusResponse readEntity(Cursor cursor, int offset) {
        VersusResponse entity = new VersusResponse( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getDouble(offset + 1), // response
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // versusId
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3) // userId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, VersusResponse entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setResponse(cursor.isNull(offset + 1) ? null : cursor.getDouble(offset + 1));
        entity.setVersusId(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setUserId(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(VersusResponse entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(VersusResponse entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getVersusDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getUserDao().getAllColumns());
            builder.append(" FROM VERSUS_RESPONSE T");
            builder.append(" LEFT JOIN VERSUS T0 ON T.'VERSUS_ID'=T0.'_id'");
            builder.append(" LEFT JOIN USER T1 ON T.'USER_ID'=T1.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected VersusResponse loadCurrentDeep(Cursor cursor, boolean lock) {
        VersusResponse entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Versus versus = loadCurrentOther(daoSession.getVersusDao(), cursor, offset);
        entity.setVersus(versus);
        offset += daoSession.getVersusDao().getAllColumns().length;

        User user = loadCurrentOther(daoSession.getUserDao(), cursor, offset);
        entity.setUser(user);

        return entity;    
    }

    public VersusResponse loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<VersusResponse> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<VersusResponse> list = new ArrayList<VersusResponse>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<VersusResponse> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<VersusResponse> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
