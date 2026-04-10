# Library & Computer Lab Availability Tracker
### Java Console Application | College Group Project

---

## 👥 Team Split & GitHub Branches

| Friend | Branch Name | Files Owned |
|--------|-------------|-------------|
| Friend 1 | `feature/computer-lab` | `ComputerPC.java`, `LabSystem.java` |
| Friend 2 | `feature/library` | `LibrarySeat.java`, `LibrarySystem.java` |
| Friend 3 | `feature/integration` | `Main.java`, `AuthManager.java`, `User.java`, `ConsoleUI.java`, `CSVHandler.java`, `ActivityLogger.java` |

> ✅ No file is shared between branches → **zero merge conflicts**

---

## 📁 Project Structure

```
LibraryLabTracker/
├── src/
│   ├── Main.java                     ← Friend 3
│   ├── auth/
│   │   ├── User.java                 ← Friend 3
│   │   └── AuthManager.java          ← Friend 3
│   ├── library/
│   │   ├── LibrarySeat.java          ← Friend 2
│   │   └── LibrarySystem.java        ← Friend 2
│   ├── lab/
│   │   ├── ComputerPC.java           ← Friend 1
│   │   └── LabSystem.java            ← Friend 1
│   ├── storage/
│   │   ├── CSVHandler.java           ← Friend 3
│   │   └── ActivityLogger.java       ← Friend 3
│   └── ui/
│       └── ConsoleUI.java            ← Friend 3
├── data/
│   ├── users.csv                     ← auto-created on first run
│   ├── library_seats.csv             ← auto-created on first run
│   ├── lab_pcs.csv                   ← auto-created on first run
│   └── activity_log.csv              ← auto-created on first run
└── README.md
```

---

## 🗂 Data Storage (CSV Files)

### `data/library_seats.csv`
```
seatId,zone,status,occupiedBy,reservedUntil
L-01,Quiet Zone,AVAILABLE,,
L-02,Group Study,OCCUPIED,Ravi,
```

### `data/lab_pcs.csv`
```
pcId,labName,status,occupiedBy,sessionStart,softwareNotes
PC-01,Lab A,AVAILABLE,,,Python,Java
PC-02,Lab B,OCCUPIED,Priya,10:30,MATLAB
```

### `data/activity_log.csv`
```
timestamp,user,action,target
2025-01-10 14:23:00,admin,OCCUPY_SEAT,L-03
```

---

## 🔑 Default Login Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin` | `admin123` |
| Student | `student` | `student123` |

---

## ✨ Feature List

### Student Features
- View availability summary (counts)
- View all library seats / PCs
- Filter available seats/PCs only
- Search library by zone
- Search lab by lab name
- Check any specific seat or PC by ID

### Admin Features
- Occupy / Reserve / Free a library seat
- Add / Remove library seats
- Occupy / Free / Maintenance a PC
- Update PC software notes
- Add / Remove PCs
- All actions auto-logged to `activity_log.csv`

---

## 🔄 Logic Flow — Checking Availability

```
User enters asset ID (e.g. "L-03" or "PC-07")
        │
        ▼
Search in-memory list (loaded from CSV on startup)
        │
   Found? ──NO──▶ Print "Not found"
        │
       YES
        │
        ▼
  Read status field
   ┌─────────────────────┐
   │ AVAILABLE           │──▶ Show green "Available"
   │ OCCUPIED            │──▶ Show red "Occupied by [name]"
   │ RESERVED/MAINTENANCE│──▶ Show yellow status + details
   └─────────────────────┘
```

---

## 🚀 How to Compile & Run

```bash
# From project root
javac -d out src/Main.java src/auth/*.java src/library/*.java src/lab/*.java src/storage/*.java src/ui/*.java

java -cp out src.Main
```

---

## 🌿 GitHub Workflow

```bash
# Each friend works on their branch
git checkout -b feature/computer-lab   # Friend 1
git checkout -b feature/library        # Friend 2
git checkout -b feature/integration    # Friend 3

# When done, push and open a Pull Request to main
git add .
git commit -m "feat: add LabSystem with occupy/free/maintenance"
git push origin feature/computer-lab
```
