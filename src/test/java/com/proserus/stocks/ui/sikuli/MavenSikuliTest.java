package com.proserus.stocks.ui.sikuli;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Keyboard;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.api.robot.desktop.DesktopMouse;

import com.google.inject.Guice;
import com.proserus.stocks.bo.common.Database;
import com.proserus.stocks.bo.common.DatabasePaths;
import com.proserus.stocks.bo.utils.PathUtils;
import com.proserus.stocks.bp.dao.PersistenceManager;
import com.proserus.stocks.bp.events.Event;
import com.proserus.stocks.bp.events.EventListener;
import com.proserus.stocks.bp.events.ModelChangeEvents;
import com.proserus.stocks.ui.Launch;
import com.proserus.stocks.ui.SwingModule;
import com.proserus.stocks.ui.controller.ViewControllers;

public class MavenSikuliTest implements EventListener {

	private static final String LOCATION = "src/test/resources/sikuli/";
	ScreenRegion s = new DesktopScreenRegion();
	Mouse mouse = new DesktopMouse();
	Keyboard keyboard = new DesktopKeyboard();
	private DatabasePaths dbPath;

	@Test
	// @Ignore
	public void test() throws IOException, InterruptedException, SecurityException, NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {

		String currentDir = new File(".").getAbsolutePath();
		currentDir = currentDir.substring(0, currentDir.indexOf("stock-portfolio-manager.tests"));
		currentDir = currentDir + "stock-portfolio-manager.ui";
		new PathUtils();
		Field f = PathUtils.class.getDeclaredField("installationFolder");
		f.setAccessible(true);
		f.set(null, currentDir);

		f = PathUtils.class.getDeclaredField("absolutePath");
		f.setAccessible(true);
		f.set(null, currentDir);

		Guice.createInjector(new SwingModule()).getInstance(ViewControllers.class);
		PersistenceManager pm = ViewControllers.getController().getPersistenceManager();
		Field emField = pm.getClass().getDeclaredField("em");
		Field dbPathField = pm.getClass().getDeclaredField("dbPath");
		dbPath = new DatabasePaths();
		dbPath.setBinaryCurrentFolder(currentDir);
		dbPath.setOsCurrentFolder(currentDir);
		Database db = new Database(currentDir + "/data/db");
		dbPath.addDb(db);
		dbPath.setSelectedDatabase(db);
		dbPathField.setAccessible(true);
		dbPathField.set(pm, dbPath);

		EntityManager em = pm.createEntityManager();
		emField.setAccessible(true);
		emField.set(pm, em);

		// EventBus.getInstance().add(this,
		// ModelChangeEvents.DATABASE_SELECTED);

		new Thread() {
			@Override
			public void run() {
				Launch.main(new String[] { null });
			}
		}.start();

		Thread.sleep(6000);
		ModelChangeEvents.DATABASE_SELECTED.fire(dbPath);
		// Thread.sleep(6000);
		// s.wait(new ImageTarget(new File(LOCATION + "SelectDatabase.png")),
		// 10);

		clickIfPossible(SikuliAction.SELECT_DATABASE_OK);

		click(SikuliImage.GRAPH_TAB);
		click(SikuliAction.MENU_TOOLBAR_TABS_TRANSACTION_TAB);// transactionTab
		click(SikuliAction.TRANSACTIONTAB_FIRSTROW_NONE_SELECTED);// transaction
																	// table
		if (isFound(SikuliImage.TRANSACTIONTAB_DELETE_ENABLED)) {
			click(SikuliAction.TRANSACTIONTAB_DELETE);

			while (isFound(SikuliImage.TRANSACTIONTAB_CONFIRMDELETE)) {
				click(SikuliAction.TRANSACTIONTAB_CONFIRMDELETE_OK);
				// transaction table
				click(SikuliAction.TRANSACTIONTAB_FIRSTROW_NONE_SELECTED);
				click(SikuliAction.TRANSACTIONTAB_DELETE);
			}
		}

		click(SikuliAction.MENU_TOOLBAR_TABS_SYMBOL_TAB);// symbolTab

		click(SikuliAction.SYMBOLTAB_FIRSTROW_NONE_SELECTED);
		if (isFound(SikuliImage.SYMBOLTAB_DELETE_ENABLED)) {
			click(SikuliAction.SYMBOLTAB_DELETE);
			while (isFound(SikuliImage.SYMBOLTAB_CONFIRM_DELETE)) {
				click(SikuliAction.SYMBOLTAB_CONFIRMDELETE_OK);
				click(SikuliAction.SYMBOLTAB_FIRSTROW_NONE_SELECTED);
				click(SikuliAction.SYMBOLTAB_DELETE);
			}
		}

		// add transaction
		click(SikuliAction.MENU_TOOLBAR_TABS_ADDTRANSACTION);

		Thread.sleep(1000);
		// SymbolField
		typeText(SikuliAction.ADD_TRANSACTION_PANEL_SYMBOL, "test");
		// name
		typeText(SikuliAction.ADD_TRANSACTION_PANEL_NAME, "Test Company Name");
		// price
		typeText(SikuliAction.ADD_TRANSACTION_PANEL_PRICE, "9.99");

		// qty
		typeText(SikuliAction.ADD_TRANSACTION_PANEL_QUANTITY, "45");

		// comm
		typeText(SikuliAction.ADD_TRANSACTION_PANEL_COMMISSION, "4.99");

		// add & close
		click(SikuliAction.ADD_TRANSACTION_PANEL_ADDCLOSE);
		// keyboard.keyDown(Key.SPACE);
		// keyboard.keyUp(Key.SPACE);

		click(SikuliAction.MENU_TOOLBAR_TABS_TRANSACTION_TAB);

		find("TransactionTab_TestEntry_Gray.png");

	}

