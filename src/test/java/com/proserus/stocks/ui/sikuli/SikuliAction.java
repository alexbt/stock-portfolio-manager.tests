package com.proserus.stocks.ui.sikuli;

public enum SikuliAction {
	SELECT_DATABASE_OK(SikuliImage.SELECT_DATABASE, 250, 70, 300, 95), //
	MENU_TOOLBAR_TABS_TRANSACTION_TAB(SikuliImage.MENU_TOOLBAR_TABS, 170, 65, 270, 80), //
	MENU_TOOLBAR_TABS_SYMBOL_TAB(SikuliImage.MENU_TOOLBAR_TABS, 265, 65, 395, 80), //
	TRANSACTIONTAB_FIRSTROW_NONE_SELECTED(SikuliImage.TRANSACTIONTAB_DELETE_DISABLED, 380, 142, 500, 142), //
	TRANSACTIONTAB_DELETE(SikuliImage.TRANSACTIONTAB_DELETE_ENABLED, 0, 85, 55, 120), //
	TRANSACTIONTAB_CONFIRMDELETE_OK(SikuliImage.TRANSACTIONTAB_CONFIRMDELETE, 200, 75, 235, 100), //
	ADD_TRANSACTION_PANEL_ADDCLOSE(SikuliImage.ADDTRANSACTIONPANEL, 310, 260, 410, 285), //
	ADD_TRANSACTION_PANEL_COMMISSION(SikuliImage.ADDTRANSACTIONPANEL, 265, 135, 375, 160), //
	SYMBOLTAB_FIRSTROW_NONE_SELECTED(SikuliImage.SYMBOLTAB_DELETE_DISABLED, 335, 146, 500, 146), //
	SYMBOLTAB_CONFIRMDELETE_OK(SikuliImage.SYMBOLTAB_CONFIRM_DELETE, 100, 80, 140, 100), //
	SYMBOLTAB_DELETE(SikuliImage.SYMBOLTAB_DELETE_ENABLED, 0, 85, 55, 120), //
	ADD_TRANSACTION_PANEL_SYMBOL(SikuliImage.ADDTRANSACTIONPANEL, 10, 60, 100, 85), //
	ADD_TRANSACTION_PANEL_NAME(SikuliImage.ADDTRANSACTIONPANEL, 165, 60, 315, 85), //
	ADD_TRANSACTION_PANEL_PRICE(SikuliImage.ADDTRANSACTIONPANEL, 10, 130, 122, 155), //
	ADD_TRANSACTION_PANEL_QUANTITY(SikuliImage.ADDTRANSACTIONPANEL, 140, 135, 250, 160), //
	MENU_TOOLBAR_TABS_ADDTRANSACTION(SikuliImage.MENU_TOOLBAR_TABS, 10, 20, 50, 55);//

	private SikuliImage image;
	private int leftx;
	private int lefty;
	private int width;
	private int height;

	public SikuliImage getImage() {
		return image;
	}

	public int getLeftx() {
		return leftx;
	}

	public int getLefty() {
		return lefty;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	private SikuliAction(SikuliImage image, int leftx, int lefty, int rightx, int righty) {
		this.image = image;
		this.leftx = leftx;
		this.lefty = lefty;
		this.width = rightx - leftx;
		this.height = righty - lefty;

	}

}
