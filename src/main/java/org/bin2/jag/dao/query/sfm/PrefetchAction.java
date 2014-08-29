package org.bin2.jag.dao.query.sfm;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by benoitroger on 29/08/14.
 */
public interface PrefetchAction {
    void execute(ResultSet rs) throws SQLException;
}
