package hello.hellospring.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
    @GetMapping("hello") // url 주소를 받아옴. 기본적으로 접속할 때는 get 요청이다.
    public String hello(Model model) {
        model.addAttribute("data", "hello!!");
        return "hello"; // template의 hello.html을 렌더링 + model을 넘겨준다.
    }

    @GetMapping("hello-mvc") // url 주소를 받아옴. 기본적으로 접속할 때는 get 요청이다.
    public String helloMvc(@RequestParam(name = "name", required = false) String name, Model model) {
        model.addAttribute("name", name);
        return "hello-template"; // template의 hello.html을 렌더링 + model을 넘겨준다.
    }

    @GetMapping("hello-string")
    @ResponseBody
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name;
    }

    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloＡpi(@RequestParam("name") String name) {
        return new Hello(name);
    }

    static class Hello {
        private String name;

        public Hello(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
