package com.ruomm.appcorex.greendao.dbstore;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.ruomm.appcorex.greendao.dbstore.DBEntryValue;

import com.ruomm.appcorex.greendao.dbstore.DBEntryValueDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 *
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig dBEntryValueDaoConfig;

    private final DBEntryValueDao dBEntryValueDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        dBEntryValueDaoConfig = daoConfigMap.get(DBEntryValueDao.class).clone();
        dBEntryValueDaoConfig.initIdentityScope(type);

        dBEntryValueDao = new DBEntryValueDao(dBEntryValueDaoConfig, this);

        registerDao(DBEntryValue.class, dBEntryValueDao);
    }

    public void clear() {
        dBEntryValueDaoConfig.clearIdentityScope();
    }

    public DBEntryValueDao getDBEntryValueDao() {
        return dBEntryValueDao;
    }

}
