package com.iiovecherry.util.dto;

import java.lang.reflect.Field;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * 
 * @author D.I.hunter
 * @ClassName: BaseDtoConvert.java
 * @date 2014-12-11
 * @Description:The base convert DTO and Entity tools implements
 *                  {@code IDtoConvert}
 */
public class BaseDtoConvert<EntityType, DtoType> implements IDtoConvert<EntityType, DtoType> {

    private IDtoConvertListener<EntityType, DtoType> listener;
    private HibernateTemplate optDao;
    private int maxDept = 1;

    public BaseDtoConvert(HibernateTemplate optDao) {
        super();
        this.optDao = optDao;
        if (optDao == null)
            throw new IllegalArgumentException("HibernateTemplate is null");
    }

    public BaseDtoConvert(HibernateTemplate optDao, IDtoConvertListener<EntityType, DtoType> listener) {
        this(optDao);
        this.listener = listener;
    }

    @SuppressWarnings("unchecked")
    @Override
    public EntityType getCreateEntity(DtoType dto) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (dto == null)
            throw new IllegalArgumentException("dto is null");
        FieldHolder holder = new FieldHolder(dto.getClass(), 1);
        EntityType entity = (EntityType) getNewEntity(dto);
        if (checkListen())
            listener.beforeCreateEntity(entity, dto);
        convertEntity(entity, dto, holder);
        if (checkListen())
            listener.afterCreateEntity(entity, dto);
        return entity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public EntityType initUpdateEntity(DtoType dto) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
        if (dto == null)
            throw new IllegalArgumentException("dto is null");
        FieldHolder holder = new FieldHolder(dto.getClass(), 2);
        EntityType entity = (EntityType) getEntity(dto, holder, true);
        if (checkListen())
            listener.beforeUpdateEntity(entity, dto);
        convertEntity(entity, dto, holder);
        if (checkListen())
            listener.afterUpdateEntity(entity, dto);
        return entity;
    }

    private void convertEntity(Object entity, Object dto, FieldHolder holder) throws IllegalArgumentException, IllegalAccessException {
        if (holder.getDtoFields() == null || holder.getDtoFields().size() < 1)
            return;
        for (Field fd : holder.getDtoFields()) {
            Field fe = holder.getEnityField(fd);
            if (fe == null)
                continue;
            fe.setAccessible(true);
            fd.setAccessible(true);
            Object oDto = fd.get(dto);
            Object oEntity = getEntityValue(oDto, fd, fe, holder);
            if (oEntity != null) {
                fe.set(entity, oEntity);
            }
        }
    }

    private Object getEntityValue(Object oDto, Field fd, Field fe, FieldHolder holder) throws IllegalArgumentException, IllegalAccessException {
        if (oDto == null)
            return null;
        String strData = null;
        if (oDto instanceof String) {
            strData = (String) oDto;
            if (StringUtils.isBlank(strData))
                return null;
        }
        if (fe.getType() == fd.getType())
            return oDto;
        Object testEntity = getEntity(oDto, holder, false);
        if (testEntity != null)
            return testEntity;
        String eName = fe.getType().getName();
        String dName = fd.getType().getName();
        if (StringUtils.isNotBlank(strData)) {
            if ("java.util.Date".equalsIgnoreCase(eName)) {
                return TimeUtil.strToDate(strData, TimeUtil.DATE_TIME_MATCH);
            }
            if ("java.lang.Integer".equalsIgnoreCase(eName) || "int".equalsIgnoreCase(eName)) {
                return Integer.parseInt(strData);
            }
            if ("java.lang.Double".equalsIgnoreCase(eName) || "double".equalsIgnoreCase(eName)) {
                return Double.parseDouble(strData);
            }
            if ("java.lang.Float".equalsIgnoreCase(eName) || "float".equalsIgnoreCase(eName)) {
                return Float.parseFloat(strData);
            }
            if ("java.lang.Boolean".equalsIgnoreCase(eName) || "boolean".equalsIgnoreCase(eName)) {
                return "true".equalsIgnoreCase(strData) ? true : false;
            }
            return optDao == null ? null : optDao.get(eName, strData);
        }
        if ("java.lang.Boolean".equalsIgnoreCase(dName) || "boolean".equalsIgnoreCase(dName)) {
            if ("java.lang.Integer".equalsIgnoreCase(eName) || "int".equalsIgnoreCase(eName)) {
                return (Boolean) oDto ? 0 : 1;
            }
        }
        return oDto;
    }

    @SuppressWarnings("unchecked")
    @Override
    public DtoType getDto(EntityType entity, Class<DtoType> myClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (entity == null)
            throw new IllegalArgumentException("entity is null");
        DtoType dto = myClass.newInstance();
        if (checkListen())
            listener.beforeCreateDto(entity, dto);

        if (checkListen())
            listener.afterCreateDto(entity, dto);
        return (DtoType) getDto(entity, dto, myClass, maxDept);
    }

    private Object getDto(Object entity, Object dto, Class<?> myClass, int limit) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (limit < 0)
            return null;
        if (entity == null)
            throw new IllegalArgumentException("entity is null");
        FieldHolder holder = new FieldHolder(myClass, 5);
        if (dto == null) {
            dto = myClass.newInstance();
        }
        convertDto(entity, dto, holder, limit);
        return dto;
    }

    private void convertDto(Object entity, Object dto, FieldHolder holder, int limit) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        if (holder.getDtoFields() == null || holder.getDtoFields().size() < 1)
            return;
        for (Field fd : holder.getDtoFields()) {
            Field fe = holder.getEnityField(fd);
            if (fe == null)
                continue;
            fe.setAccessible(true);
            fd.setAccessible(true);
            Object oEntity = fe.get(entity);
            Object oDto = getDtoValue(oEntity, fd, fe, holder, limit);
            if (oDto != null) {
                fd.set(dto, oDto);
            }
        }
    }

    private Object getDtoValue(Object oEntity, Field fd, Field fe, FieldHolder holder, int limit) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (oEntity == null)
            return null;
        if (fe.getType() == fd.getType())
            return oEntity;
        Class<?> dtoClass = fd.getType();
        EntityMark anno = dtoClass.getAnnotation(EntityMark.class);
        if (anno != null && !"null".equalsIgnoreCase(anno.name()) && fe.getType() == Class.forName(anno.name())) {
            return getDto(oEntity, null, dtoClass, limit - 1);
        }
        String fdName = fd.getType().getName();
        String feName = fe.getType().getName();
        if ("java.lang.Boolean".equalsIgnoreCase(fdName) || "boolean".equalsIgnoreCase(fdName)) {
            if ("int".equalsIgnoreCase(feName) || "java.lang.Integer".equalsIgnoreCase(feName)) {
                Integer temp = (Integer) oEntity;
                return temp == 1 ? true : false;
            } else {
                throw new IllegalArgumentException(oEntity.getClass() + " can't change to boolean");
            }
        }
        if ("java.lang.String".equalsIgnoreCase(fdName)) {
            if ("int".equalsIgnoreCase(feName) || "java.lang.Integer".equalsIgnoreCase(feName)) {
                Integer temp = (Integer) oEntity;
                return temp.toString();
            }
            if ("java.util.Date".equalsIgnoreCase(feName)) {
                Date temp = (Date) oEntity;
                EntityMark annoShowtype = fd.getAnnotation(EntityMark.class);
                String showType = annoShowtype.showType();
                if ("Date-time".equalsIgnoreCase(showType)) {
                    return TimeUtil.dateToString(temp, TimeUtil.DATE_TIME_MATCH);
                } else if ("Date-date".equalsIgnoreCase(showType)) {
                    return TimeUtil.dateToString(temp, TimeUtil.DATE_MATCH);
                } else {
                    return TimeUtil.dateToString(temp, TimeUtil.DATE_TIME_MATCH);
                }
            } else {
                FieldHolder idHolder = new FieldHolder(fe.getType(), 4);
                if (idHolder.getIdField() != null) {
                    idHolder.getIdField().setAccessible(true);
                    return idHolder.getIdField().get(oEntity);
                }
            }
        }
        return oEntity;
    }

    @Override
    public void initSearchCriteria(Criteria criteria, DtoType dto) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
        if (dto == null || criteria == null)
            return;
        if (checkListen())
            listener.beforeSearch(criteria, dto);
        FieldHolder holder = new FieldHolder(dto.getClass(), 3);
        if (holder.getDtoFields() == null || holder.getDtoFields().size() < 1) {
            if (checkListen())
                listener.aftereSearch(criteria, dto);
            return;
        }
        for (Field fd : holder.getDtoFields()) {
            EntitySearch anno = fd.getAnnotation(EntitySearch.class);
            if (anno != null) {
                String name = anno.name();
                if ("null".equalsIgnoreCase(name)) {
                    name = fd.getName();
                }
                fd.setAccessible(true);
                Object oDto = fd.get(dto);
                Object oNew = typeToSearch(oDto, anno.dataType());
                if (oNew == null)
                    continue;
                switch (anno.searchType()) {
                case 0:
                    criteria.add(Restrictions.eq(name, oNew));
                    break;
                case 1:
                    criteria.add(Restrictions.gt(name, oNew));
                    break;
                case 11:
                    criteria.add(Restrictions.ge(name, oNew));
                    break;
                case 2:
                    criteria.add(Restrictions.lt(name, oNew));
                    break;
                case 21:
                    criteria.add(Restrictions.le(name, oNew));
                    break;
                case 3:
                    criteria.add(Restrictions.like(name, (String) oNew, MatchMode.ANYWHERE));
                    break;
                case 31:
                    criteria.add(Restrictions.like(name, (String) oNew, MatchMode.START));
                    break;
                case 32:
                    criteria.add(Restrictions.like(name, (String) oNew, MatchMode.END));
                    break;
                default:
                    break;
                }
            }
        }
        if (checkListen())
            listener.aftereSearch(criteria, dto);
    }

    /**
     * Here to set rule for search with Criteria
     * 
     * @param oDto
     * @param feName
     * @param fdName
     * @return
     */
    private Object typeToSearch(Object oDto, String type) {
        if (oDto == null)
            return null;
        if (type == null || "null".equalsIgnoreCase(type))
            return oDto;
        if (type.startsWith("Date")) {
            if (!("java.lang.String".equalsIgnoreCase(oDto.getClass().getName())))
                throw new IllegalArgumentException(oDto.getClass().getName() + " can't change to java.util.Date");
            String str = (String) oDto;
            if (str.endsWith("time")) {
                return TimeUtil.strToDate(str, TimeUtil.DATE_TIME_MATCH);
            } else if (str.endsWith("date")) {
                return TimeUtil.strToDate(str, TimeUtil.DATE_MATCH);
            } else if (str.endsWith("max")) {
                return TimeUtil.strToDateTime(str, true);
            } else if (str.endsWith("min")) {
                return TimeUtil.strToDateTime(str, false);
            }
            return TimeUtil.strToDate(str, TimeUtil.DATE_TIME_MATCH);
        }
        if (type.startsWith("int")) {
            boolean temp = (Boolean) oDto;
            return temp ? 1 : 0;
        }
        return null;
    }

    public IDtoConvertListener<EntityType, DtoType> getListener() {
        return listener;
    }

    public HibernateTemplate getOptDao() {
        return optDao;
    }

    public void setOptDao(HibernateTemplate optDao) {
        this.optDao = optDao;
    }

    @Override
    public void setListener(IDtoConvertListener<EntityType, DtoType> listener) {
        this.listener = listener;
    }

    public int getMaxDept() {
        return maxDept;
    }

    @Override
    public void setMaxDept(int maxDept) {
        this.maxDept = maxDept;
    }

    /**
     * Check user add listen or not
     * 
     * @return
     */
    private boolean checkListen() {
        return listener != null ? true : false;
    }

    private Object getNewEntity(Object dto) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        EntityMark anno = dto.getClass().getAnnotation(EntityMark.class);
        String entityName = null;
        if (anno == null || "null".equalsIgnoreCase(entityName = anno.name())) {
            throw new IllegalArgumentException(dto.getClass() + " not found entity mark or entity mark name is null");
        }
        Class<?> newClass = Class.forName(entityName);
        return newClass.newInstance();
    }

    /**
     * 
     * @param dto
     * @param holder
     * @param mustEntity
     *            if false,DTO not entity mark return null else have exception
     *            here
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private Object getEntity(Object dto, FieldHolder holder, boolean mustEntity) throws IllegalArgumentException, IllegalAccessException {
        EntityMark anno = dto.getClass().getAnnotation(EntityMark.class);
        String entityName = null;
        if (anno == null || "null".equalsIgnoreCase(entityName = anno.name())) {
            if (mustEntity) {
                throw new IllegalArgumentException(dto.getClass() + " not found entity mark or entity mark name is null");
            } else {
                return null;
            }
        }
        if (holder.getIdField() == null)
            throw new IllegalArgumentException("Id EntityMark is not found!");
        holder.getIdField().setAccessible(true);
        Object idOb = holder.getIdField().get(dto);
        if (idOb == null || !(idOb instanceof String)) {
            throw new IllegalArgumentException("Id EntityMark is not String or id value is null");
        }
        String id = (String) idOb;
        return optDao == null ? null : optDao.get(entityName, id);
    }

}
