<%--
  Created by IntelliJ IDEA.
  User: TRUONGMAI
  Date: 11/30/2022
  Time: 2:22 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>User Management Application</title>
</head>
<body>
<center>
  <h1>User Management</h1>
  <h2>
    <a href="/users?action=create">Add New User</a>
  </h2>
  <h3>
    <a href="/users?action=permision">permision</a>
  </h3>
  <h3>
    <a href="/users?action=test-without-tran">test-without-tran</a>
  </h3>
  <form action="" method="get">
    <table>
      <tr>
        <td>
          <input type="text" name="search" id="search" placeholder="Search">
        </td>
        <td>
          <input type="submit" value="Search">
        </td>
      </tr>
    </table>
  </form>
  <form action="" method="get">
    <table>
      <tr>
        <td>
          <input type="submit" value="sort by name" name="sort">
        </td>
      </tr>
    </table>
  </form>
</center>

<div align="center">
  <table border="1" cellpadding="5">
    <caption><h2>List of Users</h2></caption>
    <tr>
      <th>ID</th>
      <th>Name</th>
      <th>Email</th>
      <th>Country</th>
      <th>Actions</th>
    </tr>
    <c:forEach var="user" items="${listUser}">
      <tr>
        <td><c:out value="${user.id}"/></td>
        <td><c:out value="${user.name}"/></td>
        <td><c:out value="${user.email}"/></td>
        <td><c:out value="${user.country}"/></td>
        <td>
          <a href="/users?action=edit&id=${user.id}">Edit</a>
          <a href="/users?action=delete&id=${user.id}">Delete</a>
        </td>
      </tr>
    </c:forEach>
  </table>
</div>

</body>
</html>