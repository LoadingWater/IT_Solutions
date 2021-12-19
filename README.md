# Project description
Project was created as a task for IT_Solutions.</br>
It allows you to add products to your lists, which you can create, modify and save.</br>
Lists support change of date and title.</br>

<img src="https://user-images.githubusercontent.com/46053098/146675765-4fee0740-f031-4388-885b-6940cc0d4e0e.jpg" width=20% height=20%> <img src="https://user-images.githubusercontent.com/46053098/146675805-aca035f5-1e94-4841-8ba6-c29ea35c9353.jpg" width=20% height=20%> <img src="https://user-images.githubusercontent.com/46053098/146675806-dbd71637-28d0-41f8-aff9-5c5478496e6f.jpg" width=20% height=20%> <img src="https://user-images.githubusercontent.com/46053098/146675807-dbf2b394-1f4c-4d2d-bd3a-3bc4c86a8938.jpg" width=20% height=20%>


### How does this work?
- I used fragments navigation with MVC pattern. Fragments have controllers which responsible for initializing/updating data on the screen.</br></br>
- To save data to db i have this route: load data from db to ViewModel -> modify data in ViewModel -> save data from VM to DB when we leave fragment.
This way we don't have to make db request every time user change something.</br></br>
- Libs used: Volley, Glide, Room, Fragments Navigation etc. Full list in a gradle file.

## What doens't work?
- Nothing. Everything is fine
