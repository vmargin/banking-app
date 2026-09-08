# Local PostgreSQL setup

BA-07 uses PostgreSQL through the official JDBC driver. These steps configure a local development database without putting credentials in Git.

## Check the server

```powershell
Get-Service -Name postgresql-x64-18
& 'C:\Program Files\PostgreSQL\18\bin\pg_isready.exe' -h localhost -p 5432
```

The server must report `accepting connections`.

## Create the database

Using pgAdmin or SQL Shell, run:

```sql
CREATE DATABASE banking_app;
```

Run the repository schema against that database:

```powershell
& 'C:\Program Files\PostgreSQL\18\bin\psql.exe' `
  -h localhost -U postgres -d banking_app `
  -f 'src/main/resources/schema.sql'
```

Enter the local PostgreSQL password when prompted. Never place that password in this repository or in an issue comment.

## Configure one PowerShell session

```powershell
$env:BANKING_DB_URL = 'jdbc:postgresql://localhost:5432/banking_app'
$env:BANKING_DB_USER = 'postgres'
$env:BANKING_DB_PASSWORD = 'YOUR_LOCAL_PASSWORD'
```

The variables disappear when that PowerShell window closes. The connection utility also accepts the default URL when `BANKING_DB_URL` is omitted.

## Verify

```powershell
.\scripts\dev.ps1 -Task style
.\scripts\dev.ps1 -Task test
```

Without the two credential variables, the JDBC integration test is skipped rather than falsely passing. BA-07 is complete only after the test executes successfully against your local database and the schema tables exist.
