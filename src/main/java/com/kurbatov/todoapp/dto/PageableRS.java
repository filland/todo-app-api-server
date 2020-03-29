package com.kurbatov.todoapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Used as a response to requests with paging
 *
 * @param <T> is the resource type that is being fetched
 */
public class PageableRS<T> {

    @JsonProperty("list")
    private List<T> list;

    @JsonProperty("total")
    private int totalPages;

    @JsonProperty("page")
    private int page;

    @JsonProperty("size")
    private int size;

    public PageableRS() {
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
