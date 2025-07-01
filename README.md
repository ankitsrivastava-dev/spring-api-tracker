# 🚀 Spring API Tracker

> Built with ❤️ by Ankit Srivastava

A lightweight, zero-config Spring Boot library to track all your API calls with one annotation.

## ✨ Features
- @TrackApi — one annotation to enable full tracking
- Logs parameters, response, errors, and execution time
- @Sensitive — hides sensitive fields like password, token
- View logs at `/api-tracker/logs`
- Plug-n-play in 60 seconds
- Smart config via `application.properties`

## 📦 Quick Start

```xml
<dependency>
  <groupId>com.ankitsrivastava</groupId>
  <artifactId>spring-api-tracker</artifactId>
  <version>1.0.0</version>
</dependency>

Enable with @EnableApiTracker
Annotate APIs with @TrackApi(name = "Your API Name")

Star ⭐ this repo if you like it and follow Ankit Srivastava