package com.interview.academian;
class Parent{
    public void friend(){
        System.out.println("friend parent ...");
    }
}
public class ChildTest extends Parent{
    public void friend2(){
        System.out.println("friend2 child ...");
    }

    public void friend(){
        System.out.println("friend child ...");
    }



    public static void main(String[] args) {
       Parent parent = new ChildTest();
       //ChildTest childTest = new Parent();  // child cant hold parent constructor
        parent.friend();

        Parent parent1 = new Parent();
        parent1.friend();
        ChildTest childTest = new ChildTest();
        childTest.friend();
        childTest.friend2();

    }
}
