package com.cloutteam.samjakob.gui.config;

public class PaginatedInventoryConfiguration extends InventoryConfiguration {

    // The message to display when the user tries to visit a page before the first.
    private String noPreviousPages = "There are no previous pages.";
    // The message to display when the user tries to visit a page after the last.
    private String noAdditionalPages = "There are no additional pages.";

    // The label for the previous page button.
    private String previousPage = "&c&lPrevious Page";
    // The label for the current page button.
    private String currentPage = "&c&lPage {currentPage} of {maxPages}";
    // The label for the next page button.
    private String nextPage = "&c&lNext Page";

    public PaginatedInventoryConfiguration(
            String chatPrefix,
            String noPreviousPages,
            String noAdditionalPages,

            String previousPage,
            String currentPage,
            String nextPage
    ){
        super(chatPrefix);
        this.noPreviousPages = noPreviousPages;
        this.noAdditionalPages = noAdditionalPages;

        this.previousPage = previousPage;
        this.currentPage = currentPage;
        this.nextPage = nextPage;
    }

    public PaginatedInventoryConfiguration(
            String noPreviousPages,
            String noAdditionalPages,

            String previousPage,
            String currentPage,
            String nextPage
    ){
        this.noPreviousPages = noPreviousPages;
        this.noAdditionalPages = noAdditionalPages;

        this.previousPage = previousPage;
        this.currentPage = currentPage;
        this.nextPage = nextPage;
    }

    public PaginatedInventoryConfiguration(){

    }


    public String getNoPreviousPages() {
        return noPreviousPages;
    }

    public void setNoPreviousPages(String noPreviousPages) {
        this.noPreviousPages = noPreviousPages;
    }

    public String getNoAdditionalPages() {
        return noAdditionalPages;
    }

    public void setNoAdditionalPages(String noAdditionalPages) {
        this.noAdditionalPages = noAdditionalPages;
    }

    public String getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(String previousPage) {
        this.previousPage = previousPage;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }
}

