# 💕 Anniversary App - Celebrate Love Every Month

A personalized Android app that helps couples celebrate their monthly anniversaries by sending custom wishes, messages, and animations automatically.

## 🎯 App Overview

The Anniversary App creates a romantic experience where boyfriends can set up automated, personalized anniversary messages that are delivered to their girlfriends every month. It features beautiful animations, customizable messages, and secure pairing between couples.

## ✨ Features

### 👨‍💻 For Boyfriends (Setup Side)
- **Easy Setup**: Set relationship start date and personal details
- **Message Template Builder**: Create custom monthly anniversary messages
- **Rich Customization**: 
  - 🎊 Confetti animations
  - 🎨 Custom background colors
  - 📸 Image attachments (placeholder)
  - 🎵 Audio messages (placeholder)
- **Code-based Pairing**: Generate secure 6-digit codes for girlfriend pairing
- **Message Management**: Edit, preview, and manage all anniversary messages
- **Delivery Tracking**: See when messages are delivered and read

### 👩‍💻 For Girlfriends (Receiver Side)
- **Simple Pairing**: Enter boyfriend's code to link profiles
- **Beautiful Message Display**: Receive anniversary messages with:
  - Stunning gradient backgrounds
  - Confetti animations
  - Personalized content
- **Interactive Reactions**: React to messages with emojis
- **Message History**: Browse all received anniversary messages
- **Real-time Updates**: Get notified when new messages arrive

## 🛠️ Technical Stack

| Component | Technology |
|-----------|------------|
| **UI Framework** | Jetpack Compose |
| **Architecture** | MVVM with Repository Pattern |
| **Dependency Injection** | Hilt |
| **Backend** | Firebase Realtime Database |
| **Authentication** | Firebase Auth (Anonymous) |
| **Push Notifications** | Firebase Cloud Messaging |
| **Scheduling** | WorkManager |
| **Animations** | Konfetti Library + Lottie |
| **Date Picker** | Compose Material Dialogs |
| **Navigation** | Navigation Compose |

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 24+ (Android 7.0+)
- Firebase project setup
- Kotlin 1.9.22+

### Firebase Setup

1. **Create Firebase Project**:
   - Go to [Firebase Console](https://console.firebase.google.com)
   - Create a new project named "Anniversary App"
   - Enable Google Analytics (optional)

2. **Add Android App**:
   - Package name: `com.anniversary.app`
   - Download `google-services.json`
   - Replace the placeholder file in `app/google-services.json`

3. **Enable Firebase Services**:
   ```bash
   # Enable these services in Firebase Console:
   - Realtime Database
   - Authentication (Anonymous)
   - Cloud Messaging
   - Storage (for future media features)
   ```

4. **Set Database Rules**:
   ```json
   {
     "rules": {
       "anniversaries": {
         ".read": "auth != null",
         ".write": "auth != null"
       },
       "users": {
         ".read": "auth != null", 
         ".write": "auth != null"
       },
       "wishes": {
         ".read": "auth != null",
         ".write": "auth != null"
       },
       "delivery_status": {
         ".read": "auth != null",
         ".write": "auth != null"
       }
     }
   }
   ```

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/anniversary-app.git
   cd anniversary-app
   ```

2. **Open in Android Studio**:
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Configure Firebase**:
   - Replace `app/google-services.json` with your Firebase config
   - Sync project with Gradle files

4. **Build and Run**:
   ```bash
   ./gradlew assembleDebug
   # Or use Android Studio's Run button
   ```

## 📱 App Workflow

### 1. Role Selection
Users choose between "Boyfriend" (sender) or "Girlfriend" (receiver) roles.

### 2. Boyfriend Setup
- Enter name and relationship start date
- System generates unique 6-digit code
- Share code with girlfriend

### 3. Message Creation
- Create anniversary messages for each month
- Customize with colors, animations, and content
- Preview messages before saving

### 4. Girlfriend Pairing
- Enter the 6-digit code received from boyfriend
- Automatic linking of profiles

### 5. Message Delivery
- Messages appear in girlfriend's app
- Beautiful animations and reactions
- Real-time synchronization

## 🔧 Project Structure

```
app/src/main/kotlin/com/anniversary/app/
├── data/
│   ├── model/           # Data classes (Anniversary, WishMessage, UserProfile)
│   └── repository/      # Firebase repository implementation
├── di/                  # Hilt dependency injection modules
├── notification/        # Firebase messaging service
├── ui/
│   ├── components/      # Reusable UI components
│   ├── navigation/      # Navigation setup
│   ├── screens/         # Screen composables and ViewModels
│   │   ├── boy/        # Boyfriend's screens
│   │   ├── girl/       # Girlfriend's screens
│   │   └── shared/     # Shared screens
│   └── theme/          # UI theme and styling
├── worker/             # Background work for notifications
└── AnniversaryApplication.kt
```

## 🎨 Key Components

### Data Models
- **Anniversary**: Core relationship data and couple code
- **WishMessage**: Individual anniversary messages with customization
- **UserProfile**: User information and roles
- **DeliveryStatus**: Message delivery and reaction tracking

### UI Components
- **ConfettiAnimation**: Celebration animations using Konfetti
- **WishCard**: Beautiful message display cards
- **CreateWishDialog**: Message creation interface
- **CodeDigitBox**: 6-digit code input interface

### ViewModels
- **BoySetupViewModel**: Handles anniversary creation
- **MessageBuilderViewModel**: Manages wish creation and editing
- **CodeEntryViewModel**: Handles code verification and pairing
- **WishDisplayViewModel**: Manages message display and reactions

## 🔐 Security Features

- **Anonymous Authentication**: Users don't need email/password
- **Code-based Pairing**: Secure 6-digit codes for linking
- **Data Isolation**: Each couple's data is separate
- **Firebase Security Rules**: Server-side data protection

## 🚀 Future Enhancements

### Phase 2 Features
- **Media Support**: Real image and audio message uploads
- **Voice Messages**: Record and send voice notes
- **Memory Lane**: Timeline view of anniversary history
- **Custom Animations**: Lottie animation integration
- **Relationship Milestones**: Special messages for 6 months, 1 year, etc.

### Phase 3 Features
- **Push Notifications**: Scheduled anniversary reminders
- **Theme Customization**: Multiple UI themes
- **Export Messages**: Save messages as images or PDFs
- **Multi-language Support**: Internationalization

## 🧪 Testing

```bash
# Run unit tests
./gradlew test

# Run instrumentation tests  
./gradlew connectedAndroidTest

# Run all tests
./gradlew check
```

## 📝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 💖 Acknowledgments

- **Konfetti Library** for beautiful confetti animations
- **Firebase** for backend services
- **Jetpack Compose** for modern UI development
- **Material Design 3** for design guidelines

## 🆘 Support

For support and questions:
- 📧 Email: support@anniversaryapp.com
- 🐛 Issues: [GitHub Issues](https://github.com/yourusername/anniversary-app/issues)
- 📖 Documentation: [Wiki](https://github.com/yourusername/anniversary-app/wiki)

---

**Made with ❤️ for couples celebrating love** 💕