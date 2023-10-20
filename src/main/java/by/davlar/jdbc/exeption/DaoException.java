package by.davlar.jdbc.exeption;

public class DaoException extends RuntimeException {
    public DaoException(Throwable e) {
        super(e);
    }
}
