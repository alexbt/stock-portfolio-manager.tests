package com.proserus.stocks.ui.dbutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.proserus.stocks.bo.common.Database;
import com.proserus.stocks.bo.common.DatabasePaths;
import com.proserus.stocks.bp.dao.PersistenceManager;
import com.proserus.stocks.bp.events.ModelChangeEvents;
import com.proserus.stocks.bp.services.DatabaseVersionningBp;
import com.proserus.stocks.ui.SwingModule;
import com.proserus.stocks.ui.controller.ViewControllers;

public class DatabaseVersionningBpImplTest {

	private static final int NB_EXPECTED_COMPARED_EQUALS = 7;
	private static final String EMPTY = "";
	private static final String FK_TRANSACTION_SYMBOL_ALTER = "ALTER TABLE PUBLIC.TRANSACTION ADD CONSTRAINT FK_TRANSACTION_SYMBOL FOREIGN KEY(SYMBOL_ID) REFERENCES PUBLIC.SYMBOL(ID)";
	private static final String FK_TRANSACTION_SYMBOL_CONSTRAINT = ",CONSTRAINT FK_TRANSACTION_SYMBOL FOREIGN KEY(SYMBOL_ID) REFERENCES PUBLIC.SYMBOL(ID)";

	private int compareDb(String dbPathBeingUpdated, String expectedResultFile) throws IOException {
		String dbBeingUpdated = FileUtils.readFileToString(new File(dbPathBeingUpdated));
		dbBeingUpdated = dbBeingUpdated.replaceAll("\r", EMPTY);

		String expectedResult = FileUtils.readFileToString(new File(expectedResultFile));
		expectedResult = expectedResult.replaceAll("\r", EMPTY);
		int nbEquals = 0;
		String[] expectedLines = expectedResult.split("\n");
		assertFalse(dbBeingUpdated.contains("aced0005737200"));// VARBINARY
																// entry..
		assertTrue(dbBeingUpdated.contains("INSERT INTO VERSION VALUES(1,24)"));
		assertTrue(dbBeingUpdated.contains("INSERT INTO VERSION VALUES(1,24)"));
		assertTrue(dbBeingUpdated.contains("INSERT INTO SYMBOL VALUES(-1,'0','','Error','UNKNOWN','ERR')"));
		assertTrue(dbBeingUpdated.contains("YEAR INTEGER NOT NULL"));

		boolean specialCaseForForeingKeyTransactionSymbol = dbBeingUpdated.contains(FK_TRANSACTION_SYMBOL_ALTER);

		String[] dbBeingUpdatedLines = dbBeingUpdated.split("\n");
		Arrays.sort(expectedLines);
		Arrays.sort(dbBeingUpdatedLines);
		for (String updatedLine : dbBeingUpdatedLines) {
			if (updatedLine.startsWith("CREATE MEMORY TABLE")) {
				if (!updatedLine.equals(expectedLines[nbEquals]) && specialCaseForForeingKeyTransactionSymbol) {
					expectedLines[nbEquals] = expectedLines[nbEquals].replace(FK_TRANSACTION_SYMBOL_CONSTRAINT, EMPTY);
				}
				assertEquals(expectedLines[nbEquals], updatedLine);
				nbEquals++;
			}
		}
		return nbEquals;
	}

	private void removeTmpFiles() {
		new File("src/test/resources/tmpdb.script").delete();
		new File("src/test/resources/tmpdb.properties").delete();
		new File("src/test/resources/tmpdb.log").delete();
		new File("src/test/resources/config.properties").delete();
		new File("src/test/resources/version.properties").delete();
	}

	@Test
	public void test07() throws IOException {
		testUpgrade("0.7Beta");
	}

	@Test
	public void test08() throws IOException {
		testUpgrade("0.8Beta");
	}

	@Test
	public void test09() throws IOException {
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

	// testUpgrade("0.18Beta"); BAD VERSION

	@Test
	public void test15WithNull() throws IOException {
		testUpgrade("0.15BetaWithNullType");
	}

	@Test
	public void test16() throws IOException {
		testUpgrade("0.16Beta");
	}

	@Test
	public void test17() throws IOException {
		testUpgrade("0.17Beta");
	}

	@Test
	public void test18() throws IOException {
		assertTrue("Bad release, skipped test", true);
	}

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

	@Test
	public void test23WithNull() throws IOException {
		testUpgrade("0.23BetaWithNullType");
	}

	@Test
	public void test23WithUnknownSymbol() throws IOException {
		testUpgrade("0.23BetaWithUnknownSymbol");
	}

	private void testUpgrade(String version) throws IOException {
		Injector inject = Guice.createInjector(new SwingModule());
		inject.getInstance(ViewControllers.class);

		DatabasePaths databases = new DatabasePaths();
		removeTmpFiles();

		FileUtils.copyFile(new File("src/test/resources/StockPortfolio_" + version + "/data/db.script"), new File(
				"src/test/resources/tmpdb.script"));
		databases.setSelectedDatabase(new Database("src/test/resources/tmpdb"));
		ModelChangeEvents.DATABASE_SELECTED.fire(databases);

		inject.getInstance(DatabaseVersionningBp.class).setIgnorePopop(true);
		ViewControllers.getController().checkDatabaseVersion();
		inject.getInstance(PersistenceManager.class).close();

		assertEquals(NB_EXPECTED_COMPARED_EQUALS, compareDb("src/test/resources/tmpdb.script", "src/test/resources/expected.db.script"));
		System.out.println("version " + version + " tested");
		removeTmpFiles();
	}
}
