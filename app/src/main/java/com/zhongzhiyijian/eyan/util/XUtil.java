package com.zhongzhiyijian.eyan.util;

import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.base.Constants;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;

import java.io.File;

import static org.xutils.DbManager.DaoConfig;
import static org.xutils.DbManager.DbOpenListener;
import static org.xutils.DbManager.DbUpgradeListener;
import static org.xutils.DbManager.TableCreateListener;

/**
 * Created by yangfan on 2016/3/26.
 */
public class XUtil implements Constants {
    static DaoConfig daoConfig;
    public static DaoConfig getDaoConfig(){
        File file=new File(localPath);
        if(daoConfig==null){
            daoConfig=new DaoConfig()
                    .setDbName("mybob.db")
                    .setDbDir(file)
                    .setTableCreateListener(new TableCreateListener() {
                        @Override
                        public void onTableCreated(DbManager db, TableEntity<?> table) {
                            Logger.e("onTableCreated>>>"+table.getName());
                        }
                    })
                    .setDbVersion(1)
                    .setAllowTransaction(true)
                    .setDbUpgradeListener(new DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                        }
                    })
            .setDbOpenListener(new DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                   db.getDatabase().enableWriteAheadLogging();
                }
            });
        }
        return daoConfig;
    }
}
