package edu.bbte.idde.mnim2377.data.dao;

public class PostgresCalendarDaoFactory extends DaoFactory{
    @Override
    public CalendarDao getCalendarDao() {
        return new PostgresCalendarDao();
    }
}
