# CSCI 2020U Project Charter & Work Contract

## 1. Project Overview
* **Project members:** Ramiyan Gangatharan, Shawn Heer, Graeme Jeffreys, Sheizah Jimenez, Mina Si Nan Yang
* **Product Name:** TabletopSync
* **Project Description:** TabletopSync is a multiplayer 2D tabletop RPG board application that allows multiple users to connect to a shared session and interact in real time. Users can place and move character tokens, take notes in an integrated reader/writer, and communicate through an integrated chat system. 

### Features
* **Idea:** Shared board that players connect into to add, delete, and move uploaded character/object sprites onto the grid-like canvas. Players also have the ability to update the map image, chat with other connected users through the chatbox, take notes on the provided notepad, as well as use the toolbar and menu bar that provide extra features/content for the players to use. The application will utilize a simple save/load system as well as multithreaded sockets allowing for players to commune, connect, and play together.
* **Core Functions:**
  * Multithreaded player connection.
  * Image upload for map and character/object sprites.
  * Grid-based movement (like actual tabletop games).
  * Chatbox for player-to-player communication.
* **Upgrades:**
  * Notepad.
  * Toolbar Canvas/RPG tools (ex. range/AOE circles, distance measurement, extra objects, etc.).
  * Host/Player permissions.
  * Sprite settings (ex. locking/unlocking object, adding health bar, resize, etc.)
  * Client based settings (ex. Accessibility settings, Username change, etc.)
* **Project Management Tools:** UMLs, Concept Maps, Data Flow Diagrams, UI Diagram, GitHub workflow & tasks, etc. 
* **Project Development Structure:** MVC based build structure

### Tech Stack
Java, Java Swing, XML or JSON (ex. SimpleJSON library), Maven, Multithreading, Fonts & Graphics, and more to be determined (such as data persistence through alternative methods, etc.). *SUBJECT TO CHANGE*


## 2. Team Work Contract
* **Communication Channel:** Discord, GitHub
* **Meeting Schedule:** Monday evenings.
* **Conflict Resolution:** If team members do not contribute to assigned tasks, we will contact the instructor immediately. All team members are also responsible to enable other members to execute their contributions (i.e., push the code needed, etc).

## 3. Work Division & Contribution Report
*Note: The "Actual Contribution" column should be updated at the time of final submission. You may edit the "Task" column to reflect your own tasks.*

| Task / Module | Assigned Member (Plan)          | Actual Contribution (Final) |
| :--- |:--------------------------------| :--- |
| **Multi-threaded Server** | [Ramiyan, Graeme / EVERYONE]    | [Summary of work done] |
| **Socket Networking** | [Ramiyan, Graeme / EVERYONE]    | [Summary of work done] |
| **GUI Implementation** | [Sheizah / Shawn / EVERYONE]    | [Summary of work done] |
| **Persistence (File I/O)** | [Mina / EVERYONE]               | [Summary of work done] |
| **UX/Sound Effects** | [Shawn / Sheizah / EVERYONE]    | [Summary of work done] |
| **Documentation/README** | [Sheizah / Mina / Group Effort] | [Summary of work done] |

## 4. Final Contribution Status (Tag one at Final Submission)
At the end of the project, the team must agree on one of the following tags:

* **[ ] (1) EVEN CONTRIBUTION:** All members met expectations from the original charter.
* **[ ] (2) UNEVEN CONTRIBUTION:** One or more members did not meet expectations.

*Note: Graders will use the "Actual Contribution" column above to apply uneven grades if necessary.*