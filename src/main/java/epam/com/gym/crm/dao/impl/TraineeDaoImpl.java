package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.model.Trainee;
import org.springframework.stereotype.Repository;


@Repository
public class TraineeDaoImpl extends UserDaoImpl<Trainee> {
    public TraineeDaoImpl() {
        super(Trainee.class);
    }
}
