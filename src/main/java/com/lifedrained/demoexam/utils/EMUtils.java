package com.lifedrained.demoexam.utils;

import com.lifedrained.demoexam.Main;
import com.lifedrained.demoexam.tempdata.TabArgs;
import com.lifedrained.demoexam.singleton.ParamConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Table;
import org.hibernate.Session;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EMUtils {
    private static EntityManagerFactory emf;
    private static final String BASE_URL = "jdbc:mysql://localhost:3306/";


    /**
     * Creates an instance of interface {@link EntityManager}
     * with following properties:
     * <p>Secrets using {@link #loadSecrets()}<p/>
     * <p>Connection URL using {@link #formUrl()}<p/>
     *
     * @return an instance of {@link EntityManager} with properties mentioned above.
     *
     * @throws NullPointerException if {@code DB_NAME} in {@link ParamConfig} is null
     */
    public static EntityManager getEntityManager() {
        Map<String, String> props = new HashMap<>();
        try {
            String[] secrets = loadSecrets();

            String url = formUrl();

            props.put("jakarta.persistence.jdbc.url", url);
            props.put("jakarta.persistence.jdbc.user", secrets[0]);
            props.put("jakarta.persistence.jdbc.password", secrets[1]);
            props.put("jakarta.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
            props.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            props.put("hibernate.hbm2ddl.auto", "update");

            emf = new HibernatePersistenceProvider().createEntityManagerFactory("pers", props);
            return emf.createEntityManager();
        } catch (IOException e) {
            AlertUtils.showError("Error during loading secrets", e);
            throw new RuntimeException(e);
        }
    }

    public static void shutdown() {
        if (emf != null) {
            emf.close();
        }
    }

    public static Session getSession() {
        return getEntityManager().unwrap(Session.class);
    }

    /**
     *  Loads a login with password for database authorization that is stored in your package in file {@code secrets/db_root}.
     *  <p>Example: {@code resources/com/example/exampleproject/secrets/db_root} .</p>
     *  <p>Reads secrets in order: login, new line, password  </p>
     * @return an Array with 2 elements: login and password
     * @throws IOException if secrets doesn't exists
     */
    private static String[] loadSecrets() throws IOException {

        try (InputStream is = Main.class.getResourceAsStream("secrets/db_root")) {
            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println(Arrays.deepToString(content.split(":")));
            System.out.println(content.split(":")[0]);
            return content.split(":");
        } catch (NullPointerException ex) {
            AlertUtils.showError("Error during loading secrets to DB", "Check if secrets file exists at resources/secrets/db_root");
            throw new RuntimeException(ex);
        }

    }

    /**
     * Forms a connection URL to MysSQL Database using {@link ParamConfig}.
     *
     * @throws NullPointerException if {@code ParamConfig.DB_NAME} is null
     */
    private static String formUrl() throws NullPointerException {
        ParamConfig paramConfig = ParamConfig.getInstance();
        List<MySqlUrlParams> params = paramConfig.getParams();
        StringBuilder sb = new StringBuilder();

        if (ParamConfig.DB_NAME == null) {
            throw new NullPointerException("Database name must be set! (Set it via ParamConfig.DB_NAME)");
        }

        sb.append(BASE_URL);
        sb.append(ParamConfig.DB_NAME);

        if (params == null) {
            System.err.println("Params empty (null)");
            return sb.toString();
        }

        for (int i = 0; i < params.size(); i++) {
            if (i == 0) {
                sb.append("?");
            }

            sb.append(params.get(i).getValue());

            if (i != params.size() - 1) {
                sb.append("&");
            }
        }

        System.out.println("formed url: " + sb);
        return sb.toString();
    }

    /**
     * @return a list of {@link TabArgs} which contains table name and entity class type.
     * <p> If element is null, then string "null" will be passed as item<p/>
     */
    public static List<TabArgs> getTableArgs() {
        if (emf == null){
            getEntityManager();
        }

       return emf.getMetamodel().getEntities().stream().map(entityType -> {
           Table t = entityType.getJavaType().getAnnotation(Table.class);
            return new TabArgs(entityType.getJavaType() ,t==null ? "null" : t.name());
        }).toList();
    }
    public static String getTableNameByClass (Class<?> entityType) {
      return   emf.getMetamodel().entity(entityType)
                .getJavaType().getAnnotation(Table.class).name();
    }

}

