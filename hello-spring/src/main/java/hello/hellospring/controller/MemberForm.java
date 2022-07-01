package hello.hellospring.controller;

public class MemberForm {
    private String name; // form태그내부 input태그의 name과 동일해야함

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
