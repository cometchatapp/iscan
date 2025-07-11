package com.arteriatech.ss.msec.iscan.v4.reports.returnorder.create;

interface ROFilterPresenter {
    void loadCategory(final String brand);
    void loadBrands(final String previousBrandId, final String previousCategoryId);
    void loadCRSSKU(final String previousBrandId, final String previousCategoryId);
    void onDestroy();
}
