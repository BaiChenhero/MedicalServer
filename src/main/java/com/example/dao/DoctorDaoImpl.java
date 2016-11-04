package com.example.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.example.model.Doctor;
import com.example.model.Evaluate;

@Repository(value = "doctorDao")
public class DoctorDaoImpl implements DoctorDao {

	@Autowired
	private SessionFactory sessionFactory;

	public List<Doctor> findDoctorsByClassType(String classType) {
		String hql = "from Doctor where office = ? ";
		Query query = createQuery(hql, classType);
		List<Doctor> doctors = query.list();
		return doctors;
	}

	// 根据医生id查找医生
	public Doctor findDoctorById(long doctorId) {
		String hql = "from Doctor where id = ? ";
		Query query = createQuery(hql, doctorId);
		if (!query.list().isEmpty()) {
			Doctor doctor = (Doctor) query.list().get(0);
			return doctor;
		} else {
			return null;
		}
	}

	// 获取该医生网友对其的评价信息
	public List<Evaluate> findEvaluateInfoByDoctor(long doctorId) {
		return null;
	}

	public void updateDoctorInfo(Doctor doctor) {
		Session session = sessionFactory.getCurrentSession();
		session.update(doctor);
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
