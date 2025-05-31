# ETransportApp

**ETransportApp** is an Android application that connects logistics vehicle owners and cargo providers through a fair, transparent, and direct freight-sharing system — eliminating middlemen and enabling data-driven pricing.

The app is built using modern Android technologies such as **Jetpack Compose**, **MVVM**, and **Kotlin**, and communicates with a .NET-based backend that handles business logic, real-time updates, and pricing via machine learning.

---

## Key Features

- Post and browse cargo and vehicle ads
- Submit and manage transport offers
- Add and edit personal vehicle information
- Auto-fill ads with saved vehicle data
- City selection via GeoNames API
- Multi-currency support with exchange rate conversion
- Suggested price range based on backend ML model
- Partial transport fee calculator

---

## Tech Overview

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture**: MVVM + StateFlow
- **Networking**: Retrofit
- **External APIs**: GeoNames, Open Exchange Rates API
- **Backend**: .NET (handled separately)

---

## Notes

- The app is frontend-only and requires an active backend environment for full functionality.
- Price recommendations, route calculations, and user authentication are served by a separate .NET backend.

---