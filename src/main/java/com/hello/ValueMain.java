package com.hello;

import com.hello.entity.Address;

public class ValueMain {

    public static void main(String[] args) {
        // << 값 타입의 비교 >>
        // 기본 자료형에서 == 비교 : 값 자체를 비교
        int a = 10;
        int b = 10;
        System.out.println("(a == b) = " + (a == b)); // true

        // 1. 참조 자료형에서 == 비교 (= 동일성 비교) : 인스턴스의 참조 값을 비교
        Address address1 = new Address("city", "street", "zipcode");
        Address address2 = new Address("city", "street", "zipcode");
        Address address3 = address1;
        System.out.println("(address1 == address2) = " + (address1 == address2)); // false (참조하는 주소 값이 다름)
        System.out.println("(address1 == address3) = " + (address1 == address3)); // true (참조하는 주소 값이 같음)

        // 2. 참조 자료형에서 equals() 비교 (= 동등성 비교) : 인스턴스의 값을 비교
        // 값 타입은 equals()를 사용해서 동등성 비교를 해야 한다.
        // 단, equals()의 기본 동작 방식이 == 비교이기 때문에, 오버라이딩을 통해 적절하게 재정의해야 한다.
        //  => equals() 재정의 시 hashCode() 또한 같이 재정의를 해준다.
        System.out.println("(address1.equals(address2)) = "
                + (address1.equals(address2))); // 재정의 전에는 false, 재정의 후에는 true
    }
}
