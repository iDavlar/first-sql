package by.davlar.hibernate.dao.study;

import by.davlar.hibernate.dao.AbstractDao;
import by.davlar.hibernate.entity.study.Student;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudentDao extends AbstractDao<Integer, Student> {
    private static final StudentDao INSTANCE = new StudentDao();

    public static StudentDao getInstance() {
        return INSTANCE;
    }
}
