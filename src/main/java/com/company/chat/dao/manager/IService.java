package com.company.chat.dao.manager;

import com.company.chat.dao.exceptions.FailedCRUDException;
import org.assertj.core.api.Fail;

import java.util.Map;

public interface IService<T> {

	String save(T item) throws Exception;

	T update(T item) throws Exception;

	T findById(String id);

	Map<String, T> findAll();

	T delete(String id) throws Exception;
}
