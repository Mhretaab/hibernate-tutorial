package com.tutorial.hibernate.utils;

import com.sun.xml.internal.ws.util.StringUtils;
import com.tutorial.hibernate.models.Bee;
import com.tutorial.hibernate.models.Honey;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.postgresql.ds.PGSimpleDataSource;

import javax.persistence.*;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class EntityManagerFactoryWithJava {
    private static EntityManagerFactory emFactory;

    private EntityManagerFactoryWithJava(){

    }

    public static EntityManager getEntityManager(){
        if(emFactory == null){
            PGSimpleDataSource dataSource = new PGSimpleDataSource();

            dataSource.setDatabaseName("learninghibernate");
            dataSource.setUser("learninghibernate");
            dataSource.setPassword("learninghibernate");

            Properties properties = new Properties();

            properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            properties.put("hibernate.connection.driver_class", "org.postgresql.Driver");
            properties.put("hibernate.hbm2ddl.auto", "create");

            properties.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/learninghibernate");
            properties.put("hibernate.connection.username", "learninghibernate");
            properties.put("hibernate.connection.password", "learninghibernate");

            PersistenceUnitInfo persistenceUnitInfo = new PersistenceUnitInfo() {

                @Override
                public Properties getProperties() {
                    return properties;
                }

                @Override
                public List<String> getManagedClassNames() {
                    return Arrays.asList(Honey.class.getName(), Bee.class.getName());
                }

                @Override
                public String getPersistenceUnitName() {
                    return "TestUnit";
                }

                @Override
                public String getPersistenceProviderClassName() {
                    return HibernatePersistenceProvider.class.getName();
                }

                @Override
                public PersistenceUnitTransactionType getTransactionType() {
                    return PersistenceUnitTransactionType.RESOURCE_LOCAL;
                }

                @Override
                public DataSource getJtaDataSource() {
                    return null;
                }

                @Override
                public DataSource getNonJtaDataSource() {
                    return dataSource;
                }

                @Override
                public List<String> getMappingFileNames() {
                    return null;
                }

                @Override
                public List<URL> getJarFileUrls() {
                    return Collections.emptyList();
                }

                @Override
                public URL getPersistenceUnitRootUrl() {
                    return null;
                }

                @Override
                public boolean excludeUnlistedClasses() {
                    return false;
                }

                @Override
                public SharedCacheMode getSharedCacheMode() {
                    return null;
                }

                @Override
                public ValidationMode getValidationMode() {
                    return null;
                }

                @Override
                public String getPersistenceXMLSchemaVersion() {
                    return null;
                }

                @Override
                public ClassLoader getClassLoader() {
                    return null;
                }

                @Override
                public void addTransformer(ClassTransformer transformer) {

                }

                @Override
                public ClassLoader getNewTempClassLoader() {
                    return null;
                }
            };

            HibernatePersistenceProvider hibernatePersistenceProvider = new HibernatePersistenceProvider();

            emFactory = hibernatePersistenceProvider
                    .createContainerEntityManagerFactory(persistenceUnitInfo, Collections.EMPTY_MAP);
        }
        return emFactory.createEntityManager();
    }
    public static void close(){
        emFactory.close();
    }
}
