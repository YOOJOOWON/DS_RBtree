import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class BST {
    private final boolean RED = true;
    private final boolean BLACK = false;
    private final Node nil = new Node(-1);

    public Node root = nil;

    int total;
    int nb;

    class Node {
        public int val;
        public Node left = nil, right = nil, parent = nil;
        public boolean color = BLACK;
        public Node(int newval) {
            val = newval;
        }
    }

    public BST() {
    }

    public void insert(Node tree, int n) {
        Node z = new Node(n);
        Node y = nil;
        Node x = root;

        while (x != nil) {
            y = x;
            if (z.val < x.val)
                x = x.left;
            else x = x.right;
        }
        z.parent = y;

        if (y == nil) {
            root = z;
            z.color = BLACK;
            z.parent = nil;
        } else if (z.val < y.val)
            y.left = z;
        else y.right = z;
        z.left = nil;
        z.right = nil;
        z.color = RED;
        insert_fixup(z);
    }
    
    public void insert_fixup(Node z) {
        while (z.parent.color == RED) {
            Node y = nil;
            if (z.parent == z.parent.parent.left) {
                y = z.parent.parent.right;

                if (y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                    continue;
                }
                else if (z == z.parent.right) {
                    z = z.parent;
                    left_rotate(z);
                }
                z.parent.color = BLACK;
                z.parent.parent.color = RED;
                right_rotate(z.parent.parent);
            } else {
                y = z.parent.parent.left;
                if (y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                    continue;
                }
                else if (z == z.parent.left) {
                    z = z.parent;
                    right_rotate(z);
                }
                z.parent.color = BLACK;
                z.parent.parent.color = RED;
                left_rotate(z.parent.parent);
            }
        }
        root.color = BLACK;
    }

    public void left_rotate (Node node){
        if (node.parent == nil) {
            Node right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent = nil;
            root = right;
        }
        else {
            if (node == node.parent.left) {
                node.parent.left = node.right;
            } else {
                node.parent.right = node.right;
            }
            node.right.parent = node.parent;
            node.parent = node.right;
            if (node.right.left != nil) {
                node.right.left.parent = node;
            }
            node.right = node.right.left;
            node.parent.left = node;
        }
    }

    public void right_rotate (Node node){
        if (node.parent == nil) {
            Node left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = nil;
            root = left;
            }
        else {
            if (node == node.parent.left) {
                node.parent.left = node.left;
            } else {
                node.parent.right = node.left;
            }
            node.left.parent = node.parent;
            node.parent = node.left;
            if (node.left.right != nil) {
                node.left.right.parent = node;
            }
            node.left = node.left.right;
            node.parent.right = node;
        }
    }

    void transplant (Node u, Node v){
        if(u.parent == nil){
            root = v;
        }
        else if(u == u.parent.left){
            u.parent.left = v;
        }
        else
            u.parent.right = v;
        v.parent = u.parent;
    }

    Node min_value(Node tree){
        while (tree.left != nil)
            tree = tree.left;
        return tree;
    }

    public void delete (Node tree, int n) {
        Node z;
        if((z = search(root, n))==null) return;
        Node x;
        Node y = z;
        boolean y_original_color = y.color;

        if(z.left == nil){
            x = z.right;
            transplant(z, z.right);
        }else if(z.right == nil){
            x = z.left;
            transplant(z, z.left);
        }else{
            y = min_value(z.right);
            y_original_color = y.color;
            x = y.right;
            if(y.parent == z)
                x.parent = y;
            else{
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if(y_original_color==BLACK)
            delete_fixup(x);
        return;
    }

    public void delete_fixup (Node x){
        while(x != root && x.color == BLACK){
            if(x == x.parent.left){
                Node w = x.parent.right;
                if(w.color == RED){
                    w.color = BLACK;
                    x.parent.color = RED;
                    left_rotate(x.parent);
                    w = x.parent.right;
                }
                if(w.left.color == BLACK && w.right.color == BLACK){
                    w.color = RED;
                    x = x.parent;
                    continue;
                }
                else if(w.right.color == BLACK){
                    w.left.color = BLACK;
                    w.color = RED;
                    right_rotate(w);
                    w = x.parent.right;
                }
                if(w.right.color == RED){
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    left_rotate(x.parent);
                    x = root;
                }
            }else{
                Node w = x.parent.left;
                if(w.color == RED){
                    w.color = BLACK;
                    x.parent.color = RED;
                    right_rotate(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == BLACK && w.left.color == BLACK){
                    w.color = RED;
                    x = x.parent;
                    continue;
                }
                else if(w.left.color == BLACK){
                    w.right.color = BLACK;
                    w.color = RED;
                    left_rotate(w);
                    w = x.parent.left;
                }
                if(w.left.color == RED){
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    right_rotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    public Node search (Node tree, int val) {
        if (tree == nil)
            return null;

        if (val == tree.val)
            return tree;
        else if (val < tree.val) {
            if (tree.left != nil)
                return search(tree.left, val);
        }
        else if (val > tree.val) {
            if (tree.right != nil)
                return search(tree.right, val);
        }
        return null;
    }

    public void print(Node tree, int level) {
        if (tree.right != nil)
            print(tree.right, level + 1);
        for(int i = 0; i < level; i++)
            System.out.print("    ");
        System.out.print(tree.val);
        if (tree.color)
            System.out.print("R");
        else
            System.out.print("B");
        System.out.println();
        if (tree.left != nil)
            print(tree.left, level + 1);
    }
    
    public void inorder(Node tree) {
        if (tree == nil)
            return;
        else {
            inorder(tree.left);
            System.out.println(tree.val);
            inorder(tree.right);
        }
    }
    
    int black_height () {
        int level = 0;
        Node n = root;
        while (n != nil) {
            if (n.color == BLACK)
                level++;
            n = n.right;
        }
        return level;
    } // calculate bh

    void numbering (Node tree) {
        if (tree == root) {
            total = 0;
            nb = 0;
        }
        if (tree == nil)
            return;
        else {
            numbering(tree.left);
            total ++;
            if (tree.color == BLACK)
                nb++;
            numbering(tree.right);
        }
    } // calculate total & nb
}

public class DataStructure {
    public static void main(String [] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(".\\input.txt"));
        BST bst = new BST();
        int l;

        while (true) {
            String line = br.readLine();
            line = onlyNum(line);
            l = Integer.parseInt(line);
            if (l > 0)
                bst.insert(bst.root, l);
            else if (l < 0){
                l = Math.abs(l);
                bst.delete(bst.root, l);
            }
            else if (l == 0)
                break;
        }

        bst.numbering(bst.root);

        System.out.println("total = " + bst.total);
        System.out.println("nb = " + bst.nb);
        System.out.println("bh = " + bst.black_height());
        bst.inorder(bst.root);
        System.out.println();
    }

    public static String onlyNum(String str) {
        if ( str == null ) return "";
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < str.length(); i++){
            if( Character.isDigit( str.charAt(i) ) || str.charAt(i) == '-' ) {
                sb.append( str.charAt(i) );
            }
        }
        return sb.toString();
    }
}