package edu.bbte.idde.mnim2377.data.dao;

public class InMemoryDaoFactory extends DaoFactory {
    private CalendarDao calendarDao;

    @Override
    public CalendarDao getCalendarDao() {
        if (calendarDao == null) {
            calendarDao = new InMemoryDao();
        }
        return calendarDao;
    }
}
