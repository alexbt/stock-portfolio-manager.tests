package com.proserus.stocks.ui.sikuli;

public enum SikuliImage {
	SELECT_DATABASE("SelectDatabase.png"), //
	GRAPH_TAB("GraphTab.png"), //
	MENU_TOOLBAR_TABS("MenuToolbarTabs.png"), //
	TRANSACTIONTAB_DELETE_DISABLED("TransactionTab_DeleteDisabled.png"), //
	TRANSACTIONTAB_DELETE_ENABLED("TransactionTab_DeleteEnabled.png"), //
	TRANSACTIONTAB_CONFIRMDELETE("TransactionTab_ConfirmDelete.png"), //
	SYMBOLTAB_CONFIRM_DELETE("SymbolTab_ConfirmDelete.png"), //
	SYMBOLTAB_DELETE_ENABLED("SymbolTab_DeleteEnabled.png"), //
	SYMBOLTAB_DELETE_DISABLED("SymbolTab_DeleteDisabled.png"), //
	ADDTRANSACTIONPANEL("AddTransactionPanel.png"), //
	TRANSACTIONTAB_TESTENTRY_GRAY("TransactionTab_TestEntry_Gray.png");//

	private String image;

	public String getName() {
		return image;
	}

	private SikuliImage(String image) {
		this.image = image;

	}

}