	private void click(SikuliImage sikuliImage) {
		ScreenRegion r = s.find(new ImageTarget(new File(LOCATION + sikuliImage.getName())));
		mouse.click(r.getCenter());
	}

	private void click(SikuliAction sikuliEnum) {
		ScreenRegion r = s.find(new ImageTarget(new File(LOCATION + sikuliEnum.getImage().getName())));
		r = r.getRelativeScreenRegion(sikuliEnum.getLeftx(), sikuliEnum.getLefty(), sikuliEnum.getWidth(), sikuliEnum.getHeight());
		mouse.click(r.getCenter());
	}

	private void clickIfPossible(SikuliAction sikuliEnum) {
		ScreenRegion r = s.find(new ImageTarget(new File(LOCATION + sikuliEnum.getImage().getName())));
		if (r != null) {
			r = r.getRelativeScreenRegion(sikuliEnum.getLeftx(), sikuliEnum.getLefty(), sikuliEnum.getWidth(), sikuliEnum.getHeight());
			mouse.click(r.getCenter());
		}
	}

	private void typeText(SikuliAction sikuliEnum, String text) {
		ScreenRegion r = s.find(new ImageTarget(new File(LOCATION + sikuliEnum.getImage().getName())));
		r = r.getRelativeScreenRegion(sikuliEnum.getLeftx(), sikuliEnum.getLefty(), sikuliEnum.getWidth(), sikuliEnum.getHeight());
		mouse.click(r.getCenter());
		keyboard.type(text);

	}

	private void find(String image) {
		ScreenRegion r = s.find(new ImageTarget(new File(LOCATION + image)));
		assertTrue(r != null);
	}

	private boolean isFound(SikuliImage sikuliImage) {
		ScreenRegion r = s.find(new ImageTarget(new File(LOCATION + sikuliImage.getName())));
		return r != null;// && r.getScore() > 0.96d;
	}

	@Override
	public void update(Event event, Object model) {
		if (!ModelChangeEvents.DATABASE_SELECTED.resolveModel(model).equals(dbPath))
			ModelChangeEvents.DATABASE_SELECTED.fire(dbPath);
	}
}
