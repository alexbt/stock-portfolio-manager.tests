package com.proserus.stocks.ui.dbutils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.proserus.stocks.bo.common.DatabasePaths;
import com.proserus.stocks.bp.dao.PersistenceManager;
import com.proserus.stocks.bp.events.SwingEvents;
import com.proserus.stocks.bp.services.DatabaseVersionningBp;
import com.proserus.stocks.ui.SwingModule;
import com.proserus.stocks.ui.controller.ViewControllers;

public class DatabaseVersionningBpImplTest {

	private static final String rogue_constraint = ",CONSTRAINT FK_TRANSACTION_SYMBOL FOREIGN KEY(SYMBOL_ID) REFERENCES PUBLIC.SYMBOL(ID)";

	@Test
	public void test7() throws IOException {
		testUpgrade("0.7Beta");
	}

	@Test
	public void test8() throws IOException {
		testUpgrade("0.8Beta");
	}

	@Test
	public void test9() throws IOException {
		testUpgrade("0.9Beta");
	}

	@Test
	public void test10() throws IOException {
		testUpgrade("0.10Beta");
	}

	@Test
	public void test11() throws IOException {
		testUpgrade("0.11Beta");
	}

	@Test
	public void test12() throws IOException {
		testUpgrade("0.12Beta");
	}

	@Test
	public void test13() throws IOException {
		testUpgrade("0.13Beta");
	}

	@Test
	public void test14() throws IOException {
		testUpgrade("0.14Beta");
	}

	@Test
	public void test15() throws IOException {
		testUpgrade("0.15Beta");
	}

	@Test
	public void test16() throws IOException {
		testUpgrade("0.16Beta");
	}

	@Test
	public void test17() throws IOException {
		testUpgrade("0.17Beta");
	}
	
	//testUpgrade("0.18Beta"); BAD VERSION

	@Test
	public void test19() throws IOException {
		testUpgrade("0.19Beta");
	}

	@Test
	public void test20() throws IOException {
		testUpgrade("0.20Beta");
	}

	@Test
	public void test21() throws IOException {
		testUpgrade("0.21Beta");
	}

	@Test
	public void test22() throws IOException {
		testUpgrade("0.22Beta");
	}

	@Test
	public void test23() throws IOException {
		testUpgrade("0.23Beta");
	}

	private void testUpgrade(String version) throws IOException {
		Injector inject = Guice.createInjector(new SwingModule());
		inject.getInstance(ViewControllers.class);

		DatabasePaths databases = new DatabasePaths();
		removeTmpFiles();

		FileUtils.copyFile(new File("src/test/resources/StockPortfolio_"
				+ version + "/data/db.script"), new File(
				"src/test/resources/tmpdb.script"));
		databases.setSelectedDatabase("src/test/resources/tmpdb");
		SwingEvents.DATABASE_SELECTED.fire(databases);

		inject.getInstance(DatabaseVersionningBp.class).setIgnorePopop(true);
		ViewControllers.getController().checkDatabaseVersion();
		inject.getInstance(PersistenceManager.class).close();

		assertEquals(7, compareDb("src/test/resources/tmpdb.script"));
		System.out.println("version " + version + " tested");
		removeTmpFiles();
	}

	private void removeTmpFiles() {
		new File("src/test/resources/tmpdb.script").delete();
		new File("src/test/resources/tmpdb.properties").delete();
		new File("src/test/resources/tmpdb.log").delete();
		new File("src/test/resources/config.properties").delete();
		new File("src/test/resources/version.properties").delete();
	}

	private int compareDb(String path) throws IOException {
		String oldVersion = FileUtils.readFileToString(new File(path));
		oldVersion = oldVersion.replaceAll("\r", "");
		
		String good = FileUtils.readFileToString(new File(
				"src/test/resources/expected.db.script"));
		good = good.replaceAll("\r", "");
		int i = 0;
		String[] goods = good.split("\n");
		String[] oldVersions = oldVersion.split("\n");
		Arrays.sort(goods);
		Arrays.sort(oldVersions);
		for (String line : oldVersions) {
			if (line.startsWith("CREATE MEMORY TABLE")) {
				if(!line.equals(goods[i]) && line.equals(goods[i].replace(rogue_constraint, ""))){
					assertEquals(goods[i++].replace(rogue_constraint, ""), line);  
				}else{
					assertEquals(goods[i++], line);
				}
			}
		}
		return i;
	}
}
