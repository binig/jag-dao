package org.bin2.jag.dao.query.sfm;

import org.bin2.jag.dao.Dao;
import org.bin2.jag.dao.IndexParameter;
import org.bin2.jag.dao.Query;

import java.util.List;

/**
 * Created by benoitroger on 29/08/14.
 */
public interface SfmDao extends Dao<MyObject, Long> {

    @Query("select id, email, my_property from MyTable where email like ? ")
    MyObject findByEmail(@IndexParameter(1) String email);


    @Query("select id, email, my_property from MyTable ")
    List<MyObject> findAll();
}
