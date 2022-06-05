package fmi.user

case class User(
  id: UserId,
  passwordHash: String,
  role: UserRole,
  name: String,
  age: Option[Int]
)

case class UserId(email: String)

enum UserRole:
  case Admin, NormalUser
