package com.nginx.app.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rev-prox")
public class RevProxController {

  String hostname = System.getenv("HOSTNAME");

  @GetMapping("/prox-1")
  public String myEndPoint01() {

    return "Load Balancer 01: %s".formatted(hostname);
  }

  @GetMapping("/prox-2")
  public String myEndPoint02() {

    return "Load Balancer 02: %s".formatted(hostname);
  }
}