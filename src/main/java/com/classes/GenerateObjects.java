package com.classes;

import org.apache.cayenne.configuration.ConfigurationNode;
import org.apache.cayenne.configuration.ConfigurationTree;
import org.apache.cayenne.configuration.DataChannelDescriptor;
import org.apache.cayenne.configuration.server.ServerModule;
import org.apache.cayenne.dbsync.DbSyncModule;
import org.apache.cayenne.dbsync.naming.NameBuilder;
import org.apache.cayenne.di.DIBootstrap;
import org.apache.cayenne.di.Injector;
import org.apache.cayenne.di.Module;
import org.apache.cayenne.map.DataMap;
import org.apache.cayenne.map.DbAttribute;
import org.apache.cayenne.map.DbEntity;
import org.apache.cayenne.map.ObjAttribute;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.project.Project;
import org.apache.cayenne.project.ProjectModule;
import org.apache.cayenne.project.ProjectSaver;
import org.apache.cayenne.resource.URLResource;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class GenerateObjects {


    public void createTestProject(String entitiesCount, String attributesCount) {

        DataChannelDescriptor dataChannelDescriptor = new DataChannelDescriptor();
        dataChannelDescriptor.setName(NameBuilder.builder(dataChannelDescriptor).name());
        Project project1 = new Project(new ConfigurationTree<ConfigurationNode>(dataChannelDescriptor));
        StringRandomGenerator generator = new StringRandomGenerator();
        int COUNT_ENTITIES = Integer.parseInt(entitiesCount);
        int COUNT_ATTRIBUTES = Integer.parseInt(attributesCount);
        DataMap dataMap = new DataMap();
        for (int i = 1; i <= COUNT_ENTITIES; i++) {
            DbEntity dbEntity = new DbEntity(generator.generateRandomString());
            DbAttribute dbAttribute = new DbAttribute("id");
            dbAttribute.setPrimaryKey(true);
            dbAttribute.setMandatory(true);
            dbAttribute.setType(Types.INTEGER);
            for (int j = 1; j <= COUNT_ATTRIBUTES; j++) {
                DbAttribute dbAttr = new DbAttribute(generator.generateRandomString());
                dbAttr.setType(Types.VARCHAR);
                dbAttr.setMaxLength(255);
                dbEntity.addAttribute(dbAttr);
            }
            dbEntity.addAttribute(dbAttribute);
            dataMap.addDbEntity(dbEntity);
            ObjEntity objEntity = new ObjEntity(generator.generateRandomString());
            objEntity.setDbEntity(dbEntity);
            List<String> namesOfDbAttributes = new ArrayList<String>();
            for (DbAttribute dbAttr : dbEntity.getAttributes()) {
                namesOfDbAttributes.add(dbAttr.getName());
            }
            for (int j = 0; j <= COUNT_ATTRIBUTES; j++) {
                ObjAttribute objAttribute = new ObjAttribute(generator.generateRandomString());
                if (!namesOfDbAttributes.get(j).equals("id")) {
                    objAttribute.setDbAttributePath(namesOfDbAttributes.get(j));
                    objAttribute.setType("java.lang.String");
                    objEntity.addAttribute(objAttribute);
                }
            }
            dataMap.addObjEntity(objEntity);
        }

        DataChannelDescriptor descriptor = (DataChannelDescriptor) project1.getRootNode();
        dataMap.setName(NameBuilder.builder(dataMap, descriptor).name());
        descriptor.getDataMaps().add(dataMap);
        Injector injector = DIBootstrap.createInjector(appendModules(new ArrayList<Module>()));
        ProjectSaver saver = injector.getInstance(ProjectSaver.class);
        URLResource res = null;
        try {
            res = new URLResource(new File("cayenne-testProject.xml").toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        saver.saveAs(project1, res);
    }

    protected Collection<Module> appendModules(Collection<Module> modules) {
        modules.add(new ServerModule("CayenneModeler"));
        modules.add(new ProjectModule());
        modules.add(new DbSyncModule());
        return modules;
    }
}
