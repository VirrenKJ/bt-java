package com.bug.tracker.common.service;

import java.util.Collection;
import java.util.List;

public interface ModelConvertorService {

    <D, T> D map(final T entity, Class<D> outClass);

    <D, T> List<D> map(final Collection<T> entityList, Class<D> outCLass);
}
