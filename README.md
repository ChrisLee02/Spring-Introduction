# Spring-Introduction

스프링 입문 - 코드로 배우는 스프링 부트, 웹 MVC. DB 접근 기술 강의 레포지토리입니다.

# 인프런 - Spring 입문

0. 스프링 웹 개발 기초

- Controller에서 url에 따라 내용을 매핑해준다. 이때 `@GetMapping`을 이용한다.
- 컨트롤러도 쓰임새에 따라 클래스 단위로 분리해준다. 기준은 링크의 내용으로.
- 정적 html, 동적 template을 String 반환 타입으로 매핑해줄 수 있다. 스프링이 알아서 resource폴더를 뒤져준다. 이때 template의 html파일에 데이터를 다루는 문법을 사용할 수 있도록 하고, 해당 파일을 렌더링하는 것을 템플릿 엔진이 담당, 여기서는 타임리프를 사용하였다.
- `@ResponseBody` `@GetMapping` 아래에 달아주면 HTTP의 BODY에 내용을 바로 반환한다. 이때는 객체를 반환타입으로 잡고, 객체의 변수대로 json을 생성해준다.
- MVC패턴 → model - view - controller : 모델은 데이터를, view는 페이지 구조를, controller는 모델에 접근해서 view에 적절한 데이터를 넘겨준다.


1. 구현 전 의존관계 설계

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/b1c44f9c-ca1d-4262-a619-4e89dbbad058/Untitled.png)

컨트롤러: 웹 MVC의 컨트롤러 역할, 다만 여기서 데이터를 가공하지 않는다. url에 따라, 적당한 데이터를 넘겨주는 역할만 담당한다.
서비스: 핵심 비즈니스 로직 구현, 여기서 데이터를 가공한다.
리포지토리: 데이터베이스에 접근, 도메인 객체를 DB에 저장하고 관리
도메인: 비즈니스 도메인 객체, 예) 회원, 주문, 쿠폰 등등 주로 데이터베이스에 저장하고 관리됨.

이때 컨트롤러는 서비스 객체를 필요로 하고, 서비스는 리포지토리 객체를 필요로 하는 등 화살표로 표현된 필요관계 → 이를 의존성이라고 부른다. 이 의존관계는 Java코드로만 표현해선 안되고, 스프링에 별도로 의존관계를 명시해주어야 한다. → @Autowired

2. 리포지토리의 인터페이스화

객체지향 설계의 장점. 리포지토리를 실질적으로 구현하는 방식은 다양하다. 내부 메모리를 쓰는지,외부 DB를 쓰는지에 따라, 또는 DB에 접근하는 방식에 따라 다를 수 있다. 그러나 기능을 추상화하여 인터페이스로 구현해놓고, 실제 코드에서는 인터페이스 객체로 선언하면 코드를 교체하기 간편하다.

3. 테스트 코드

test/java 아래에 패키지 경로를 맞추고 test파일을 만들어준다. `@SpringBootTest` `@Test` 키워드로 명시가능.

given - when - then 패턴을 따른다.

```java
void 중복_회원_예외() {
    //given when then 패턴을 이용한다

    //given: 어떤 데이터가 들어가있나
    Member member1 = new Member();
    member1.setName("spring");
    Member member2 = new Member();
    member2.setName("spring");

    //when: 무엇을 할 것인가
    memberService.join(member1);
    IllegalStateException e =assertThrows(IllegalStateException.class,
            () -> memberService.join(member2));

    //them: 무엇이 나와야 하는가
assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
}
```

assertThat을 이용해서 테스트의 유효성을 검증함.

`@AfterEach` `@BeforeEach` 키워드로 테스트 전후에 실행되는 코드를 만들 수 있다.

4. 의존성 주입(DI)

의존성을 주입할 대상, 주입당할 대상을 스프링 빈으로 등록한 뒤, @Autowired annotation을 이용한다. 이때 스프링 빈을 등록하는 방법은 2가지가 있음.

- 컴포넌트 스캔과 자동 의존관계 설정 → Controller, Service, Repository 등은 컴포넌트 내장.
- 자바 코드로 직접 스프링 빈 등록하기 → SpringConfig라는 별도 클래스에 `@Configuration`를 붙여준다. `@Bean`키워드로

```java
@Bean
public MemberService memberService(){
    return new MemberService(memberRepository);
};
```

이렇게 등록한다.

- 스프링 빈 방식의 장점은 코드 교체가 용이하다는 점이다.
- 보통 정형화된 놈들은 컴포넌트를, 그렇지 않거나 코드를 교체해야 되면 스프링 빈을 이용.

5. 기능 구현

- <form> 태그의 method 값을 post로 두면 HTTP 요청을 post로 보낼 수 있음.
- @GetMapping 말고, PostMapping으로 이를 처리할 수 있음.
- “redirect:{경로}”로 리디렉션 처리 가능.

6. 실제 DB 접근 기술

- JdbcTemplate OR JPA 이용.
- 데이터 객체를 @Entity로 등록하고, id로 쓸 private 변수 위에 @id 등록 EntityManager 객체를 이용해서 DB에 접근한다.
- id에 대해서는 em.find 사용 가능, 나머지는 createQuery를 이용한다.
- 스프링 데이터 JPA를 이용하면 함수명만 지어주면 구현을 자동으로 해준다.

7. AOP

- Aspect Oriented Programming: 공통적인 관심 사항과 핵심적인 관심 사항을 분리한다.
- 위에서 구현한 기능들이 핵심 관심 사항이라면, 각 기능들의 소요 시간을 측정하는 것은 공통 관심 사항에 해당한다.
- 개별적인 코드에 소요 시간 측정 로직을 집어넣기보단, 시간을 측정하는 로직을 분리한 뒤 주입하는 방식으로 해결한다. @Aspect로 명시, @Around("execution(* hello.hellospring..*(..))")로 실행 대상 명시
