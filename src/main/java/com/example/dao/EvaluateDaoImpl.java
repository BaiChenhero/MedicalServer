package com.example.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.example.model.Doctor;
import com.example.model.Evaluate;

@Repository(value = "evaluateDao")
public class EvaluateDaoImpl implements EvaluateDao {

	@Autowired
	private SessionFactory sessionFactory;

	public List<Evaluate> findEvaluatesByDoctor(Doctor doctor) {
		String hql = " from Evaluate where doctor = ?";
		Query query = createQuery(hql, doctor);
		List<Evaluate> evaluates = query.list();
		return evaluates;
	}

	// 定义条件查询
	public Query createQuery(final String queryString, final Object... values) {
		Assert.hasText(queryString, "queryString不能为空");
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}

}
