package by.davlar.hibernate.dao.study;

import by.davlar.hibernate.dao.AbstractDao;
import by.davlar.hibernate.entity.study.TrainerCourse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TrainerCourseDao extends AbstractDao<Integer, TrainerCourse> {
    private static final TrainerCourseDao INSTANCE = new TrainerCourseDao();

    public static TrainerCourseDao getInstance() {
        return INSTANCE;
    }
}
