SOLID Principles
S – Single Responsibility
Each class has one responsibility:
MediaServiceImpl – media business logic
JdbcMediaRepository – database queries
SortingUtils – sorting/filtering logic

O – Open/Closed
New media types can be added without changing existing logic.


L – Liskov Substitution
Song and Podcast extend MediaContentBase and can be used polymorphically.

I – Interface Segregation
Small interfaces:
Playable
PricedItem

OOP Concepts
Inheritance
MediaContentBase
├── Song
└── Podcast

Polymorphism
MediaContentBase m = new Song(...);
m.play();

Encapsulation
Private fields with getters/setters.


Composition
MediaContentBase contains Category. 
Database Schema

Tables:
categories
media
playlists
playlist_items


Relationships:
One-to-Many: Category → Media
Many-to-Many: Playlist ↔ Media



Feature:
CRUD Operations
Create media
Read all media
Update media
Delete media
Sorting & Filtering (Lambdas)
Sort by name
Sort by duration
Filter by type
Validatable

