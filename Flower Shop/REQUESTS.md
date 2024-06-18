## Request Examples

### Book

```
POST /books
Content-Type: application/json

{
  "title": "The Great Gatsby",
  "author": "F. Scott Fitzgerald",
  "publicationYear": 1925,
  "genre": "FICTION",
  "status": "AVAILABLE"
}
```

### Member

```
POST /members
Content-Type: application/json

{
  "name": "John Doe",
  "email": "johndoe@example.com",
  "membershipType": "STANDARD"
}
```
