package org.example.migration;

public interface Migration {
    void migrate();
    void setNext(Migration migration);
}
