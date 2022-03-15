package com.janey668.c1;

import java.util.ArrayList;
import java.util.List;

public class Test4 {

    public static void main(String[] args) {
        C c1 = new C();
        C c2 = new C();
        System.out.println(c1.s.equals(c2.s));
        System.out.println(c1.list.equals(c2.list));
    }

    static class C {
        public S s = new S();
        public List list = new ArrayList();
        public S get() {
            return s;
        }
    }

    static class S {

    }
}
