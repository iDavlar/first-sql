package by.davlar.hibernate.dao.study;

import by.davlar.hibernate.dao.AbstractDao;
import by.davlar.hibernate.entity.study.Trainer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TrainerDao extends AbstractDao<Integer, Trainer> {
    private static final TrainerDao INSTANCE = new TrainerDao();

    public static TrainerDao getInstance() {
        return INSTANCE;
    }
}
