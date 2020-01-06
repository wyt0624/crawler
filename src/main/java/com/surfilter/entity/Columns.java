package com.surfilter.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * datatable 请求参数
 */
@Data
public class Columns {
    private Integer draw;
    private Integer start;
    private Integer length;

    private Map<Search,String> search = new HashMap<Search,String>(  );
    private List<Map<Order,String>> order = new ArrayList<Map<Order,String>>(  );
    private List<Map<Column,String>> column = new ArrayList<Map<Column,String>>(  );

    public enum Search {
        value,regex;
        private String content;
        private Search(String va){
            this.content= va;
        }
        private Search(){

        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
    public enum Order {
        column,dir
    }
    public enum Column {
        data,name,searchable,orderable,searchValue,searchRegex
    }
}
