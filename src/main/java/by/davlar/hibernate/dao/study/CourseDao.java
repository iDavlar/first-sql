package by.davlar.hibernate.dao.study;

import by.davlar.hibernate.dao.AbstractDao;
import by.davlar.hibernate.entity.study.Course;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseDao extends AbstractDao<Integer, Course> {
    private static final CourseDao INSTANCE = new CourseDao();

    public static CourseDao getInstance() {
        return INSTANCE;
    }
}
