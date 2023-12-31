<%--
  Created by IntelliJ IDEA.
  User: mikae
  Date: 2023-09-14
  Time: 00:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="sidebar border border-right col-md-3 col-lg-2 bg-body-tertiary">
    <div class="offcanvas-md offcanvas-end bg-body-tertiary" tabindex="-1" id="sidebarMenu"
         aria-labelledby="sidebarMenuLabel">
        <div class="offcanvas-header">
            <h5 class="offcanvas-title" id="sidebarMenuLabel">Company name</h5>
            <button type="button" class="btn-close" data-bs-dismiss="offcanvas"
                    data-bs-target="#sidebarMenu" aria-label="Close"></button>
        </div>
        <div class="offcanvas-body d-md-flex flex-column p-0 pt-lg-3 overflow-y-auto">
            <ul class="nav flex-column">
                <li class="nav-item">
                    <a class="nav-link d-flex align-items-center gap-2 <% if(request.getParameter("content").equals("start.jsp")) { %> active <% } %>"
                       aria-current="page" href="${pageContext.request.contextPath}/">
                        <svg class="bi">
                            <use xlink:href="#house-fill"/>
                        </svg>
                        Dashboard
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link d-flex align-items-center gap-2 <% if(request.getParameter("content").equals("reports.jsp") || request.getParameter("content").equals("view-report.jsp")) { %> active <% } %>"
                       href="${pageContext.request.contextPath}/reports">
                        <svg class="bi">
                            <use xlink:href="#graph-up"/>
                        </svg>
                        Reports
                    </a>
                </li>
            </ul>

            <% if (request.getParameter("role").contains("admin")) { %>

            <hr class="my-3">

            <h6 class="sidebar-heading d-flex align-items-left px-3 gap-2 mt-2 mb-1 ml0-m text-body-secondary text-uppercase">
                <svg class="bi">
                    <use xlink:href="#door-closed"></use>
                </svg>
                <span>Admin</span>
            </h6>
            <ul class="nav flex-column mb-auto">
                <li class="nav-item">
                    <a class="nav-link d-flex align-items-center gap-2 <% if(request.getParameter("content").equals("user-admin.jsp") || request.getParameter("content").equals("file.jsp")) { %> active <% } %>"
                       href="${pageContext.request.contextPath}/admin/users">
                        <svg class="bi">
                            <use xlink:href="#door-closed"></use>
                        </svg>
                        User magament
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link d-flex align-items-center gap-2 <% if(request.getParameter("content").equals("project-admin.jsp") || request.getParameter("content").equals("project-admin-add.jsp") || request.getParameter("content").equals("project-admin-edit.jsp") || request.getParameter("content").equals("project-admin-users.jsp")) { %> active <% } %>"
                       href="${pageContext.request.contextPath}/admin/projects">
                        <svg class="bi">
                            <use xlink:href="#file-earmark-text"></use>
                        </svg>
                        Project magament
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link d-flex align-items-center gap-2 <% if(request.getParameter("content").equals("report-admin.jsp") || request.getParameter("content").equals("view-report-admin.jsp")) { %> active <% } %>"
                       href="${pageContext.request.contextPath}/admin/reports">
                        <svg class="bi">
                            <use xlink:href="#file-earmark-text"></use>
                        </svg>
                        Report magament
                    </a>
                </li>
            </ul>

            <% } %>

            <hr class="my-3">

            <ul class="nav flex-column mb-auto">
                <li class="nav-item">
                    <a class="nav-link d-flex align-items-center gap-2" href="#">
                        <svg class="bi">
                            <use xlink:href="#gear-wide-connected"/>
                        </svg>
                        Settings
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link d-flex align-items-center gap-2"
                       href="${pageContext.request.contextPath}/login?logout=true">
                        <svg class="bi">
                            <use xlink:href="#door-closed"/>
                        </svg>
                        Sign out
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>
