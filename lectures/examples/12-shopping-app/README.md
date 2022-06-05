# Shopping Application

# DB Setup

Install PostgreSQL. Once installed go into `psql` from the command line (instructions should be available depending on you method of installation) and enter:

```
CREATE DATABASE shoppingapp;
CREATE USER shoppingapp WITH ENCRYPTED PASSWORD 'secret-P@assw0rd';
GRANT ALL PRIVILEGES ON DATABASE shoppingapp TO shoppingapp;
```

# Run application

The application can be started with `sbt run`.
