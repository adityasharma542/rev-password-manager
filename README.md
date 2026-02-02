# RevPassword Manager

## Project Overview
RevPassword Manager is a secure, console-based Java application that allows users to store and manage passwords safely.
The application uses encryption, OTP verification, logging, and unit testing.

---

## Technologies Used
- Java
- JDBC
- MySQL
- Log4J
- JUnit

---

## Features
- User Registration and Login
- Master Password Encryption
- Add, View, and Delete Account Passwords
- Security Question for Account Recovery
- OTP Verification for Sensitive Operations
- Logging using Log4J
- Unit Testing using JUnit

---

## Database Design
Database Name: `password_manager`

### Tables Used:
1. users
2. passwords
3. security_questions

---

## Architecture
The application follows a layered architecture:
- Presentation Layer (Main.java)
- Service Layer (UserService, PasswordService)
- Utility Layer (DBUtil, EncryptionUtil, OTPUtil)
- Database Layer (MySQL)

---

## Logging
Log4J is used for logging:
- INFO for successful operations
- WARN for invalid actions
- ERROR for exceptions

---

## Testing
JUnit is used for unit testing:
- EncryptionUtilTest
- UserServiceTest

---

## How to Run

### Compile
```bash
javac -cp "lib/*" src/app/*.java src/service/*.java src/util/*.java
