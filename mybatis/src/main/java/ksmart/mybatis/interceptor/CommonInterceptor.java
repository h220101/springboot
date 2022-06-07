package ksmart.mybatis.interceptor;

import java.util.Set;
import java.util.StringJoiner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class CommonInterceptor implements HandlerInterceptor{
   
   
   private static final Logger log = LoggerFactory.getLogger(CommonInterceptor.class); //logger 자동완성

   /**
    * HandlerMapping이 핸들러 객체를 결정한 후 HandlerAdapter가 핸드러를 호출하기전에 호출되는 메서드
    * return true : controller 진입o , false : controller 진입x
    * 
    * */
   @Override
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
         throws Exception {
      
      Set<String> paramKey = request.getParameterMap().keySet();
      
      StringJoiner param = new StringJoiner(", ");
      
      if(paramKey != null) {
         for(String key : paramKey) {
            param.add(key + ":" + request.getParameter(key));
         }
      }
      
      log.info("AccessInfo===============================================");
      log.info("PORT          ::::          {}", request.getServerPort());
      log.info("SEVERNAME          ::::          {}", request.getServerName());
      log.info("URI          ::::          {}", request.getRequestURI());
      log.info("HTTPMETHOD          ::::          {}", request.getMethod()); //get,post방식 구분 추가
      if(param != null) log.info("PARAMETER          ::::          {}", param);         
      
      log.info("preHandle===============================================end");
      
      
      return HandlerInterceptor.super.preHandle(request, response, handler);
   }
   
   /**
    * HandlerAdapter가 실제로 핸드러를 호출한 후 DispatcherServlet 이 뷰를 렌더링하기전에 호출되는 메서드
    * 
    * */
   
   @Override
   public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
         ModelAndView modelAndView) throws Exception {
      log.info("postHandle===============================================");
      
      HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
   }
   
   /**
    * DispatcherServlet이 뷰를 렌더링 한 후 호출되는 메서드
    * */
   
   @Override
   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
         throws Exception {
      log.info("afterCompletion===============================================");
   
      HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
   }
}