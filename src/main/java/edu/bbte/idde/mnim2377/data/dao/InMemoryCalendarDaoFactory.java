package edu.bbte.idde.mnim2377.data.dao;

public class InMemoryCalendarDaoFactory extends DaoFactory{
    @Override
    public CalendarDao getCalendarDao() {
        return new InMemoryCalendarDao();
    }
}
