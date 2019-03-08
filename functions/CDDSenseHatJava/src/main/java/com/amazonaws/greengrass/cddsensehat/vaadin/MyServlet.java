package com.amazonaws.greengrass.cddsensehat.vaadin;

import com.vaadin.guice.annotation.Configuration;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "Guice-Vaadin-Servlet", urlPatterns = "/*")
@Configuration(basePackages = {"com.amazonaws.greengrass.cddsensehat.vaadin"})
public class MyServlet extends com.vaadin.guice.server.GuiceVaadinServlet {
}