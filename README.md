# Zmail

A simple, extensible email sending service built with Spring Boot. Supports synchronous and asynchronous email delivery,
template-based emails, and file attachments.

## Features

- Send basic emails (plain text)
- Send advanced emails with FreeMarker templates
- Attach files to emails (from resources or user uploads)
- Asynchronous email sending for improved performance
- RESTful API endpoints
- Robust logging with SLF4J
- Mail username and password are read from environment variables for security

## Technologies Used

- Java 17+
- Spring Boot
- Spring Mail
- FreeMarker (for email templates)
- SLF4J (logging)

## Getting Started

### Prerequisites

- Java 17 or newer
- Maven

### Configuration

Edit `src/main/resources/application.properties` and `messages.properties` to set up your mail server and custom
messages.

**Mail username and password are read from environment variables.**
Set the following environment variables before running the application:

- `SYSTEM_EMAIL_ID` (your email address)
- `SYSTEM_EMAIL_PASSWORD` (your email password)

Example mail configuration:

```
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.username=${SYSTEM_EMAIL_ID}
spring.mail.password=${SYSTEM_EMAIL_PASSWORD}
```

### Build & Run

```
mvn clean install
mvn spring-boot:run
```

### API Endpoints

- `POST /mail/sendBasic` - Send a simple email
- `POST /mail/sendAdvanced` - Send a template-based email with a sample attachment
- `POST /mail/sendWithYourAttachment` - Send a template-based email with user-uploaded attachments

#### Example Request (sendBasic)

```
POST /mail/sendBasic
Content-Type: application/json
{
  "to": "recipient@example.com",
  "subject": "Hello",
  "body": "Welcome to Zmail!"
}
```

## Templates

Email templates are stored in `src/main/resources/templates/` (FreeMarker format: `.ftlh`).

## Logging

All major actions and errors are logged using SLF4J. Check your console or configure a log file in
`application.properties`.

## Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

## License

This project is licensed under the MIT License.

## Author

poyrazemun

