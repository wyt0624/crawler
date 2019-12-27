package com.surfilter.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Patterns {
    private final Node root = new Node();
    private List<Node> tree;

    public Patterns(List<Keyword> keywords) {
        tree = new ArrayList<Node>();
        root.failureNode = root;
        tree.add( root );
        for (Keyword keyword : keywords) {
            addKeyword( keyword );
        }
        setFailNode();
    }

    private void setFailNode() {
        Queue<Node> queue = new LinkedList<Node>();
        Node node = root;
        for (Node d1 : node.childrenList) {
            queue.offer( d1 );
        }
        while (!queue.isEmpty()) {
            node = queue.poll();
            if (node.childrenList != null) {
                for (Node curNode : node.childrenList) {
                    queue.offer( curNode );
                    Node failNode = node.failureNode;
                    while (!failNode.containsChild( curNode.character )) {
                        failNode = failNode.failureNode;
                        if (failNode == null || failNode.state == 0) break;
                    }
                    if (failNode != null && failNode.containsChild( curNode.character )) {
                        curNode.failureNode = failNode.getChild( curNode.character );
                        curNode.addKeywords( curNode.failureNode.keywords );

                    }

                }
            }
        }
    }

    private void addKeyword(Keyword keyword) {
        char[] wordCharArr = keyword.getWord().toCharArray();
        Node current = root;
        for (char currentChar : wordCharArr) {
            if (current.containsChild( currentChar )) {
                current = current.getChild( currentChar );
            } else {
                Node node = new Node( currentChar, root );
                current.addChild( node );
                current = node;
                tree.add( node );
            }
        }
        current.addKeyword( keyword );

    }

    public Set<Keyword> searchKeyword(String data, Integer category) {
        Set<Keyword> matchResult = new HashSet<>();
        Node node = root;
        char[] chs = data.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            while (node != null && !node.containsChild( chs[i] )) {
                //	if(node.state==0) break;
                node = node.failureNode;
                if (node == null || node.state == 0) break;
            }
            if (node != null && node.containsChild( chs[i] )) {
                node = node.getChild( chs[i] );
                if (node.keywords != null) {
                    for (Keyword pattern : node.keywords) {
                        if (category == null) {
                            //						System.out.println(pattern.getWord());
                            matchResult.add( new Keyword( pattern.getWord() ) );
                        } else {
                            if (pattern.getCategories().contains( category )) {
                                matchResult.add( pattern );
                            }
                        }
                    }
                }
            }
        }
        return matchResult;
    }

}
