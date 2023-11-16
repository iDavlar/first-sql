package by.davlar.hibernate.dao.study;

import by.davlar.hibernate.dao.AbstractDao;
import by.davlar.hibernate.entity.study.StudentProfile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudentProfileDao extends AbstractDao<Integer, StudentProfile> {
    private static final StudentProfileDao INSTANCE = new StudentProfileDao();

    public static StudentProfileDao getInstance() {
        return INSTANCE;
    }
}
