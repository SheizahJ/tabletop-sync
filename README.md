# TabletopSync 

## Meet the Team 

- **Sheizah Jimenez**: Project Lead + Front-End Developer
- **Shawn Heer**: Lead Front-End Developer + UX Designer
- **Mina Si Nan Yang**: Persistence Manager + UI Designer
- **Graeme Jeffreys**: Primary Network Manager + Back-End Developer
- **Ramiyan Gangatharan**: Lead Back-End Developer + Network Support

## Project Overview 

### Description
TabletopSync is a multiplayer 2D tabletop RPG board application that allows multiple users to connect to a shared session and interact in real time. Users can place and move character tokens, take notes in a persistence-based notepad, and communicate through an integrated chat system.

### Key Features
  - **Main Canvas**
    - Adding Sprites to the Canvas w/ uploaded image
    - Update the Map/background w/ uploaded image
    - *Toggle Snap to Grid for sprites
  - **Sprite Objects**
    - Draggable objects
    - Lock/Unlock (using PopupMenu)
    - Delete from Canvas (using PopupMenu or 'Del' key)
    - Selected Sprite shows info of Player who selected
  - **Chatbox**
    - *TextField to type messages into
    - Send message (using Send button or 'Enter' key)
    - Player List w/ Player names and assigned colours
  - **Notepad**
    - Create New notes
    - *TextArea to write/update notes into
    - Open Saved notes (a list of saved notes is provided)
    - Save new/opened notes
    - Delete new/opened notes
  
  _*These features are Client-sided only. All other features communicate to the Server in some way._

### Process & Documentation



**Data Flow Diagrams (DFD)**: The initial expectations of how the system interacts with one another

<div align="center">
  <img width="500" alt="image" src="https://github.com/user-attachments/assets/2351ad08-d229-4e24-9805-d3d2443965e9" />
  <img width="500" alt="image" src="https://github.com/user-attachments/assets/543957f3-79dd-47f8-b484-34fb9ff6c240" />
  <img width="500" alt="image" src="https://github.com/user-attachments/assets/da91310c-e340-4196-b765-6fe7c3eba054" />
  <img width="500" alt="image" src="https://github.com/user-attachments/assets/a288ee4a-930c-4430-b7df-8c89f5eb6b3f" />
  <img width="500" alt="image" src="https://github.com/user-attachments/assets/61d075a3-ec50-46e8-9b7f-33d2694d2f12" />
</div>

<br/>

**UI Diagrams**: A basic idea of how the application should look/feel when the user uses the application.

<div align="center">
  <img width="500" alt="image" src="https://github.com/user-attachments/assets/1deb8004-000d-4349-9b23-3cef42ecbdb0" />
  <img width="500" alt="image" src="https://github.com/user-attachments/assets/c8895bdc-4bf3-48e8-825e-22c6f9c1b255" />
  <img width="500" alt="image" src="https://github.com/user-attachments/assets/85e5946d-9c7d-429d-b0ad-ee45cdd59267" />
  <img width="500" alt="image" src="https://github.com/user-attachments/assets/feeda490-97cd-4dce-993f-b88a4455aeb7" />
  <img width="1000" alt="image" src="https://github.com/user-attachments/assets/b63a7af9-9c6a-46ea-83c1-c744d1cde27e" />
  <img width="1000" alt="image" src="https://github.com/user-attachments/assets/0e064d2c-fa3a-43f0-8522-ccc7520ec0eb" />
  <img width="500" alt="image" src="https://github.com/user-attachments/assets/54ec1d98-bad9-4356-a1d3-b66a5e60a740" />
</div>

## Project Demo

### Video Demo
<video controls src="assets/demonstration.mp4" title="Title"></video>


### Login Page
<img width="990" height="624" alt="image" src="https://github.com/user-attachments/assets/3592152d-273e-4ab0-9f56-dfe6cc71a957" />

### Canvas (Join Messages + Initial State)
<img width="1920" height="1029" alt="image" src="https://github.com/user-attachments/assets/f641d3f8-33fc-41be-9911-bc96df9c4aa5" />

### Move Sprites + Selected Sprite Highlight
<img width="1920" height="1028" alt="image" src="https://github.com/user-attachments/assets/f54f846c-a11c-4405-a8ee-e34089ce7e27" />

### Add a Sprite
<img width="1920" height="1029" alt="image" src="https://github.com/user-attachments/assets/5259e615-ea4d-4090-a0bd-353f7abe1e52" />

### Running 2 Clients
<img width="1920" height="1029" alt="image" src="https://github.com/user-attachments/assets/372cdc44-af94-497f-ab6f-ea8b698ee47e" />

### Chatbox
<div align="center">
  <img width="350" alt="image" src="https://github.com/user-attachments/assets/310785de-eda0-4013-b1c0-61de27b02c6b" />
  <img width="350" alt="image" src="https://github.com/user-attachments/assets/9e73b2ea-be48-498c-9b18-6d23c5fd13ce" />
</div>

### Notepad
<div align="center">
  <img width="350" alt="image" src="https://github.com/user-attachments/assets/47854402-882f-46d7-8fe3-26f970974d45" />
  <img width="350" alt="image" src="https://github.com/user-attachments/assets/25d54d28-30df-4053-adf3-c1be8a8250c4" />
</div>

## How to Run
1. git clone `https://github.com/OntarioTech-CS-program/w26-csci2020u-finalproject-w26-team16`
2. Change directory to the project folder (ex. `cd ..\Software Systems\Assignments\w26-csci2020u-finalproject-w26-team16`)
3. Run `Server.java`
4. Run `Client.java`
   * Can run multiple Clients at once...

## AI Disclosure

- Assistance with debugging code snippets.
- Minor UI inquiries (e.g. How to create items in a JPopupMenu)
- Assisted with mathematical equations related to the camera movement, grid-related features, sprite dragging, etc.
- Consultation purposes (e.g. What is the best way to do <insert-topic-here>)

## Resources
- **Flatlaf Dependency**: Creates the Server UI in a similar fashion to a regular Terminal.
- **Jackson Databind Dependency**: Used for JSON inquiries.
- **Stack Overflow**: Used to help with implementation of network and minor UI inquiries.

