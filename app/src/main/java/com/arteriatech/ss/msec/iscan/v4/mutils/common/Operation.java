package com.arteriatech.ss.msec.iscan.v4.mutils.common;


public enum Operation {
	DeleteErrorArchive(90),
	GetRequest(80),
	UploadFileLogs(70),
	GetStoreOpen(60),
	Create(10),
	Delete(20),
	Update(30),
	OfflineFlush(40),
	OfflineRefresh(50);



	private final int value;
	
	Operation(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
}
