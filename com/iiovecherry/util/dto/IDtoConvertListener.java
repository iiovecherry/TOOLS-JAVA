package com.iiovecherry.util.dto;

import org.hibernate.Criteria;

/**
 * 
 * @author D.I.hunter
 * @ClassName: IDtoConvertListener.java
 * @date 2014-12-11
 * @Description:The interface to listener DTO and entity convert
 */
public interface IDtoConvertListener<EntityType,DtoType> {
    /**
     * when find entity and ready to auto update entity invoke
     * @param entity
     * @param dto
     * @throws IllegalArgumentException
     */
    public void beforeUpdateEntity(EntityType entity,DtoType dto) throws IllegalArgumentException;
    /**
     * when find entity and ready to auto update entity invoke
     * @param entity
     * @param dto
     * @throws IllegalArgumentException
     */
    public void afterUpdateEntity(EntityType entity,DtoType dto) throws IllegalArgumentException;
    /**
     * when create empty entity and ready to set entity value invoke
     * @param entity
     * @param dto
     * @throws IllegalArgumentException
     */
    public void beforeCreateEntity(EntityType entity,DtoType dto) throws IllegalArgumentException;
    /**
     * when finished  create entity invoke
     * @param entity
     * @param dto
     * @throws IllegalArgumentException
     */
    public void afterCreateEntity(EntityType entity,DtoType dto) throws IllegalArgumentException;
    /**
     * when create empty DTO  and ready to set DTO value invoke
     * @param entity
     * @param dto
     * @throws IllegalArgumentException
     */
    public void beforeCreateDto(EntityType entity,DtoType dto) throws IllegalArgumentException;
    /**
     * when create empty DTO  and ready to set DTO value invoke
     * @param entity
     * @param dto
     * @throws IllegalArgumentException
     */
    public void afterCreateDto(EntityType entity,DtoType dto) throws IllegalArgumentException;
    /**
     * ready to set Criteria will invoke
     * @param criteria
     * @param dto
     */
    public void beforeSearch(Criteria criteria,DtoType dto)throws IllegalArgumentException;
    /**
     * finished set Criteria will invoke
     * @param criteria
     * @param dto
     * @throws IllegalArgumentException
     */
    public void aftereSearch(Criteria criteria,DtoType dto)throws IllegalArgumentException;
}
