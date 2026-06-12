package com.example.itstore.model;

import java.io.Serializable;
import java.util.List;

public class VariantReviewsResponse implements Serializable {
    private boolean success;
    private List<ProductReviewsResponse.ReviewDetail> data;
    private PaginationInfo pagination;

    public boolean isSuccess() { return success; }
    public List<ProductReviewsResponse.ReviewDetail> getData() { return data; }
    public PaginationInfo getPagination() { return pagination; }

    public static class PaginationInfo implements Serializable {
        private int page;
        private int limit;
        private int total;
        private int total_pages;

        public int getPage() { return page; }
        public int getLimit() { return limit; }
        public int getTotal() { return total; }
        public int getTotalPages() { return total_pages; }
    }
}