<%@page pageEncoding="utf-8"%>

   <img src="images/logo.png" alt="logo" class="left" />
   <!-- EL默認從四個隱含對象中取值:
       page-request--session--application
             不包括cookie，且cookie就是不是隱含對象
             要想用el訪問cookie，其語法是固定的，如下：--- -->
   <%-- <span>${cookie.adminCode.value }</span> --%>
   <span>${adminCode }</span>
   <a href="#">[退出]</a>






























