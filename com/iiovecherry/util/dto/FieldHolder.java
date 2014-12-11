package com.iiovecherry.util.dto;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author D.I.hunter
 * @ClassName: FieldHolder.java
 * @date 2014-12-11
 * @Description:A tool to get Field collection
 */
public class FieldHolder {
    private Class<?> entityClass;
    private List<Field> dtoFields;
    private Map<String, Field> entityFields;
    private Field idField;
    private Class<?> dtoClass;

    /**
     * 
     * @param dtoClass
     * @param model
     *            1 createEntity 2 updateEntity 3 search Criteria 4 only for
     *            id 5 create DTO
     * @throws ClassNotFoundException
     */
    public FieldHolder(Class<?> dtoClass, int model) throws ClassNotFoundException {
        this.dtoClass = dtoClass;
        if (this.dtoClass == null)
            throw new IllegalArgumentException("dto class is null");
        switch (model) {
        case 1:
        case 2:
        case 5:
            initCreateOrupdate(model);
            break;
        case 3:
        case 4:
            initDtoField(model);
            break;
        }
    }
    private void initCreateOrupdate(int model) throws ClassNotFoundException {
        entityClass = initEntityClass();
        if (entityClass != null) {
            initDtoField(model);
        }
    }

    public Field getEnityField(Field fd) {
        if (entityFields != null && entityFields.size() > 0) {
            EntityMark anno = fd.getAnnotation(EntityMark.class);
            String name = (anno != null && !"null".equalsIgnoreCase(anno.name()) ? anno.name() : fd.getName());
            Field fe = entityFields.get(name);
            if (fe != null) {
                return fe;
            }
        }
        throw new IllegalArgumentException("entity field not found【" + fd + "】");
    }

    /**
     * 1 createEntity 2 updateEntity 3 search Criteria 4 only for id 5
     * createDTO
     * 
     * @param type
     *            1 create
     */
    private void initDtoField(int model) {
        dtoFields = new ArrayList<Field>();
        initDtoField(dtoClass, model);
        Class<?> superClass = dtoClass.getSuperclass();
        initDtoField(superClass, model);
        if (dtoFields.size() < 1) {
            dtoFields = null;
        }
    }

    private void initDtoField(Class<?> dtoClass, int model) {
        if (dtoClass != null) {
            Field[] fs = dtoClass.getDeclaredFields();
            if (fs != null && fs.length > 0) {
                for (Field item : fs) {
                    switch (model) {
                    case 1:
                        EntityMark anno1 = item.getAnnotation(EntityMark.class);
                        if (anno1 != null) {
                            if (anno1.isId()) {
                                idField = item;
                            } else if (anno1.creatable()) {
                                dtoFields.add(item);
                            }
                        }
                        break;
                    case 2:
                        EntityMark anno2 = item.getAnnotation(EntityMark.class);
                        if (anno2 != null) {
                            if (anno2.isId()) {
                                idField = item;
                            } else if (anno2.updatable()) {
                                dtoFields.add(item);
                            }
                        }
                        break;
                    case 3:
                        EntitySearch anno3 = item.getAnnotation(EntitySearch.class);
                        if (anno3 != null) {
                            dtoFields.add(item);
                        }
                        break;
                    case 4:
                        EntityMark anno4 = item.getAnnotation(EntityMark.class);
                        if (anno4 != null) {
                            if (anno4.isId()) {
                                idField = item;
                            }
                        }
                        break;
                    case 5:
                        EntityMark anno5 = item.getAnnotation(EntityMark.class);
                        if (anno5 != null && anno5.showable()) {
                            dtoFields.add(item);
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * Get the class name for entity
     * 
     * @param dto
     * @return
     * @throws ClassNotFoundException
     */
    private Class<?> initEntityClass() throws ClassNotFoundException {
        String name = null;
        EntityMark typeMark = dtoClass.getAnnotation(EntityMark.class);
        if (typeMark != null && (name = typeMark.name()) != null) {
            name = "null".equalsIgnoreCase(name) ? null : name;
        }
        if (name == null)
            throw new IllegalArgumentException("entity type is not find");
        Class<?> tempClass = null;
        tempClass = Class.forName(name);
        if (tempClass != null) {
            Field[] f1 = tempClass.getDeclaredFields();
            entityFields = new HashMap<String, Field>();
            if (f1 != null && f1.length > 0) {
                for (Field item : f1) {
                    entityFields.put(item.getName(), item);
                }
            }
            Class<?> fatherClass = tempClass.getSuperclass();
            if (fatherClass != null) {
                Field[] f2 = fatherClass.getDeclaredFields();
                if (f2 != null && f2.length > 0) {
                    for (Field item : f2) {
                        entityFields.put(item.getName(), item);
                    }
                }
            }
            if (entityFields.size() < 1) {
                entityFields = null;
            }
        }
        return tempClass;
    }
    public List<Field> getDtoFields() {
        return dtoFields;
    }
    public void setDtoFields(List<Field> dtoFields) {
        this.dtoFields = dtoFields;
    }
    public Map<String, Field> getEntityFields() {
        return entityFields;
    }
    public void setEntityFields(Map<String, Field> entityFields) {
        this.entityFields = entityFields;
    }
    public Field getIdField() {
        return idField;
    }
    public void setIdField(Field idField) {
        this.idField = idField;
    }
    
}
