package com.yq008.basepro.applib.db.dao;

import com.yq008.basepro.applib.db.bean.TBGuide;
import com.yq008.basepro.applib.db.helper.DaoHelper;

/**
 * 引导页Dao
 * Created by Xiay on 2017/3/21.
 */

public class GuideDao extends DaoHelper<TBGuide> {
    /**
     * 返回实体类对应的dao对象
     */
    public GuideDao() {
        super(TBGuide.class);
    }
}
