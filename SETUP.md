# 🚀 Quick Setup Guide - Anniversary App

## 📱 Build APK with GitHub Actions (No Android Studio Required!)

### Step 1: Fork the Repository
1. Click the "Fork" button on this repository
2. Clone your fork to your local machine (optional)

### Step 2: Setup Firebase (Required)
1. Go to [Firebase Console](https://console.firebase.google.com)
2. Create a new project called "Anniversary App"
3. Add an Android app with package name: `com.anniversary.app`
4. Download the `google-services.json` file
5. Replace the placeholder file at `app/google-services.json` with your downloaded file

### Step 3: Enable Firebase Services
In your Firebase console, enable:
- ✅ **Realtime Database** (set rules to test mode initially)
- ✅ **Authentication** → Sign-in method → Anonymous (enable)
- ✅ **Cloud Messaging** (automatic)

### Step 4: Trigger Build
1. Commit and push your changes to the `main` branch
2. GitHub Actions will automatically start building
3. Go to the "Actions" tab in your GitHub repository
4. Wait for the build to complete (usually 3-5 minutes)

### Step 5: Download APK
1. Click on the completed workflow run
2. Scroll down to "Artifacts" section
3. Download `anniversary-app-debug.zip`
4. Extract and install the APK on your Android device

## 🔧 Troubleshooting

### Build Fails?
- Make sure you replaced `google-services.json` with your actual Firebase config
- Check that Firebase services are enabled in your project
- Verify the package name in Firebase matches `com.anniversary.app`

### APK Won't Install?
- Enable "Install from unknown sources" in Android settings
- Make sure your device runs Android 7.0+ (API 24+)

### Can't Access Firebase Features?
- Set Realtime Database rules to:
```json
{
  "rules": {
    ".read": "auth != null",
    ".write": "auth != null"
  }
}
```

## 🎯 Test the App

### As Boyfriend:
1. Open app → Choose "I'm the Boyfriend"
2. Enter your name and relationship start date
3. Create anniversary messages
4. Share the 6-digit code with your girlfriend

### As Girlfriend:
1. Open app → Choose "I'm the Girlfriend"
2. Enter the 6-digit code from your boyfriend
3. View and react to anniversary messages

## 🆘 Need Help?
- Check the main [README.md](README.md) for detailed documentation
- Create an issue in this repository for bugs
- Make sure you're using the latest code from the main branch

---
**Happy Anniversary Celebrations! 💕**