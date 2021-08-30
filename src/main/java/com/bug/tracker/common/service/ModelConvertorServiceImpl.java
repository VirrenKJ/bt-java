package com.bug.tracker.common.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("modelConvertorService")
public class ModelConvertorServiceImpl implements ModelConvertorService{

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public <D, T> D map(final T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }

    @Override
    public <D, T> List<D> map(final Collection<T> entityList, Class<D> outCLass) {
        List<D> objectTOList = new ArrayList<>();
        if (entityList != null && entityList.size() > 0) {
            for (T entityListObject : entityList) {
                objectTOList.add(map(entityListObject, outCLass));
            }
        }
        return objectTOList;
    }

}
