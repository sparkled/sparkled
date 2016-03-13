package net.chrisparton.xmas.servlet;

import net.chrisparton.xmas.persistence.PersistenceService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        PersistenceService.instance().shutdown();
    }
}
