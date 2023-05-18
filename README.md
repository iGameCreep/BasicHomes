# BasicHomes
## A basic plugin to manage your homes in your Minecraft server using Postgresql !

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white) ![Database](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)

## Requirements
- **Java** and **Maven** to build the .jar file
- **Postgresql** for the database

## Installing

- Clone the repository
- Open the `src/main/resources/config.yml` file to access the database configuration
- Replace the `jdbc-url`, `username` and `password` variables by the ones you use to connect to your database.
- Save the files, compile the project with `mvn package` command and the **.jar** file of the plugin will appear in the **target** folder !

## Commands

Here are all the commands available at the moment !

| Command  | Usage           | Description |
|----------|-----------------| ------ |
| /home    | /home (name)    | Teleport to a home by it's name ! |
| /homes   | /homes          | Get all of your homes ! |
| /sethome | /sethome (name) | Create a home and give it a name ! |
| /delhome | /delhome (name) | Delete a home by it's name ! |
| /account | /account        | Create your account to manage your homes online ! |


#### Account Commands

| Command                 | Usage                   | Description |
|-------------------------|-------------------------| ------ |
| /account                | /account                | Create your account to manage your homes online ! |
| /account id             | /account id             | Retrieve your account ID ! |
| /account add-server     | /account add-server     | Add a new Minecraft server using BasicHomes to your account ! |
| /account reset-password | /account reset-password | Reset the password of your account ! |
| /account delete         | /account delete         | Delete your account ! |

# Online Dashboard

The online dashboard is available [here](https://github.com/iGameCreep/BasicHomes-Dashboard) !