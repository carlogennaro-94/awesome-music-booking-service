# Awesome Music Booking Service

Technical assignment implemented with Java 21 and Spring Boot 3.

The application provides REST APIs to manage rehearsal room booking requests for Awesome Music.

A booking is created in `PENDING` status and can later be `APPROVED` or `REJECTED`. Each booking has a unique booking code that can be used to retrieve its current status.

## Features

- Create a rehearsal room booking
- Retrieve all pending bookings
- Retrieve a booking by booking code
- Approve or reject a booking
- Prevent approval of multiple bookings for the same room, date and time slot
- Input validation and centralized API error handling

## Booking slots

Available booking slots are:

- `MORNING`
- `AFTERNOON`
- `EVENING`

## Booking lifecycle

```text
PENDING
  ├── APPROVED
  └── REJECTED
