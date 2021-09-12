package com.epam.esm.interceptor;

import com.epam.esm.controller.TagController;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        Link link = linkTo(methodOn(TagController.class).getById(1)).withSelfRel();
        request.setAttribute("_self", link);
        Link deleteTag = linkTo(methodOn(TagController.class).deleteById(1)).withRel("deleteTag");
        request.setAttribute("deleteTag", deleteTag);
        Link topUserTopTag = linkTo(methodOn(TagController.class).findTopUserTopTag()).withRel("topUserTopTag");
        request.setAttribute("topUserTopTag", topUserTopTag);
    }
}
