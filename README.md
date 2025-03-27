
# **🚲 Bike Rental System for Sustainable Cities**

## **📌 Overview**
This project is part of a sustainable cities initiative, aiming to provide a **bike and bicycle rental platform** for businesses and citizens. Businesses can **list their bikes**, and citizens can **browse available bikes, view details, and rent them**. The system is built using **Firebase Firestore** for real-time database operations.

---

## **📌 Features**
### **For Businesses (NGOs, Shops, Individuals)**
✅ Add bikes and bicycles for rental  
✅ Upload images and details (type, price, availability)  
✅ Manage listed vehicles  

### **For Citizens**
✅ View available bikes for rent  
✅ Check bike details (name, type, price, image)  
✅ Directly contact the owner to rent a bike  

---

## **📌 Technologies Used**
- **Android (Java)**
- **Firebase Firestore** (Database)
- **Glide** (Image Loading)
- **RecyclerView** (List Display)

---

## **📌 Installation and Setup**
1️⃣ Clone this repository:
```bash
git clone https://github.com/your-repo-url.git
```
2️⃣ Open the project in **Android Studio**  
3️⃣ Connect Firebase:
   - Go to **Firebase Console**
   - Add your **google-services.json** file  
4️⃣ Run the project on an emulator or physical device.

---

## **📌 Firebase Firestore Structure**
### **Collection: `rental_bikes`**
| Field | Type | Description |
|-------|------|-------------|
| `name` | String | Name of the bike |
| `type` | String | Type (e.g., Electric, Manual) |
| `price` | String | Rental price per hour |
| `imageUrl` | String | Bike image URL |
| `ownerContact` | String | Contact number for renting |

---

## **📌 Future Enhancements**
🚀 Online booking & payments  
🚀 GPS tracking of bikes  
🚀 User reviews and ratings  

---

## **📌 Contributors**
- **Your Name** (Android Developer)
- **Other Team Members** (If applicable)

---

## **📌 License**
📜 This project is **open-source**. Feel free to modify and improve!  
