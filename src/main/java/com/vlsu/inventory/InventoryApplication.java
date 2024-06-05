package com.vlsu.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
public class InventoryApplication {

	public static void main(String[] args) {
		initDb();
		SpringApplication.run(InventoryApplication.class, args);
	}

	private static void initDb() {
		try {
			Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", "postgres", "postgres");
			Statement statement = c.createStatement();
			try {
				statement.executeUpdate("CREATE DATABASE inventory;");
			} catch (Exception e) {
				System.out.println("База данных уже инициализирована!");
			}
			try {
				statement.executeUpdate("CREATE ROLE root WITH LOGIN SUPERUSER PASSWORD 'root';");
			} catch (Exception e) {
				System.out.println("Подключение уже инициализировано!");
			}
		} catch (Exception e) {
			System.out.println("UNABLE TO GET ACCESS TO POSTGRESQL SERVER!");
		}
	}

}
