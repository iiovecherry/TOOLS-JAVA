package com.iiovecherry.util.dto;

import org.hibernate.Criteria;

/**
 * 
 * @author D.I.hunter
 * @ClassName: IDtoConvert.java
 * @date 2014-12-11
 * @Description: The interface to convert DTO and entity
 */
public interface IDtoConvert<EntityType,DtoType> {
    /**
     * Get entity to save data
     * 
     * @param dto
     * @param optDao
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    public EntityType getCreateEntity(DtoType dto) throws ClassNotFoundException, InstantiationException, IllegalAccessException;

    /**
     * Update entity with DTO
     * 
     * @param dto
     * @param optDao
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public EntityType initUpdateEntity(DtoType dto) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException;

    /**
     * Get DTO to show for UI
     * 
     * @param entity
     * @param myClass
     * @return
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    public DtoType getDto(EntityType entity, Class<DtoType> myClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException;

    /**
     * configure search Criteria
     * 
     * @param criteria
     * @param dto
     * @return
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    public void initSearchCriteria(Criteria criteria, DtoType dto) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException;

    /**
     * set listener to tools
     * 
     * @param listener
     */
    public void setListener(IDtoConvertListener<EntityType,DtoType> listener);
    /**
     * Set max dept change
     * @param limit
     */
    public void setMaxDept(int limit);
}
