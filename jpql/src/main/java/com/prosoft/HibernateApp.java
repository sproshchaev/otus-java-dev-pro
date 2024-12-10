package com.prosoft;

import com.prosoft.util.LiquibaseRunner;

public class HibernateApp {

    public static void main(String[] args) {

        LiquibaseRunner.runMigrations();

    }

}
