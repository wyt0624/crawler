package com.surfilter.util;

import java.util.ArrayList;
import java.util.List;

public class Node {

    public Integer state;
    public char character = 0;
    public Node failureNode;
    public List<Keyword> keywords;
    public List<Node> childrenList;

    public Node() {
        keywords = new ArrayList<Keyword>();
        childrenList = new ArrayList<Node>();
        state = 0;
        failureNode = null;
        character = 0;
    }

    public Node(char c, Node node) {
        keywords = new ArrayList<Keyword>();
        childrenList = new ArrayList<Node>();
        state = 1;
        character = c;
        failureNode = node;
    }

    public Boolean containsChild(char c) {
        for (Node childNode : childrenList) {
            if (childNode.character == c) return true;
        }
        return false;
    }

    public Node getChild(char c) {
        for (Node childNode : childrenList) {
            if (childNode.character == c) return childNode;
        }
        return null;
    }

    public void addKeyword(Keyword keyword) {
        keywords.add( keyword );

    }

    public void addKeywords(List<Keyword> k) {
        keywords.addAll( k );
    }

    public void addChild(Node child) {
        childrenList.add( child );
    }
}
