# Postora - A Spring Boot Blogging Application (Backend Only)

> _"Postora" - A place where posts shine and flow over time._

## Overview

Postora is a fully functional RESTful backend for a modern blogging platform. Built using **Spring Boot**, this application supports user authentication, role-based authorization, content management, likes, comments, bookmarks, email notifications, and more.

This is a **backend-only** project — designed to be easily integratable with any frontend (React, Angular, etc.).

---

## Features

### Authentication & Authorization
- JWT-based login & registration
- Role-based access control (`ROLE_USER`, `ROLE_ADMIN`)
- Secure endpoints using Spring Security

### Blog Post Management
- Full CRUD support
- One post → one category
- One post → many tags
- Search, pagination & sorting
- View posts by user or filter by category/tag

### Tags & Categories
- Admin can manage categories
- Tags added dynamically at post creation

### Commenting System
- Comment on posts
- Users can update/delete their own comments
- Post author and admin can delete any comment
- Total comment count per post

### Likes
- Like/unlike posts
- Prevent duplicate likes
- List of users who liked a post
- Total like count per post

### Bookmarks
- Bookmark/unbookmark posts
- View all bookmarks of a user
- Prevent duplicate bookmarks
- Total bookmark count per post

### Email Notifications
- Async email on registration, profile update and profile delete to user using JavaMailSender
- Async email on post creation,update, and delete to the author using JavaMailSender

### User Profile
- Users can view & update their own profile
- Admins can access any profile
- Users can view other user profiles

### Tech Stack
- Spring Boot
- Spring Security + JWT
- Spring Data JPA + Hibernate
- Project Lombok
- JavaMailSender (Async email)
- ModelMapper (DTO conversion)
- Bean Validation (Jakarta)
- Swagger/OpenAPI for documentation
- Postman for testing

---

## Security
- Passwords hashed using **BCrypt**
- JWT token included in each request after login
- Access controlled via method-level `@PreAuthorize` and role checks

---

### Author
**Ritik Raj**  
_MCA @ VIT Vellore_  
Java & Spring Boot Enthusiast  
[LinkedIn](https://www.linkedin.com/in/ritik-raj-8a6641224/)  
[GitHub](https://github.com/ritikkraj)