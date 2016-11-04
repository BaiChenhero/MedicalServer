package com.example.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.example.domain.UserInfo;
import com.example.model.ContractPerson;
import com.example.model.HeartRate;
import com.example.model.Record;
import com.example.model.Temperature;
import com.example.model.User;

@Repository(value = "userDao")
public class UserDAOImpl implements UserDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public void save(User user) {
		Session session = sessionFactory.getCurrentSession();
		session.save(user);
	}

	public User findUser(long userId) {
		String hql = "from User where id = ?";
		Query query = createQuery(hql, userId);
		User user = (User) query.list().get(0);
		return user;
	}

	public User findUser(UserInfo userInfo) {
		String hql = "from User where username = ? and password = ?";
		Query query = createQuery(hql, userInfo.getUsername(),
				userInfo.getPassword());
		List<User> users = query.list();
		if (users.isEmpty()) {
			return null;
		} else {
			User findUser = (User) query.list().get(0);
			return findUser;
		}

	}

	public void saveUserTemperature(Temperature temperature) {
		Session session = sessionFactory.getCurrentSession();
		session.save(temperature);
	}

	public void saveUserHeartRate(HeartRate heartRate) {
		Session session = sessionFactory.getCurrentSession();
		session.save(heartRate);
	}

	public void saveUserTumbleInfo(Record userRecord) {
		Session session = sessionFactory.getCurrentSession();
		session.save(userRecord);
	}

	public List<HeartRate> getUserHeartRateByTime(Date startDate, Date endDate,String userId) {
		User user = findUser(Long.parseLong(userId));
		String sql = "";
		Query query = null;
		if (startDate == null) {
			sql = "from HeartRate where time <= ? and user = ? ";
			query = createQuery(sql, endDate,user);
		} else {
			sql = "from HeartRate where time >= ? and time <= ? and user = ?";
			query = createQuery(sql, startDate, endDate,user);
		}
		List<HeartRate> userHeartRates = query.list();
		return userHeartRates;
	}

	public List<Temperature> getUserTemperatureByTime(Date startDate, Date endDate,String userId) {
		User user = findUser(Long.parseLong(userId));
		String sql = "";
		Query query = null;
		if (startDate == null) {
			sql = "from Temperature where time <= ? and user = ?";
			query = createQuery(sql, endDate,user);
		} else {
			sql = "from Temperature where time >= ? and time <= ? and user = ?";
			query = createQuery(sql, startDate, endDate,user);
		}
		List<Temperature> userHeartRates = query.list();
		return userHeartRates;
	}

	public boolean saveEmergencyContractPerson(ContractPerson contractPerson) {
		Session session = sessionFactory.getCurrentSession();
		session.save(contractPerson);
		return true;

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
