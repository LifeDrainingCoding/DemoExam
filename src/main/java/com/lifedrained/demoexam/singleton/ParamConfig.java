package com.lifedrained.demoexam.singleton;

import com.lifedrained.demoexam.utils.EMUtils;
import com.lifedrained.demoexam.utils.MySqlUrlParams;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * A singleton configuration class for MySQL Database.
 * Uses in  {@link EMUtils} for connection configuration
 *<p>{@link #DB_NAME} defines database name in connection url to connect, default is null<p/>
 *
 */

public class ParamConfig {
    private List<MySqlUrlParams> params;

    private static ParamConfig INSTANCE;

    public static String DB_NAME;

    private ParamConfig() {}

    public static ParamConfig getInstance()  {
        if (INSTANCE == null) {
            INSTANCE = new ParamConfig();
        }

        return INSTANCE ;
    }



    public List<MySqlUrlParams> getParams() {
        return params;
    }



    public void configure(MySqlUrlParams... params) {
        this.params = Arrays.asList(params);
    }

    public void configure(Collection<MySqlUrlParams> params) {
        this.params.addAll(params);
    }
}
