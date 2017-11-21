package com.xu.myprojectstool.base;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Administrator on 2017/9/24.
 */

public class BaseInfo {

    private Realm realm;
    private Context context;

    public BaseInfo(Context context) {
        this.context = context;
        realm = Realm.getInstance(BaseApplication.getRealmConfigurationInstance());
    }


    public static BaseInfo getInstance(Context context){
        return new BaseInfo(context);
    }

    public <T extends RealmObject> void  saveInfo(final T realmObject){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(realmObject);
            }
        });
    }

    public <T extends RealmObject> RealmResults<T> findAll(Class<T> classType){
        return realm.where(classType).findAll();
    }

    public <T extends RealmObject> void clearTable(final Class<T> tableName){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(tableName);
            }
        });
    }

    public ImageData findExist(String userName){
        return realm.where(ImageData.class).equalTo("name",userName).findFirst();
    }
//
//    public void deletUser(final UserData data){
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                data.deleteFromRealm();
//            }
//        });
//    }
}
